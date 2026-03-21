package com.example.stockpos.app.repository;

import com.example.stockpos.app.models.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BrandRepository extends JpaRepository<Brand, Integer>, JpaSpecificationExecutor<Brand> {
    boolean existsByCode(String code);
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
}
