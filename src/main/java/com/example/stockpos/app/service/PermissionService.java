package com.example.stockpos.app.service;

import com.example.stockpos.app.dto.common.request.PaginationRequest;
import com.example.stockpos.app.dto.permission.request.PermissionRequest;
import com.example.stockpos.app.dto.common.response.PaginationResponse;
import com.example.stockpos.app.dto.permission.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    List<PermissionResponse> findAll();
    PaginationResponse<PermissionResponse> findAllWithPagination(PaginationRequest request);
    PermissionResponse findById(Integer id);
    PermissionResponse create(PermissionRequest.CreatePermissionRequest request);
    PermissionResponse update(Integer id, PermissionRequest.UpdatePermissionRequest request);
    void delete(Integer id);
}
