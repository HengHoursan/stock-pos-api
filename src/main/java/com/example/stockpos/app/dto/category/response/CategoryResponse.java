package com.example.stockpos.app.dto.category.response;

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
public class CategoryResponse {

    private Integer id;

    private String name;

    private String description;

    private String imageUrl;

    private Integer parentId;

    private Boolean status;
}