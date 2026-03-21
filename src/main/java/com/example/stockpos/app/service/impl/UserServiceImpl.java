package com.example.stockpos.app.service.impl;

import com.example.stockpos.app.dto.common.request.IdRequest;
import com.example.stockpos.app.dto.common.request.PaginationRequest;
import com.example.stockpos.app.dto.user.request.UserRequest;
import com.example.stockpos.app.dto.common.response.PaginationMeta;
import com.example.stockpos.app.dto.common.response.PaginationResponse;
import com.example.stockpos.app.dto.user.response.UserResponse;
import com.example.stockpos.app.exception.common.DuplicateResourceException;
import com.example.stockpos.app.exception.role.RoleNotFoundException;
import com.example.stockpos.app.exception.user.UserNotFoundException;
import com.example.stockpos.app.models.Role;
import com.example.stockpos.app.models.User;
import com.example.stockpos.app.repository.RoleRepository;
import com.example.stockpos.app.repository.UserRepository;
import com.example.stockpos.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
    @Transactional
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
    @Transactional
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
    @Transactional
    public void softDelete(IdRequest request) {
        User user = repository.findById(request.getId())
                .orElseThrow(() -> new UserNotFoundException(request.getId()));
        user.setDeleted(true);
        user.setDeletedAt(LocalDateTime.now());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User currentUser) {
                user.setDeletedBy(currentUser.getId());
            }
        }

        repository.save(user);
    }

    @Override
    @Transactional
    public void forceDelete(IdRequest request) {
        if (!repository.existsById(request.getId())) {
            throw new UserNotFoundException(request.getId());
        }
        repository.deleteById(request.getId());
    }

}
