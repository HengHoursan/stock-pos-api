package com.example.stockpos.app.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationMeta {
    private Long total;
    private Integer from;
    private Integer to;
    private Integer page;
    private Integer limit;
}
