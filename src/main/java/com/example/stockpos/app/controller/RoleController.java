package com.example.stockpos.app.controller;

import com.example.stockpos.app.dto.requests.PaginationRequest;
import com.example.stockpos.app.dto.requests.RoleRequest;
import com.example.stockpos.app.dto.responses.ApiResponse;
import com.example.stockpos.app.dto.responses.PaginationResponse;
import com.example.stockpos.app.dto.responses.RoleResponse;
import com.example.stockpos.app.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService service;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<PaginationResponse<RoleResponse>>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) String orderBy
    ) {
        Map<String, String> filters = new HashMap<>();
        if (status != null) filters.put("status", status.toString());

        PaginationRequest request = PaginationRequest.builder()
                .page(page)
                .limit(limit)
                .search(search)
                .filters(filters)
                .orderBy(orderBy)
                .build();

        return ResponseEntity.ok(service.findAllWithPagination(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> create(
            @Valid @RequestBody RoleRequest.CreateRoleRequest request
    ) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> update(
            @PathVariable Integer id,
            @Valid @RequestBody RoleRequest.UpdateRoleRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @PostMapping("/{id}/permissions")
    public ResponseEntity<ApiResponse<RoleResponse>> assignPermissions(
            @PathVariable Integer id,
            @RequestBody List<Integer> permissionIds
    ) {
        return ResponseEntity.ok(service.assignPermissions(id, permissionIds));
    }
}
