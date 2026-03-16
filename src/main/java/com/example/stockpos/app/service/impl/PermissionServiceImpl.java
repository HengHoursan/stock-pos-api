package com.example.stockpos.app.service.impl;

import com.example.stockpos.app.dto.requests.PaginationRequest;
import com.example.stockpos.app.dto.requests.PermissionRequest;
import com.example.stockpos.app.dto.responses.ApiResponse;
import com.example.stockpos.app.dto.responses.PaginationMeta;
import com.example.stockpos.app.dto.responses.PaginationResponse;
import com.example.stockpos.app.dto.responses.PermissionResponse;
import com.example.stockpos.app.models.Permission;
import com.example.stockpos.app.repository.PermissionRepository;
import com.example.stockpos.app.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository repository;

    @Override
    public ApiResponse<List<PermissionResponse>> findAll() {
        List<PermissionResponse> permissions = repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ApiResponse.success("Permissions fetched successfully", permissions);
    }

    @Override
    public ApiResponse<PaginationResponse<PermissionResponse>> findAllWithPagination(PaginationRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage(), 
                request.getLimit(), 
                request.getOrderBy() != null && !request.getOrderBy().isEmpty() ? Sort.by(request.getOrderBy()) : Sort.unsorted()
        );

        Specification<Permission> spec = (root, query, criteriaBuilder) -> {
            if (request.getSearch() == null || request.getSearch().isEmpty()) {
                return null;
            }
            String searchPattern = "%" + request.getSearch().toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("displayName")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("group")), searchPattern)
            );
        };

        Page<Permission> permissionPage = repository.findAll(spec, pageable);
        List<PermissionResponse> content = permissionPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        int from = (int) permissionPage.getPageable().getOffset() + 1;
        int to = from + permissionPage.getNumberOfElements() - 1;

        PaginationMeta meta = PaginationMeta.builder()
                .total(permissionPage.getTotalElements())
                .from(permissionPage.getNumberOfElements() > 0 ? from : 0)
                .to(permissionPage.getNumberOfElements() > 0 ? to : 0)
                .page(permissionPage.getNumber())
                .limit(permissionPage.getSize())
                .build();

        return ApiResponse.success("Permissions fetched successfully", PaginationResponse.<PermissionResponse>builder()
                .data(content)
                .meta(meta)
                .build());
    }

    @Override
    public ApiResponse<PermissionResponse> findById(Integer id) {
        PermissionResponse response = repository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        return ApiResponse.success("Permission fetched successfully", response);
    }

    @Override
    public ApiResponse<PermissionResponse> create(PermissionRequest.CreatePermissionRequest request) {
        Permission permission = Permission.builder()
                .name(request.getName())
                .displayName(request.getDisplayName())
                .group(request.getGroup())
                .sort(request.getSort())
                .build();
        return ApiResponse.success("Permission created successfully", mapToResponse(repository.save(permission)));
    }

    @Override
    public ApiResponse<PermissionResponse> update(Integer id, PermissionRequest.UpdatePermissionRequest request) {
        Permission permission = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        
        permission.setName(request.getName());
        permission.setDisplayName(request.getDisplayName());
        permission.setGroup(request.getGroup());
        permission.setSort(request.getSort());
        
        return ApiResponse.success("Permission updated successfully", mapToResponse(repository.save(permission)));
    }

    @Override
    public ApiResponse<Void> delete(Integer id) {
        repository.deleteById(id);
        return ApiResponse.success("Permission deleted successfully", null);
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
