package com.example.stockpos.app.service.impl;

import com.example.stockpos.app.dto.requests.PaginationRequest;
import com.example.stockpos.app.dto.requests.UserRequest;
import com.example.stockpos.app.dto.responses.PaginationMeta;
import com.example.stockpos.app.dto.responses.PaginationResponse;
import com.example.stockpos.app.dto.responses.UserResponse;
import com.example.stockpos.app.exception.DuplicateResourceException;
import com.example.stockpos.app.exception.RoleNotFoundException;
import com.example.stockpos.app.exception.UserNotFoundException;
import com.example.stockpos.app.models.Role;
import com.example.stockpos.app.models.User;
import com.example.stockpos.app.repository.RoleRepository;
import com.example.stockpos.app.repository.UserRepository;
import com.example.stockpos.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        // Build filters dynamically
        Specification<User> spec = (root, query, cb) -> cb.conjunction();

        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            String keyword = "%" + request.getSearch().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("username")), keyword),
                    cb.like(cb.lower(root.get("email")), keyword)
            ));
        }

        if (request.getFilters() != null) {
            if (request.getFilters().containsKey("status")) {
                boolean isActive = Boolean.parseBoolean(request.getFilters().get("status"));
                spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), isActive));
            }
            if (request.getFilters().containsKey("roleId")) {
                int roleId = Integer.parseInt(request.getFilters().get("roleId"));
                spec = spec.and((root, query, cb) -> cb.equal(root.get("role").get("id"), roleId));
            }
        }

        // Fetch and return
        Page<User> page = repository.findAll(spec, request.toPageable());
        return PaginationResponse.<UserResponse>builder()
                .data(page.getContent().stream().map(UserResponse::fromEntity).collect(Collectors.toList()))
                .meta(PaginationMeta.fromPage(page))
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

        Integer roleId = request.getRoleId();
        if (roleId == null) {
            roleId = roleRepository.findByName("USER")
                    .map(Role::getId)
                    .orElseThrow(() -> new RoleNotFoundException("Default role 'USER' not found"));
        }

        final Integer finalRoleId = roleId;
        Role role = roleRepository.findById(finalRoleId)
                .orElseThrow(() -> new RoleNotFoundException(finalRoleId));

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
