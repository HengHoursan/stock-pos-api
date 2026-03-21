package com.example.stockpos.app.service.impl;

import com.example.stockpos.app.dto.common.request.IdRequest;
import com.example.stockpos.app.dto.common.request.PaginationRequest;
import com.example.stockpos.app.dto.brand.request.CreateBrandRequest;
import com.example.stockpos.app.dto.brand.request.UpdateBrandRequest;
import com.example.stockpos.app.dto.brand.request.UpdateBrandStatusRequest;
import com.example.stockpos.app.dto.common.response.PaginationMeta;
import com.example.stockpos.app.dto.common.response.PaginationResponse;
import com.example.stockpos.app.dto.brand.response.BrandResponse;
import com.example.stockpos.app.exception.common.DuplicateResourceException;
import com.example.stockpos.app.exception.brand.BrandNotFoundException;
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
        return brandRepository.findAll().stream()
                .map(BrandResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public PaginationResponse<BrandResponse> findAllWithPagination(PaginationRequest request) {
        Specification<Brand> spec = (root, query, cb) -> cb.conjunction();

        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            String keyword = "%" + request.getSearch().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), keyword),
                    cb.like(cb.lower(root.get("slug")), keyword),
                    cb.like(cb.lower(root.get("code")), keyword),
                    cb.like(cb.lower(root.get("description")), keyword)
            ));
        }

        if (request.getFilters() != null && request.getFilters().containsKey("status")) {
            boolean isActive = Boolean.parseBoolean(request.getFilters().get("status"));
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), isActive));
        }

        Page<Brand> page = brandRepository.findAll(spec, request.toPageable());
        return PaginationResponse.<BrandResponse>builder()
                .data(page.getContent().stream().map(BrandResponse::fromEntity).collect(Collectors.toList()))
                .meta(PaginationMeta.fromPage(page))
                .build();
    }

    @Override
    public BrandResponse findById(Integer id) {
        return brandRepository.findById(id)
                .map(BrandResponse::fromEntity)
                .orElseThrow(() -> new BrandNotFoundException(id));
    }

    @Override
    @Transactional
    public BrandResponse create(CreateBrandRequest request) {
        if (brandRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Brand with name " + request.getName() + " already exists");
        }
        if (brandRepository.existsBySlug(request.getSlug())) {
            throw new DuplicateResourceException("Brand with slug " + request.getSlug() + " already exists");
        }

        String code = request.getCode();
        if (code == null || code.isEmpty()) {
            code = Helper.generateCode("BRD-");
        }
        
        if (brandRepository.existsByCode(code)) {
            throw new DuplicateResourceException("Brand with code " + code + " already exists");
        }

        Brand brand = Brand.builder()
                .code(code)
                .name(request.getName())
                .slug(request.getSlug())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .parentId(request.getParentId())
                .status(true)
                .build();

        return BrandResponse.fromEntity(brandRepository.save(brand));
    }

    @Override
    @Transactional
    public BrandResponse update(UpdateBrandRequest request) {
        Brand brand = brandRepository.findById(request.getId())
                .orElseThrow(() -> new BrandNotFoundException(request.getId()));

        if (request.getName() != null && !request.getName().equals(brand.getName())) {
            if (brandRepository.existsByName(request.getName())) {
                throw new DuplicateResourceException("Brand with name " + request.getName() + " already exists");
            }
            brand.setName(request.getName());
        }

        if (request.getSlug() != null && !request.getSlug().equals(brand.getSlug())) {
            if (brandRepository.existsBySlug(request.getSlug())) {
                throw new DuplicateResourceException("Brand with slug " + request.getSlug() + " already exists");
            }
            brand.setSlug(request.getSlug());
        }

        if (request.getCode() != null && !request.getCode().equals(brand.getCode())) {
            if (brandRepository.existsByCode(request.getCode())) {
                throw new DuplicateResourceException("Brand with code " + request.getCode() + " already exists");
            }
            brand.setCode(request.getCode());
        }

        if (request.getDescription() != null) brand.setDescription(request.getDescription());
        if (request.getImageUrl() != null) brand.setImageUrl(request.getImageUrl());
        if (request.getParentId() != null) brand.setParentId(request.getParentId());

        return BrandResponse.fromEntity(brandRepository.save(brand));
    }

    @Override
    @Transactional
    public void updateStatus(UpdateBrandStatusRequest request) {
        Brand brand = brandRepository.findById(request.getId())
                .orElseThrow(() -> new BrandNotFoundException(request.getId()));
        brand.setStatus(request.getStatus());
        brandRepository.save(brand);
    }

    @Override
    @Transactional
    public void softDelete(IdRequest request) {
        Brand brand = brandRepository.findById(request.getId())
                .orElseThrow(() -> new BrandNotFoundException(request.getId()));
        
        brand.setDeleted(true);
        brand.setDeletedAt(LocalDateTime.now());
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
             User currentUser = (User) authentication.getPrincipal();
             brand.setDeletedBy(currentUser.getId());
        }
        
        brandRepository.save(brand);
    }

    @Override
    @Transactional
    public void forceDelete(IdRequest request) {
        if (!brandRepository.existsById(request.getId())) {
            throw new BrandNotFoundException(request.getId());
        }
        brandRepository.deleteById(request.getId());
    }

}
