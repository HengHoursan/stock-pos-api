package com.example.stockpos.app.dto.category.response;

import com.example.stockpos.app.models.Category;
 
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

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

    public static CategoryResponse fromEntity(Category category) {
        if (category == null) return null;
        return CategoryResponse.builder()
                .id(category.getId())
                .code(category.getCode())
                .parentId(category.getParentId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .status(category.getStatus())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .createdBy(category.getCreatedBy())
                .updatedBy(category.getUpdatedBy())
                .deletedAt(category.getDeletedAt())
                .deletedBy(category.getDeletedBy())
                .build();
    }
}