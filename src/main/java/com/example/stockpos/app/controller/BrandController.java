package com.example.stockpos.app.controller;

import com.example.stockpos.app.dto.brand.request.*;
import com.example.stockpos.app.dto.brand.response.BrandResponse;
import com.example.stockpos.app.dto.common.request.IdRequest;
import com.example.stockpos.app.dto.common.request.PaginationRequest;
import com.example.stockpos.app.dto.common.response.ApiResponse;
import com.example.stockpos.app.dto.common.response.PaginationResponse;
import com.example.stockpos.app.service.BrandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
@Tag(name = "Brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping("/all")
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Brands fetched successfully", brandService.findAll()));
    }

    @PostMapping("/list")
    public ResponseEntity<ApiResponse<PaginationResponse<BrandResponse>>> getAllPaginated(
            @RequestBody PaginationRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Brands fetched successfully", brandService.findAllWithPagination(request)));
    }

    @PostMapping("/detail")
    public ResponseEntity<ApiResponse<BrandResponse>> getById(@Valid @RequestBody IdRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Brand fetched successfully", brandService.findById(request.getId())));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<BrandResponse>> create(@Valid @RequestBody CreateBrandRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Brand created successfully", brandService.create(request)));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<BrandResponse>> update(@Valid @RequestBody UpdateBrandRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Brand updated successfully", brandService.update(request)));
    }

    @PostMapping("/update-status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(@Valid @RequestBody UpdateBrandStatusRequest request) {
        brandService.updateStatus(request);
        return ResponseEntity.ok(ApiResponse.success("Brand status updated successfully", null));
    }

    @PostMapping("/soft-delete")
    public ResponseEntity<ApiResponse<Void>> softDelete(@Valid @RequestBody IdRequest request) {
        brandService.softDelete(request);
        return ResponseEntity.ok(ApiResponse.success("Brand soft-deleted successfully", null));
    }

    @PostMapping("/force-delete")
    public ResponseEntity<ApiResponse<Void>> forceDelete(@Valid @RequestBody IdRequest request) {
        brandService.forceDelete(request);
        return ResponseEntity.ok(ApiResponse.success("Brand force-deleted successfully", null));
    }
}
