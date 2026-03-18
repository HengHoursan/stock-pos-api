package com.example.stockpos.app.service.impl;

import com.example.stockpos.app.dto.common.request.PaginationRequest;
import com.example.stockpos.app.dto.permission.request.PermissionRequest;
import com.example.stockpos.app.dto.common.response.PaginationMeta;
import com.example.stockpos.app.dto.common.response.PaginationResponse;
import com.example.stockpos.app.dto.permission.response.PermissionResponse;
import com.example.stockpos.app.exception.DuplicateResourceException;
import com.example.stockpos.app.exception.PermissionNotFoundException;
import com.example.stockpos.app.exception.ResourceInUseException;
import com.example.stockpos.app.models.Permission;
import com.example.stockpos.app.repository.PermissionRepository;
import com.example.stockpos.app.repository.RolePermissionRepository;
import com.example.stockpos.app.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository repository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    public List<PermissionResponse> findAll() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaginationResponse<PermissionResponse> findAllWithPagination(PaginationRequest request) {
        // Build filters dynamically
        Specification<Permission> spec = (root, query, cb) -> cb.conjunction();

        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            String keyword = "%" + request.getSearch().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), keyword),
                    cb.like(cb.lower(root.get("displayName")), keyword),
                    cb.like(cb.lower(root.get("group")), keyword)
            ));
        }

        // Fetch and return
        Page<Permission> page = repository.findAll(spec, request.toPageable());
        return PaginationResponse.<PermissionResponse>builder()
                .data(page.getContent().stream().map(this::mapToResponse).collect(Collectors.toList()))
                .meta(PaginationMeta.fromPage(page))
                .build();
    }

    @Override
    public PermissionResponse findById(Integer id) {
        return repository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new PermissionNotFoundException(id));
    }

    @Override
    public PermissionResponse create(PermissionRequest.CreatePermissionRequest request) {
        if (repository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Permission with name " + request.getName() + " already exists");
        }

        Permission permission = Permission.builder()
                .name(request.getName())
                .displayName(request.getDisplayName())
                .group(request.getGroup())
                .sort(request.getSort())
                .build();
        return mapToResponse(repository.save(permission));
    }

    @Override
    public PermissionResponse update(Integer id, PermissionRequest.UpdatePermissionRequest request) {
        Permission permission = repository.findById(id)
                .orElseThrow(() -> new PermissionNotFoundException(id));
        
        // Check if new name is already taken by another permission
        repository.findByName(request.getName()).ifPresent(existingPermission -> {
            if (!existingPermission.getId().equals(id)) {
                throw new DuplicateResourceException("Permission with name " + request.getName() + " already exists");
            }
        });

        permission.setName(request.getName());
        permission.setDisplayName(request.getDisplayName());
        permission.setGroup(request.getGroup());
        permission.setSort(request.getSort());
        
        return mapToResponse(repository.save(permission));
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new PermissionNotFoundException(id);
        }

        if (rolePermissionRepository.existsByPermission_Id(id)) {
            throw new ResourceInUseException("Cannot delete permission because it is currently assigned to one or more roles");
        }

        repository.deleteById(id);
    }

    private PermissionResponse mapToResponse(Permission permission) {
        return PermissionResponse.builder()
                .id(permission.getId())
                .name(permission.getName())
                .displayName(permission.getDisplayName())
                .group(permission.getGroup())
                .sort(permission.getSort())
                .build();
    }
}
