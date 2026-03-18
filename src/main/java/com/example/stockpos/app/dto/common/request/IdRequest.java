package com.example.stockpos.app.dto.common.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdRequest {
    @NotNull(message = "ID is required")
    private Integer id;
}
