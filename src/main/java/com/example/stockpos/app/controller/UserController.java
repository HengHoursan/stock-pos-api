
package com.example.stockpos.app.controller;

import com.example.stockpos.app.dto.common.request.IdRequest;
import com.example.stockpos.app.dto.common.request.PaginationRequest;
import com.example.stockpos.app.dto.user.request.UserRequest;
import com.example.stockpos.app.dto.common.response.ApiResponse;
import com.example.stockpos.app.dto.common.response.PaginationResponse;
import com.example.stockpos.app.dto.user.response.UserResponse;
import com.example.stockpos.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/all")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Users fetched successfully", userService.findAll()));
    }

    @PostMapping("/list")
    public ResponseEntity<ApiResponse<PaginationResponse<UserResponse>>> getAllPaginated(
            @RequestBody PaginationRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Users fetched successfully", userService.findAllWithPagination(request)));
    }

    @PostMapping("/detail")
    public ResponseEntity<ApiResponse<UserResponse>> getById(@Valid @RequestBody IdRequest request) {
        return ResponseEntity.ok(ApiResponse.success("User fetched successfully", userService.findById(request.getId())));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserResponse>> create(@Valid @RequestBody UserRequest.CreateUserRequest request) {
        return ResponseEntity.ok(ApiResponse.success("User created successfully", userService.create(request)));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<UserResponse>> update(@Valid @RequestBody UserRequest.UpdateUserRequest request) {
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", userService.update(request.getId(), request)));
    }

    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> delete(@Valid @RequestBody IdRequest request) {
        userService.delete(request.getId());
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }
}
