package com.example.stockpos.app.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

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

    public static PaginationMeta fromPage(Page<?> page) {
        int offset = (int) page.getPageable().getOffset();
        int count  = page.getNumberOfElements();
        return PaginationMeta.builder()
                .total(page.getTotalElements())
                .from(count > 0 ? offset + 1 : 0)
                .to(count > 0 ? offset + count : 0)
                .page(page.getNumber())
                .limit(page.getSize())
                .build();
    }
}
