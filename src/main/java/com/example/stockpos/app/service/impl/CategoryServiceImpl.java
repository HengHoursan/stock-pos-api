package com.example.stockpos.app.service.impl;

import com.example.stockpos.app.dto.category.request.CreateCategoryRequest;
import com.example.stockpos.app.dto.category.request.UpdateCategoryRequest;
import com.example.stockpos.app.dto.category.request.UpdateCategoryStatusRequest;
import com.example.stockpos.app.dto.category.response.CategoryResponse;
import com.example.stockpos.app.dto.common.request.IdRequest;
import com.example.stockpos.app.dto.common.request.PaginationRequest;
import com.example.stockpos.app.dto.common.response.PaginationMeta;
import com.example.stockpos.app.dto.common.response.PaginationResponse;
import com.example.stockpos.app.repository.CategoryRepository;
import com.example.stockpos.app.service.CategoryService;
import com.example.stockpos.app.models.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaginationResponse<CategoryResponse> findAllWithPagination(PaginationRequest request) {
        Specification<Category> specification = (root, query, cb) -> cb.conjunction();

        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            String keyword = "%" + request.getSearch().toLowerCase() + "%";
            specification = specification.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), keyword),
                    cb.like(cb.lower(root.get("description")), keyword)
            ));
        }

        if (request.getFilters() != null && request.getFilters().containsKey("status")) {
            boolean isStatus = "true".equals(request.getFilters().get("status"));
            specification = specification.and((root, query, cb) ->
                    cb.equal(root.get("status"), isStatus)
            );
        }

        Page<Category> page = categoryRepository.findAll(specification, request.toPageable());
        return PaginationResponse.<CategoryResponse>builder()
                .data(page.getContent().stream().map(this::mapToResponse).collect(Collectors.toList()))
                .meta(PaginationMeta.fromPage(page))
                .build();
    }

    @Override
    public CategoryResponse findById(Integer id) {
        return categoryRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    @Override
    @Transactional
    public CategoryResponse create(CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Category name already exists");
        }
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .parentId(request.getParentId())
                .status(true)
                .build();
        return mapToResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryResponse update(UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getId()));

        if (request.getName() != null && !request.getName().equals(category.getName())) {
            if (categoryRepository.existsByName(request.getName())) {
                throw new RuntimeException("Category name already exists");
            }
            category.setName(request.getName());
        }

        if (request.getDescription() != null) category.setDescription(request.getDescription());
        if (request.getImageUrl() != null) category.setImageUrl(request.getImageUrl());
        if (request.getParentId() != null) category.setParentId(request.getParentId());

        return mapToResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void updateStatus(UpdateCategoryStatusRequest request) {
        Category category = categoryRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getId()));
        category.setStatus(request.getStatus());
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void delete(IdRequest request) {
        if (!categoryRepository.existsById(request.getId())) {
            throw new RuntimeException("Category not found with id: " + request.getId());
        }
        categoryRepository.deleteById(request.getId());
    }

    // Helper method to convert Category entity to CategoryResponse DTO
    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .parentId(category.getParentId())
                .status(category.getStatus())
                .build();
    }
}
