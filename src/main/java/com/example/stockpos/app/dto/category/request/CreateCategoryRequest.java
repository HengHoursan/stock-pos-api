package com.example.stockpos.app.dto.category.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCategoryRequest {

    @Size(max = 50, message = "Code must be at most 50 characters.")
    private String code;

    @PositiveOrZero(message = "Parent ID must be zero or positive.")
    private Integer parentId;

    @NotBlank(message = "Name is required.")
    @Size(max = 100, message = "Name must be at most 100 characters.")
    private String name;

    @NotBlank(message = "Slug is required.")
    @Size(max = 100, message = "Slug must be at most 100 characters.")
    private String slug;

    @Size(max = 255, message = "Description must be at most 255 characters.")
    private String description;

    @Size(max = 255, message = "Image URL must be at most 255 characters.")
    private String imageUrl;
}