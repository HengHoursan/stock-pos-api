package com.example.stockpos.app.controller;

import com.example.stockpos.app.dto.common.request.IdRequest;
import com.example.stockpos.app.dto.common.request.PaginationRequest;
import com.example.stockpos.app.dto.permission.request.PermissionRequest;
import com.example.stockpos.app.dto.common.response.ApiResponse;
import com.example.stockpos.app.dto.common.response.PaginationResponse;
import com.example.stockpos.app.dto.permission.response.PermissionResponse;
import com.example.stockpos.app.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
@Tag(name = "Permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService service;

    @PostMapping("/all")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Permissions fetched successfully", service.findAll()));
    }

    @PostMapping("/list")
    public ResponseEntity<ApiResponse<PaginationResponse<PermissionResponse>>> getAllPaginated(
            @RequestBody PaginationRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Permissions fetched successfully", service.findAllWithPagination(request)));
    }

    @PostMapping("/detail")
    public ResponseEntity<ApiResponse<PermissionResponse>> getById(@Valid @RequestBody IdRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Permission fetched successfully", service.findById(request.getId())));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PermissionResponse>> create(@Valid @RequestBody PermissionRequest.CreatePermissionRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Permission created successfully", service.create(request)));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<PermissionResponse>> update(@Valid @RequestBody PermissionRequest.UpdatePermissionRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Permission updated successfully", service.update(request.getId(), request)));
    }

    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> delete(@Valid @RequestBody IdRequest request) {
        service.delete(request.getId());
        return ResponseEntity.ok(ApiResponse.success("Permission deleted successfully", null));
    }
}
