package com.example.stockpos.app.service.impl;

import com.example.stockpos.app.dto.requests.PaginationRequest;
import com.example.stockpos.app.dto.requests.UserRequest;
import com.example.stockpos.app.dto.responses.UserResponse;
import com.example.stockpos.app.dto.responses.PaginationMeta;
import com.example.stockpos.app.dto.responses.PaginationResponse;
import com.example.stockpos.app.models.User;
import com.example.stockpos.app.models.Role;
import com.example.stockpos.app.exception.UserNotFoundException;
import com.example.stockpos.app.exception.RoleNotFoundException;
import com.example.stockpos.app.exception.DuplicateResourceException;
import com.example.stockpos.app.repository.UserRepository;
import com.example.stockpos.app.repository.RoleRepository;
import com.example.stockpos.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> findAll() {
        return repository.findAll().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public PaginationResponse<UserResponse> findAllWithPagination(PaginationRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage(), 
                request.getLimit(), 
                request.getOrderBy() != null && !request.getOrderBy().isEmpty() ? Sort.by(request.getOrderBy()) : Sort.unsorted()
        );

        Specification<User> spec = (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (request.getSearch() != null && !request.getSearch().isEmpty()) {
                String searchPattern = "%" + request.getSearch().toLowerCase() + "%";
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), searchPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchPattern)
                ));
            }

            if (request.getFilters() != null) {
                if (request.getFilters().containsKey("status")) {
                    predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("status"), Boolean.parseBoolean(request.getFilters().get("status"))));
                }
                if (request.getFilters().containsKey("roleId")) {
                    predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("role").get("id"), Integer.parseInt(request.getFilters().get("roleId"))));
                }
            }

            return predicates;
        };

        Page<User> userPage = repository.findAll(spec, pageable);
        List<UserResponse> content = userPage.getContent().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());

        int from = (int) userPage.getPageable().getOffset() + 1;
        int to = from + userPage.getNumberOfElements() - 1;

        PaginationMeta meta = PaginationMeta.builder()
                .total(userPage.getTotalElements())
                .from(userPage.getNumberOfElements() > 0 ? from : 0)
                .to(userPage.getNumberOfElements() > 0 ? to : 0)
                .page(userPage.getNumber())
                .limit(userPage.getSize())
                .build();

        return PaginationResponse.<UserResponse>builder()
                .data(content)
                .meta(meta)
                .build();
    }

    @Override
    public UserResponse findById(Integer id) {
        return repository.findById(id)
                .map(UserResponse::fromEntity)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public UserResponse create(UserRequest.CreateUserRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User with email " + request.getEmail() + " already exists");
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RoleNotFoundException(request.getRoleId()));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(request.getStatus())
                .role(role)
                .build();
        
        return UserResponse.fromEntity(repository.save(user));
    }

    @Override
    public UserResponse update(Integer id, UserRequest.UpdateUserRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        
        // Check if new email is already taken by another user
        repository.findByEmail(request.getEmail()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(id)) {
                throw new DuplicateResourceException("User with email " + request.getEmail() + " already exists");
            }
        });

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setStatus(request.getStatus());
        
        return UserResponse.fromEntity(repository.save(user));
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

}
