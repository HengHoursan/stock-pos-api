package com.example.stockpos.app.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Data Transfer Object for Role responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {
    private Integer id;
    private String name;
    private String displayName;
    private Boolean status;
    private List<PermissionResponse> permissions;
    public static RoleResponse fromEntity(com.example.stockpos.app.models.Role role) {
        if (role == null) return null;
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .displayName(role.getDisplayName())
                .status(role.getStatus())
                .permissions(role.getPermissions() != null ? role.getPermissions().stream()
                        .map(PermissionResponse::fromEntity)
                        .collect(java.util.stream.Collectors.toList()) : null)
                .build();
    }
}
