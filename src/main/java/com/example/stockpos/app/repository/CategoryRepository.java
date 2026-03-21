package com.example.stockpos.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.stockpos.app.models.Category;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
    boolean existsByName(String name);
}
