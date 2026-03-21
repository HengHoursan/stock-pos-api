package com.example.stockpos.app.dto.category.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryStatusRequest {
    @NotNull (message = "ID is required.")
    private Integer id;
    @Builder.Default
    private Boolean status =  true;

}
