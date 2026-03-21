package com.example.stockpos.app.service.impl;

import com.example.stockpos.app.dto.common.request.IdRequest;
import com.example.stockpos.app.dto.common.request.PaginationRequest;
import com.example.stockpos.app.dto.category.request.CreateCategoryRequest;
import com.example.stockpos.app.dto.category.request.UpdateCategoryRequest;
import com.example.stockpos.app.dto.category.request.UpdateCategoryStatusRequest;
import com.example.stockpos.app.dto.common.response.PaginationMeta;
import com.example.stockpos.app.dto.common.response.PaginationResponse;
import com.example.stockpos.app.dto.category.response.CategoryResponse;
import com.example.stockpos.app.exception.common.DuplicateResourceException;
import com.example.stockpos.app.exception.category.CategoryNotFoundException;
import com.example.stockpos.app.models.Category;
import com.example.stockpos.app.repository.CategoryRepository;
import com.example.stockpos.app.service.CategoryService;
import com.example.stockpos.app.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public PaginationResponse<CategoryResponse> findAllWithPagination(PaginationRequest request) {
        Specification<Category> spec = (root, query, cb) -> cb.conjunction();

        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            String keyword = "%" + request.getSearch().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), keyword),
                    cb.like(cb.lower(root.get("slug")), keyword),
                    cb.like(cb.lower(root.get("code")), keyword),
                    cb.like(cb.lower(root.get("description")), keyword)
            ));
        }

        if (request.getFilters() != null) {
            if (request.getFilters().containsKey("status")) {
                boolean isActive = Boolean.parseBoolean(request.getFilters().get("status"));
                spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), isActive));
            }
            if (request.getFilters().containsKey("parentId")) {
                int parentId = Integer.parseInt(request.getFilters().get("parentId"));
                spec = spec.and((root, query, cb) -> cb.equal(root.get("parentId"), parentId));
            }
        }

        Page<Category> page = categoryRepository.findAll(spec, request.toPageable());
        return PaginationResponse.<CategoryResponse>builder()
                .data(page.getContent().stream().map(CategoryResponse::fromEntity).collect(Collectors.toList()))
                .meta(PaginationMeta.fromPage(page))
                .build();
    }

    @Override
    public CategoryResponse findById(Integer id) {
        return categoryRepository.findById(id)
                .map(CategoryResponse::fromEntity)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    @Transactional
    public CategoryResponse create(CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category with name " + request.getName() + " already exists");
        }
        if (categoryRepository.existsBySlug(request.getSlug())) {
            throw new DuplicateResourceException("Category with slug " + request.getSlug() + " already exists");
        }

        String code = request.getCode();
        if (code == null || code.isEmpty()) {
            code = Helper.generateCode("CAT-");
        }
        
        if (categoryRepository.existsByCode(code)) {
            throw new DuplicateResourceException("Category with code " + code + " already exists");
        }

        Category category = Category.builder()
                .code(code)
                .name(request.getName())
                .slug(request.getSlug())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .parentId(request.getParentId())
                .status(true)
                .build();

        return CategoryResponse.fromEntity(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryResponse update(UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(request.getId())
                .orElseThrow(() -> new CategoryNotFoundException(request.getId()));

        if (request.getName() != null && !request.getName().equals(category.getName())) {
            if (categoryRepository.existsByName(request.getName())) {
                throw new DuplicateResourceException("Category with name " + request.getName() + " already exists");
            }
            category.setName(request.getName());
        }

        if (request.getSlug() != null && !request.getSlug().equals(category.getSlug())) {
            if (categoryRepository.existsBySlug(request.getSlug())) {
                throw new DuplicateResourceException("Category with slug " + request.getSlug() + " already exists");
            }
            category.setSlug(request.getSlug());
        }

        if (request.getCode() != null && !request.getCode().equals(category.getCode())) {
            if (categoryRepository.existsByCode(request.getCode())) {
                throw new DuplicateResourceException("Category with code " + request.getCode() + " already exists");
            }
            category.setCode(request.getCode());
        }

        if (request.getDescription() != null) category.setDescription(request.getDescription());
        if (request.getImageUrl() != null) category.setImageUrl(request.getImageUrl());
        if (request.getParentId() != null) category.setParentId(request.getParentId());

        return CategoryResponse.fromEntity(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void updateStatus(UpdateCategoryStatusRequest request) {
        Category category = categoryRepository.findById(request.getId())
                .orElseThrow(() -> new CategoryNotFoundException(request.getId()));
        category.setStatus(request.getStatus());
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void softDelete(IdRequest request) {
        Category category = categoryRepository.findById(request.getId())
                .orElseThrow(() -> new CategoryNotFoundException(request.getId()));
        
        category.setDeleted(true);
        category.setDeletedAt(LocalDateTime.now());
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
             com.example.stockpos.app.models.User currentUser = (com.example.stockpos.app.models.User) authentication.getPrincipal();
             category.setDeletedBy(currentUser.getId());
        }
        
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void forceDelete(IdRequest request) {
        if (!categoryRepository.existsById(request.getId())) {
            throw new CategoryNotFoundException(request.getId());
        }
        categoryRepository.deleteById(request.getId());
    }

}
