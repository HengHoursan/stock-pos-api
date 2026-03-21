package com.example.stockpos.app.service;

import com.example.stockpos.app.dto.brand.request.*;
import com.example.stockpos.app.dto.brand.response.BrandResponse;
import com.example.stockpos.app.dto.common.request.IdRequest;
import com.example.stockpos.app.dto.common.request.PaginationRequest;
import com.example.stockpos.app.dto.common.response.PaginationResponse;

import java.util.List;

public interface BrandService {
    List<BrandResponse> findAll();
    PaginationResponse<BrandResponse> findAllWithPagination(PaginationRequest request);
    BrandResponse findById(Integer id);
    BrandResponse create(CreateBrandRequest request);
    BrandResponse update(UpdateBrandRequest request);
    void updateStatus(UpdateBrandStatusRequest request);
    void softDelete(IdRequest request);
    void forceDelete(IdRequest request);
}
