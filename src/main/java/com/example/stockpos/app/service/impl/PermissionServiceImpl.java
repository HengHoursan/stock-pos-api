package com.example.stockpos.app.service.impl;

import com.example.stockpos.app.dto.requests.PaginationRequest;
import com.example.stockpos.app.dto.requests.PermissionRequest;
import com.example.stockpos.app.dto.responses.PaginationMeta;
import com.example.stockpos.app.dto.responses.PaginationResponse;
import com.example.stockpos.app.dto.responses.PermissionResponse;
import com.example.stockpos.app.exception.PermissionNotFoundException;
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
    public List<PermissionResponse> findAll() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaginationResponse<PermissionResponse> findAllWithPagination(PaginationRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getLimit(),
                request.getOrderBy() != null && !request.getOrderBy().isEmpty() ? Sort.by(request.getOrderBy()) : Sort.unsorted()
        );

        Specification<Permission> spec = (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (request.getSearch() != null && !request.getSearch().isEmpty()) {
                String searchPattern = "%" + request.getSearch().toLowerCase() + "%";
                predicates = cb.and(predicates, cb.or(
                        cb.like(cb.lower(root.get("name")), searchPattern),
                        cb.like(cb.lower(root.get("displayName")), searchPattern),
                        cb.like(cb.lower(root.get("group")), searchPattern)
                ));
            }

            return predicates;
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

        return PaginationResponse.<PermissionResponse>builder()
                .data(content)
                .meta(meta)
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
        
        permission.setName(request.getName());
        permission.setDisplayName(request.getDisplayName());
        permission.setGroup(request.getGroup());
        permission.setSort(request.getSort());
        
        return mapToResponse(repository.save(permission));
    }

    @Override
    public void delete(Integer id) {
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
