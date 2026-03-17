package com.example.stockpos.app.service;

import com.example.stockpos.app.dto.requests.PaginationRequest;
import com.example.stockpos.app.dto.requests.PermissionRequest;
import com.example.stockpos.app.dto.responses.PaginationResponse;
import com.example.stockpos.app.dto.responses.PermissionResponse;

import java.util.List;

public interface PermissionService {
    List<PermissionResponse> findAll();
    PaginationResponse<PermissionResponse> findAllWithPagination(PaginationRequest request);
    PermissionResponse findById(Integer id);
    PermissionResponse create(PermissionRequest.CreatePermissionRequest request);
    PermissionResponse update(Integer id, PermissionRequest.UpdatePermissionRequest request);
    void delete(Integer id);
}
