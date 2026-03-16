package com.example.stockpos.app.service;

import com.example.stockpos.app.dto.requests.PaginationRequest;
import com.example.stockpos.app.dto.requests.RoleRequest;
import com.example.stockpos.app.dto.responses.ApiResponse;
import com.example.stockpos.app.dto.responses.PaginationResponse;
import com.example.stockpos.app.dto.responses.RoleResponse;
import java.util.List;

public interface RoleService {
    ApiResponse<List<RoleResponse>> findAll();
    ApiResponse<PaginationResponse<RoleResponse>> findAllWithPagination(PaginationRequest request);
    ApiResponse<RoleResponse> findById(Integer id);
    ApiResponse<RoleResponse> create(RoleRequest.CreateRoleRequest request);
    ApiResponse<RoleResponse> update(Integer id, RoleRequest.UpdateRoleRequest request);
    ApiResponse<Void> delete(Integer id);
    ApiResponse<RoleResponse> assignPermissions(Integer roleId, List<Integer> permissionIds);
}
