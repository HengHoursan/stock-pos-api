package com.example.stockpos.app.service;

import com.example.stockpos.app.dto.requests.AuthRequest;
import com.example.stockpos.app.dto.requests.UserRequest;
import com.example.stockpos.app.dto.responses.ApiResponse;
import com.example.stockpos.app.dto.responses.AuthResponse;

public interface AuthService {
    ApiResponse<AuthResponse> register(UserRequest.CreateUserRequest request);
    ApiResponse<AuthResponse> login (AuthRequest request);
}
