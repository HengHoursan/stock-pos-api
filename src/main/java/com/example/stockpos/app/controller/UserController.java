
package com.example.stockpos.app.controller;

import com.example.stockpos.app.dto.requests.PaginationRequest;
import com.example.stockpos.app.dto.requests.UserRequest;
import com.example.stockpos.app.dto.responses.ApiResponse;
import com.example.stockpos.app.dto.responses.PaginationResponse;
import com.example.stockpos.app.dto.responses.UserResponse;
import com.example.stockpos.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService UserService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAll() {
        return ResponseEntity.ok(UserService.findAll());
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<PaginationResponse<UserResponse>>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) Integer roleId,
            @RequestParam(required = false) String orderBy
    ) {
        Map<String, String> filters = new HashMap<>();
        if (status != null) filters.put("status", status.toString());
        if (roleId != null) filters.put("roleId", roleId.toString());

        PaginationRequest request = PaginationRequest.builder()
                .page(page)
                .limit(limit)
                .search(search)
                .filters(filters)
                .orderBy(orderBy)
                .build();

        return ResponseEntity.ok(UserService.findAllWithPagination(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(UserService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(
            @Valid @RequestBody UserRequest.CreateUserRequest request
    ) {
        return ResponseEntity.ok(UserService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @PathVariable Integer id,
            @Valid @RequestBody UserRequest.UpdateUserRequest request
    ) {
        return ResponseEntity.ok(UserService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(UserService.delete(id));
    }
}
