package com.example.stockpos.app.dto.permission.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Permission request DTOs for create and update operations.
 */
public class PermissionRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreatePermissionRequest {
        @NotBlank(message = "Name is required")
        @Size(max = 50, message = "Name cannot exceed 50 characters")
        private String name;

        @NotBlank(message = "Display name is required")
        @Size(max = 100, message = "Display name cannot exceed 100 characters")
        private String displayName;

        @NotBlank(message = "Group is required")
        @Size(max = 50, message = "Group cannot exceed 50 characters")
        private String group;

        @NotNull(message = "Sort order is required")
        private Integer sort;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdatePermissionRequest {
        @NotNull(message = "ID is required")
        private Integer id;

        @NotBlank(message = "Name is required")
        @Size(max = 50, message = "Name cannot exceed 50 characters")
        private String name;

        @NotBlank(message = "Display name is required")
        @Size(max = 100, message = "Display name cannot exceed 100 characters")
        private String displayName;

        @NotBlank(message = "Group is required")
        @Size(max = 50, message = "Group cannot exceed 50 characters")
        private String group;

        @NotNull(message = "Sort order is required")
        private Integer sort;
    }
}
