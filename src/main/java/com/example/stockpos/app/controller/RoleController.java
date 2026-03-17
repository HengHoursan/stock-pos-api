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
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService service;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Roles fetched successfully", service.findAll()));
    }

    @PostMapping("/list")
    public ResponseEntity<ApiResponse<PaginationResponse<RoleResponse>>> getAllPaginated(
            @RequestBody PaginationRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Roles fetched successfully", service.findAllWithPagination(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success("Role fetched successfully", service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> create(@Valid @RequestBody RoleRequest.CreateRoleRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Role created successfully", service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> update(
            @PathVariable Integer id,
            @Valid @RequestBody RoleRequest.UpdateRoleRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Role updated successfully", service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Role deleted successfully", null));
    }

    @PostMapping("/{id}/permissions")
    public ResponseEntity<ApiResponse<RoleResponse>> assignPermissions(
            @PathVariable Integer id,
            @RequestBody List<Integer> permissionIds
    ) {
        return ResponseEntity.ok(ApiResponse.success("Permissions assigned successfully", service.assignPermissions(id, permissionIds)));
    }
}
