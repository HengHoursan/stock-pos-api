package com.example.stockpos.app.service;

import com.example.stockpos.app.dto.common.request.PaginationRequest;
import com.example.stockpos.app.dto.role.request.RoleRequest;
import com.example.stockpos.app.dto.common.response.PaginationResponse;
import com.example.stockpos.app.dto.role.response.RoleResponse;
import java.util.List;

public interface RoleService {
    List<RoleResponse> findAll();
    PaginationResponse<RoleResponse> findAllWithPagination(PaginationRequest request);
    RoleResponse findById(Integer id);
    RoleResponse create(RoleRequest.CreateRoleRequest request);
    RoleResponse update(Integer id, RoleRequest.UpdateRoleRequest request);
    void delete(Integer id);
    RoleResponse assignPermissions(Integer roleId, List<Integer> permissionIds);
}
