package com.example.stockpos.app.dto.role.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionRequest {
    @NotNull(message = "Role ID is required")
    private Integer roleId;

    private List<Integer> permissionIds;
}
