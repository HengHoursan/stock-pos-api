package com.example.stockpos.app.service.impl;

import com.example.stockpos.app.dto.requests.PaginationRequest;
import com.example.stockpos.app.dto.requests.RoleRequest;
import com.example.stockpos.app.dto.responses.ApiResponse;
import com.example.stockpos.app.dto.responses.PaginationMeta;
import com.example.stockpos.app.dto.responses.PaginationResponse;
import com.example.stockpos.app.dto.responses.RoleResponse;
import com.example.stockpos.app.models.Permission;
import com.example.stockpos.app.models.Role;
import com.example.stockpos.app.repository.PermissionRepository;
import com.example.stockpos.app.repository.RoleRepository;
import com.example.stockpos.app.service.RoleService;
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
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final PermissionRepository permissionRepository;

    @Override
    public ApiResponse<List<RoleResponse>> findAll() {
        List<RoleResponse> roles = repository.findAll().stream()
                .map(RoleResponse::fromEntity)
                .collect(Collectors.toList());
        return ApiResponse.success("Roles fetched successfully", roles);
    }

    @Override
    public ApiResponse<PaginationResponse<RoleResponse>> findAllWithPagination(PaginationRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage(), 
                request.getLimit(), 
                request.getOrderBy() != null && !request.getOrderBy().isEmpty() ? Sort.by(request.getOrderBy()) : Sort.unsorted()
        );

        Specification<Role> spec = (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (request.getSearch() != null && !request.getSearch().isEmpty()) {
                String searchPattern = "%" + request.getSearch().toLowerCase() + "%";
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("displayName")), searchPattern)
                ));
            }

            if (request.getFilters() != null && request.getFilters().containsKey("status")) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("status"), Boolean.parseBoolean(request.getFilters().get("status"))));
            }

            return predicates;
        };

        Page<Role> rolePage = repository.findAll(spec, pageable);
        List<RoleResponse> content = rolePage.getContent().stream()
                .map(RoleResponse::fromEntity)
                .collect(Collectors.toList());

        int from = (int) rolePage.getPageable().getOffset() + 1;
        int to = from + rolePage.getNumberOfElements() - 1;

        PaginationMeta meta = PaginationMeta.builder()
                .total(rolePage.getTotalElements())
                .from(rolePage.getNumberOfElements() > 0 ? from : 0)
                .to(rolePage.getNumberOfElements() > 0 ? to : 0)
                .page(rolePage.getNumber())
                .limit(rolePage.getSize())
                .build();

        return ApiResponse.success("Roles fetched successfully", PaginationResponse.<RoleResponse>builder()
                .data(content)
                .meta(meta)
                .build());
    }

    @Override
    public ApiResponse<RoleResponse> findById(Integer id) {
        RoleResponse response = repository.findById(id)
                .map(RoleResponse::fromEntity)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        return ApiResponse.success("Role fetched successfully", response);
    }

    @Override
    public ApiResponse<RoleResponse> create(RoleRequest.CreateRoleRequest request) {
        Role role = Role.builder()
                .name(request.getName())
                .displayName(request.getDisplayName())
                .status(request.getStatus())
                .build();
        return ApiResponse.success("Role created successfully", RoleResponse.fromEntity(repository.save(role)));
    }

    @Override
    public ApiResponse<RoleResponse> update(Integer id, RoleRequest.UpdateRoleRequest request) {
        Role role = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        
        role.setName(request.getName());
        role.setDisplayName(request.getDisplayName());
        role.setStatus(request.getStatus());
        
        return ApiResponse.success("Role updated successfully", RoleResponse.fromEntity(repository.save(role)));
    }

    @Override
    public ApiResponse<Void> delete(Integer id) {
        repository.deleteById(id);
        return ApiResponse.success("Role deleted successfully", null);
    }

    @Override
    public ApiResponse<RoleResponse> assignPermissions(Integer roleId, List<Integer> permissionIds) {
        Role role = repository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        role.setPermissions(permissions);
        
        return ApiResponse.success("Permissions assigned successfully", RoleResponse.fromEntity(repository.save(role)));
    }

}
