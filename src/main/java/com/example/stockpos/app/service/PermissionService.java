package com.example.stockpos.app.service;

import com.example.stockpos.app.dto.requests.PaginationRequest;
import com.example.stockpos.app.dto.requests.PermissionRequest;
import com.example.stockpos.app.dto.responses.ApiResponse;
import com.example.stockpos.app.dto.responses.PaginationResponse;
import com.example.stockpos.app.dto.responses.PermissionResponse;

import java.util.List;

public interface PermissionService {
    ApiResponse<List<PermissionResponse>> findAll();
    ApiResponse<PaginationResponse<PermissionResponse>> findAllWithPagination(PaginationRequest request);
    ApiResponse<PermissionResponse> findById(Integer id);
    ApiResponse<PermissionResponse> create(PermissionRequest.CreatePermissionRequest request);
    ApiResponse<PermissionResponse> update(Integer id, PermissionRequest.UpdatePermissionRequest request);
    ApiResponse<Void> delete(Integer id);
}
