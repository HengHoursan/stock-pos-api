package com.example.stockpos.app.controller;

import com.example.stockpos.app.dto.common.request.IdRequest;
import com.example.stockpos.app.dto.common.request.PaginationRequest;
import com.example.stockpos.app.dto.role.request.RolePermissionRequest;
import com.example.stockpos.app.dto.role.request.RoleRequest;
import com.example.stockpos.app.dto.common.response.ApiResponse;
import com.example.stockpos.app.dto.common.response.PaginationResponse;
import com.example.stockpos.app.dto.role.response.RoleResponse;
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

    @PostMapping("/detail")
    public ResponseEntity<ApiResponse<RoleResponse>> getById(@Valid @RequestBody IdRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Role fetched successfully", service.findById(request.getId())));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> create(@Valid @RequestBody RoleRequest.CreateRoleRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Role created successfully", service.create(request)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<RoleResponse>> update(@Valid @RequestBody RoleRequest.UpdateRoleRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Role updated successfully", service.update(request.getId(), request)));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> delete(@Valid @RequestBody IdRequest request) {
        service.delete(request.getId());
        return ResponseEntity.ok(ApiResponse.success("Role deleted successfully", null));
    }

    @PostMapping("/permissions")
    public ResponseEntity<ApiResponse<RoleResponse>> assignPermissions(@Valid @RequestBody RolePermissionRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Permissions assigned successfully", service.assignPermissions(request.getRoleId(), request.getPermissionIds())));
    }
}
