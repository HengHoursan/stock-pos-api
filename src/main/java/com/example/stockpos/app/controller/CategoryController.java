package com.example.stockpos.app.controller;

import com.example.stockpos.app.dto.category.request.CreateCategoryRequest;
import com.example.stockpos.app.dto.category.request.UpdateCategoryRequest;
import com.example.stockpos.app.dto.category.request.UpdateCategoryStatusRequest;
import com.example.stockpos.app.dto.category.response.CategoryResponse;
import com.example.stockpos.app.dto.common.request.IdRequest;
import com.example.stockpos.app.dto.common.request.PaginationRequest;
import com.example.stockpos.app.dto.common.response.ApiResponse;
import com.example.stockpos.app.dto.common.response.PaginationResponse;
import com.example.stockpos.app.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/all")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Categories fetched successfully", categoryService.findAll()));
    }

    @PostMapping("/list")
    public ResponseEntity<ApiResponse<PaginationResponse<CategoryResponse>>> getAllPaginated(
            @RequestBody PaginationRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Categories fetched successfully", categoryService.findAllWithPagination(request)));
    }

    @PostMapping("/detail")
    public ResponseEntity<ApiResponse<CategoryResponse>> getById(@Valid @RequestBody IdRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Category fetched successfully", categoryService.findById(request.getId())));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CreateCategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Category created successfully", categoryService.create(request)));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(@Valid @RequestBody UpdateCategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully", categoryService.update(request)));
    }

    @PostMapping("/update-status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(@Valid @RequestBody UpdateCategoryStatusRequest request) {
        categoryService.updateStatus(request);
        return ResponseEntity.ok(ApiResponse.success("Category status updated successfully", null));
    }

    @PostMapping("/soft-delete")
    public ResponseEntity<ApiResponse<Void>> softDelete(@Valid @RequestBody IdRequest request) {
        categoryService.softDelete(request);
        return ResponseEntity.ok(ApiResponse.success("Category soft-deleted successfully", null));
    }

    @PostMapping("/force-delete")
    public ResponseEntity<ApiResponse<Void>> forceDelete(@Valid @RequestBody IdRequest request) {
        categoryService.forceDelete(request);
        return ResponseEntity.ok(ApiResponse.success("Category force-deleted successfully", null));
    }
}
