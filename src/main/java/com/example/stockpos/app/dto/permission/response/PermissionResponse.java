package com.example.stockpos.app.dto.permission.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Permission responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse {
    private Integer id;
    private String name;
    private String displayName;
    private String group;
    private Integer sort;
    public static PermissionResponse fromEntity(com.example.stockpos.app.models.Permission permission) {
        if (permission == null) return null;
        return PermissionResponse.builder()
                .id(permission.getId())
                .name(permission.getName())
                .displayName(permission.getDisplayName())
                .group(permission.getGroup())
                .sort(permission.getSort())
                .build();
    }
}
