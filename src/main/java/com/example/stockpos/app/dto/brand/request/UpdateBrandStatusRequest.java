package com.example.stockpos.app.dto.brand.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBrandStatusRequest {
    @NotNull(message = "ID is required.")
    private Integer id;
    
    @NotNull(message = "Status is required.")
    private Boolean status;
}
