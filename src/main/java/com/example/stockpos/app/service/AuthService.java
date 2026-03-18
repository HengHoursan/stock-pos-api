package com.example.stockpos.app.service;

import com.example.stockpos.app.dto.auth.request.AuthRequest;
import com.example.stockpos.app.dto.user.request.UserRequest;
import com.example.stockpos.app.dto.auth.response.AuthResponse;

public interface AuthService {
    AuthResponse register(UserRequest.CreateUserRequest request);
    AuthResponse login (AuthRequest request);
    void logout(String token);
}
