package com.example.stockpos.app.service;

import com.example.stockpos.app.dto.requests.AuthRequest;
import com.example.stockpos.app.dto.requests.UserRequest;
import com.example.stockpos.app.dto.responses.AuthResponse;

public interface AuthService {
    AuthResponse register(UserRequest.CreateUserRequest request);
    AuthResponse login (AuthRequest request);
}
