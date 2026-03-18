package com.example.stockpos.app.controller;

import com.example.stockpos.app.dto.auth.request.AuthRequest;
import com.example.stockpos.app.dto.user.request.UserRequest;
import com.example.stockpos.app.dto.common.response.ApiResponse;
import com.example.stockpos.app.dto.auth.response.AuthResponse;
import com.example.stockpos.app.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
@Tag(name = "Authentications")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(description = "Create a new user and return access and refresh tokens")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody UserRequest.CreateUserRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", authService.register(request)));
    }

    @PostMapping("/login")
    @Operation(description = "Authenticate user credentials and return access and refresh tokens")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody AuthRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("User login successfully", authService.login(request)));
    }

    @PostMapping("/logout")
    @Operation(description = "Logout user (invalidates the token immediately on the server)")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            authService.logout(jwt);
        }
        return ResponseEntity.ok(ApiResponse.success("User logged out successfully.", null));
    }
}
