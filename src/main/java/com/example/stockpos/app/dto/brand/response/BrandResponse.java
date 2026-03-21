package com.example.stockpos.app.dto.brand.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {
    private Integer id;
    private String code;
    private Integer parentId;
    private String name;
    private String slug;
    private String description;
    private String imageUrl;
    private Boolean status;

    // Auditing fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer updatedBy;
    private LocalDateTime deletedAt;
    private Integer deletedBy;
}
