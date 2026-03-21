package com.example.stockpos.app.service.impl;

import com.example.stockpos.app.dto.brand.request.*;
import com.example.stockpos.app.dto.brand.response.BrandResponse;
import com.example.stockpos.app.dto.common.request.IdRequest;
import com.example.stockpos.app.dto.common.request.PaginationRequest;
import com.example.stockpos.app.dto.common.response.PaginationMeta;
import com.example.stockpos.app.dto.common.response.PaginationResponse;
import com.example.stockpos.app.models.Brand;
import com.example.stockpos.app.models.User;
import com.example.stockpos.app.repository.BrandRepository;
import com.example.stockpos.app.service.BrandService;
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
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    @Override
    public List<BrandResponse> findAll() {
        return brandRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaginationResponse<BrandResponse> findAllWithPagination(PaginationRequest request) {
        Specification<Brand> specification = (root, query, cb) -> cb.conjunction();

        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            String keyword = "%" + request.getSearch().toLowerCase() + "%";
            specification = specification.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("code")), keyword),
                    cb.like(cb.lower(root.get("name")), keyword),
                    cb.like(cb.lower(root.get("slug")), keyword),
                    cb.like(cb.lower(root.get("description")), keyword)
            ));
        }

        if (request.getFilters() != null && request.getFilters().containsKey("status")) {
            boolean isStatus = "true".equals(request.getFilters().get("status"));
            specification = specification.and((root, query, cb) ->
                    cb.equal(root.get("status"), isStatus)
            );
        }

        Page<Brand> page = brandRepository.findAll(specification, request.toPageable());
        return PaginationResponse.<BrandResponse>builder()
                .data(page.getContent().stream().map(this::mapToResponse).collect(Collectors.toList()))
                .meta(PaginationMeta.fromPage(page))
                .build();
    }

    @Override
    public BrandResponse findById(Integer id) {
        return brandRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Brand not found with id: " + id));
    }

    @Override
    @Transactional
    public BrandResponse create(CreateBrandRequest request) {
        String code = request.getCode();
        if (code == null || code.isEmpty()) {
            code = Helper.generateCode("BRD-");
        }

        if (brandRepository.existsByCode(code)) {
            throw new RuntimeException("Brand code already exists");
        }
        if (brandRepository.existsByName(request.getName())) {
            throw new RuntimeException("Brand name already exists");
        }
        if (brandRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Brand slug already exists");
        }
        Brand brand = Brand.builder()
                .code(code)
                .parentId(request.getParentId())
                .name(request.getName())
                .slug(request.getSlug())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .status(true)
                .build();
        return mapToResponse(brandRepository.save(brand));
    }

    @Override
    @Transactional
    public BrandResponse update(UpdateBrandRequest request) {
        Brand brand = brandRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Brand not found with id: " + request.getId()));

        if (request.getCode() != null && !request.getCode().equals(brand.getCode())) {
            if (brandRepository.existsByCode(request.getCode())) {
                throw new RuntimeException("Brand code already exists");
            }
            brand.setCode(request.getCode());
        }

        if (request.getName() != null && !request.getName().equals(brand.getName())) {
            if (brandRepository.existsByName(request.getName())) {
                throw new RuntimeException("Brand name already exists");
            }
            brand.setName(request.getName());
        }

        if (request.getSlug() != null && !request.getSlug().equals(brand.getSlug())) {
            if (brandRepository.existsBySlug(request.getSlug())) {
                throw new RuntimeException("Brand slug already exists");
            }
            brand.setSlug(request.getSlug());
        }

        if (request.getParentId() != null) brand.setParentId(request.getParentId());
        if (request.getDescription() != null) brand.setDescription(request.getDescription());
        if (request.getImageUrl() != null) brand.setImageUrl(request.getImageUrl());

        return mapToResponse(brandRepository.save(brand));
    }

    @Override
    @Transactional
    public void updateStatus(UpdateBrandStatusRequest request) {
        Brand brand = brandRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Brand not found with id: " + request.getId()));
        brand.setStatus(request.getStatus());
        brandRepository.save(brand);
    }

    @Override
    @Transactional
    public void softDelete(IdRequest request) {
        Brand brand = brandRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Brand not found with id: " + request.getId()));
        brand.setDeleted(true);
        brand.setDeletedAt(LocalDateTime.now());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User user) {
                brand.setDeletedBy(user.getId());
            }
        }

        brandRepository.save(brand);
    }

    @Override
    @Transactional
    public void forceDelete(IdRequest request) {
        if (!brandRepository.existsById(request.getId())) {
            throw new RuntimeException("Brand not found with id: " + request.getId());
        }
        brandRepository.deleteById(request.getId());
    }

    private BrandResponse mapToResponse(Brand brand) {
        return BrandResponse.builder()
                .id(brand.getId())
                .code(brand.getCode())
                .parentId(brand.getParentId())
                .name(brand.getName())
                .slug(brand.getSlug())
                .description(brand.getDescription())
                .imageUrl(brand.getImageUrl())
                .status(brand.getStatus())
                .createdAt(brand.getCreatedAt())
                .updatedAt(brand.getUpdatedAt())
                .createdBy(brand.getCreatedBy())
                .updatedBy(brand.getUpdatedBy())
                .deletedAt(brand.getDeletedAt())
                .deletedBy(brand.getDeletedBy())
                .build();
    }
}
