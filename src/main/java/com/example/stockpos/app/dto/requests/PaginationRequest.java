package com.example.stockpos.app.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

    public Pageable toPageable() {
        Sort sort = (orderBy != null && !orderBy.isEmpty()) ? Sort.by(orderBy) : Sort.unsorted();
        return PageRequest.of(page, limit, sort);
    }
}
