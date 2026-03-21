package com.example.stockpos.app.dto.brand.response;

import com.example.stockpos.app.models.Brand;
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

    public static BrandResponse fromEntity(Brand brand) {
        if (brand == null) return null;
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
