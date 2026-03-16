package com.example.stockpos.app.service.impl;

import com.example.stockpos.app.config.jwt.JWTService;
import com.example.stockpos.app.dto.requests.AuthRequest;
import com.example.stockpos.app.dto.requests.UserRequest;
import com.example.stockpos.app.dto.responses.ApiResponse;
import com.example.stockpos.app.dto.responses.AuthResponse;
import com.example.stockpos.app.dto.responses.UserResponse;
import com.example.stockpos.app.models.User;
import com.example.stockpos.app.repository.UserRepository;
import com.example.stockpos.app.repository.RoleRepository;
import com.example.stockpos.app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public ApiResponse<AuthResponse> register(UserRequest.CreateUserRequest request) {
        var role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(request.getStatus())
                .role(role)
                .build();
        
        var savedUser = repository.save(user);
        var accessToken = jwtService.generateAccessToken(savedUser);
        var refreshToken = jwtService.generateRefreshToken(savedUser);
        
        // Using a static factory method 'of' or a Builder
        return ApiResponse.success("User registered successfully", AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(UserResponse.fromEntity(savedUser))
                .build());
    }

    @Override
    public ApiResponse<AuthResponse> login(AuthRequest request) {
        // 1. Authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Find user from DB
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Generate tokens
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        
        // 4. Return the DTO
        return ApiResponse.success("User login successfully", AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(UserResponse.fromEntity(user))
                .build());
    }
}