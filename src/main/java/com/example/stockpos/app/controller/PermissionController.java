package com.example.stockpos.app.controller;

import com.example.stockpos.app.dto.requests.PaginationRequest;
import com.example.stockpos.app.dto.requests.PermissionRequest;
import com.example.stockpos.app.dto.responses.ApiResponse;
import com.example.stockpos.app.dto.responses.PaginationResponse;
import com.example.stockpos.app.dto.responses.PermissionResponse;
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

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Permissions fetched successfully", service.findAll()));
    }

    @PostMapping("/list")
    public ResponseEntity<ApiResponse<PaginationResponse<PermissionResponse>>> getAllPaginated(
            @RequestBody PaginationRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Permissions fetched successfully", service.findAllWithPagination(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success("Permission fetched successfully", service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PermissionResponse>> create(@Valid @RequestBody PermissionRequest.CreatePermissionRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Permission created successfully", service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionResponse>> update(
            @PathVariable Integer id,
            @Valid @RequestBody PermissionRequest.UpdatePermissionRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Permission updated successfully", service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Permission deleted successfully", null));
    }
}
