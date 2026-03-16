package com.example.stockpos.app.service;

import com.example.stockpos.app.dto.requests.PaginationRequest;
import com.example.stockpos.app.dto.requests.UserRequest;
import com.example.stockpos.app.dto.responses.ApiResponse;
import com.example.stockpos.app.dto.responses.PaginationResponse;
import com.example.stockpos.app.dto.responses.UserResponse;

import java.util.List;

public interface UserService {
    ApiResponse<List<UserResponse>> findAll();
    ApiResponse<PaginationResponse<UserResponse>> findAllWithPagination(PaginationRequest request);
    ApiResponse<UserResponse> findById(Integer id);
    ApiResponse<UserResponse> create(UserRequest.CreateUserRequest request);
    ApiResponse<UserResponse> update(Integer id, UserRequest.UpdateUserRequest request);
    ApiResponse<Void> delete(Integer id);
}
