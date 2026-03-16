package com.example.stockpos.app.controller;

import com.example.stockpos.app.dto.requests.AuthRequest;
import com.example.stockpos.app.dto.requests.UserRequest;
import com.example.stockpos.app.dto.responses.ApiResponse;
import com.example.stockpos.app.dto.responses.AuthResponse;
import com.example.stockpos.app.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final AuthService Authservice;

    @PostMapping("/register")
    @Operation(description = "Create a new user and return access and refresh tokens")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody UserRequest.CreateUserRequest request
    ) {
        return ResponseEntity.ok(Authservice.register(request));
    }

    @PostMapping("/login")
    @Operation(description = "Authenticate user credentials and return access and refresh tokens")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody AuthRequest request
    ) {
        return ResponseEntity.ok(Authservice.login(request));
    }
}
