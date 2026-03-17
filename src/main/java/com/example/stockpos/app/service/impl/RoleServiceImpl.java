package com.example.stockpos.app.service.impl;

import com.example.stockpos.app.dto.requests.PaginationRequest;
import com.example.stockpos.app.dto.requests.RoleRequest;
import com.example.stockpos.app.dto.responses.PaginationMeta;
import com.example.stockpos.app.dto.responses.PaginationResponse;
import com.example.stockpos.app.dto.responses.RoleResponse;
import com.example.stockpos.app.exception.RoleNotFoundException;
import com.example.stockpos.app.models.Permission;
import com.example.stockpos.app.models.Role;
import com.example.stockpos.app.models.RolePermission;
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
    public List<RoleResponse> findAll() {
        return repository.findAll().stream()
                .map(RoleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public PaginationResponse<RoleResponse> findAllWithPagination(PaginationRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getLimit(),
                request.getOrderBy() != null && !request.getOrderBy().isEmpty() ? Sort.by(request.getOrderBy()) : Sort.unsorted()
        );

        Specification<Role> spec = (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (request.getSearch() != null && !request.getSearch().isEmpty()) {
                String searchPattern = "%" + request.getSearch().toLowerCase() + "%";
                predicates = cb.and(predicates, cb.or(
                        cb.like(cb.lower(root.get("name")), searchPattern),
                        cb.like(cb.lower(root.get("displayName")), searchPattern)
                ));
            }

            if (request.getFilters() != null && request.getFilters().containsKey("status")) {
                predicates = cb.and(predicates, cb.equal(root.get("status"), Boolean.parseBoolean(request.getFilters().get("status"))));
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

        return PaginationResponse.<RoleResponse>builder()
                .data(content)
                .meta(meta)
                .build();
    }

    @Override
    public RoleResponse findById(Integer id) {
        return repository.findById(id)
                .map(RoleResponse::fromEntity)
                .orElseThrow(() -> new RoleNotFoundException(id));
    }

    @Override
    public RoleResponse create(RoleRequest.CreateRoleRequest request) {
        Role role = Role.builder()
                .name(request.getName())
                .displayName(request.getDisplayName())
                .status(request.getStatus())
                .build();
        return RoleResponse.fromEntity(repository.save(role));
    }

    @Override
    public RoleResponse update(Integer id, RoleRequest.UpdateRoleRequest request) {
        Role role = repository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
        
        role.setName(request.getName());
        role.setDisplayName(request.getDisplayName());
        role.setStatus(request.getStatus());
        
        return RoleResponse.fromEntity(repository.save(role));
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public RoleResponse assignPermissions(Integer roleId, List<Integer> permissionIds) {
        Role role = repository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(roleId));
        
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        
        // Clear existing permissions
        role.getRolePermissions().clear();
        
        // Add new permissions
        List<RolePermission> rolePermissions = permissions.stream()
                .map(permission -> RolePermission.builder()
                        .role(role)
                        .permission(permission)
                        .build())
                .collect(Collectors.toList());
        
        role.getRolePermissions().addAll(rolePermissions);
        
        return RoleResponse.fromEntity(repository.save(role));
    }

}
