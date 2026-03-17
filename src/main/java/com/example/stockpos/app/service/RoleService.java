package com.example.stockpos.app.service;

import com.example.stockpos.app.dto.requests.PaginationRequest;
import com.example.stockpos.app.dto.requests.RoleRequest;
import com.example.stockpos.app.dto.responses.PaginationResponse;
import com.example.stockpos.app.dto.responses.RoleResponse;
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
