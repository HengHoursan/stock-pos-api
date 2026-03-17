package com.example.stockpos.app.service;

import com.example.stockpos.app.dto.requests.PaginationRequest;
import com.example.stockpos.app.dto.requests.UserRequest;
import com.example.stockpos.app.dto.responses.PaginationResponse;
import com.example.stockpos.app.dto.responses.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> findAll();
    PaginationResponse<UserResponse> findAllWithPagination(PaginationRequest request);
    UserResponse findById(Integer id);
    UserResponse create(UserRequest.CreateUserRequest request);
    UserResponse update(Integer id, UserRequest.UpdateUserRequest request);
    void delete(Integer id);
}
