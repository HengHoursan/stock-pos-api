package com.example.stockpos.app.service;

import com.example.stockpos.app.dto.category.response.CategoryResponse;
import com.example.stockpos.app.dto.common.request.PaginationRequest;
import com.example.stockpos.app.dto.category.request.*;
import com.example.stockpos.app.dto.common.request.IdRequest;
import com.example.stockpos.app.dto.common.response.PaginationResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse>findAll();
    PaginationResponse<CategoryResponse> findAllWithPagination(PaginationRequest request);
    CategoryResponse findById(Integer id);
    CategoryResponse create(CreateCategoryRequest request);
    CategoryResponse update(UpdateCategoryRequest request);
    void updateStatus(UpdateCategoryStatusRequest request);
    void delete(IdRequest request);
}
