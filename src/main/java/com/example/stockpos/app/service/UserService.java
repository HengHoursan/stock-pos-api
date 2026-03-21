package com.example.stockpos.app.service;

import com.example.stockpos.app.dto.common.request.IdRequest;
import com.example.stockpos.app.dto.common.request.PaginationRequest;
import com.example.stockpos.app.dto.user.request.UserRequest;
import com.example.stockpos.app.dto.common.response.PaginationResponse;
import com.example.stockpos.app.dto.user.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> findAll();
    PaginationResponse<UserResponse> findAllWithPagination(PaginationRequest request);
    UserResponse findById(Integer id);
    UserResponse create(UserRequest.CreateUserRequest request);
    UserResponse update(Integer id, UserRequest.UpdateUserRequest request);
    void softDelete(IdRequest request);
    void forceDelete(IdRequest request);
}
