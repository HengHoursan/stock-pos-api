package com.example.stockpos.app.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {
    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer limit = 10;

    private String search;

    private Map<String, String> filters;

    private String orderBy;
}
