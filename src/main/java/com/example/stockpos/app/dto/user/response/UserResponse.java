package com.example.stockpos.app.dto.user.response;

import com.example.stockpos.app.dto.role.response.RoleResponse;
import com.example.stockpos.app.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Integer id;
    private String username;
    private String email;
    private Boolean status;
    private RoleResponse role;

    // Auditing fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer updatedBy;
    private LocalDateTime deletedAt;
    private Integer deletedBy;

    public static UserResponse fromEntity(User user) {
        if (user == null) return null;
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .status(user.getStatus())
                .role(RoleResponse.fromEntity(user.getRole()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .updatedBy(user.getUpdatedBy())
                .deletedAt(user.getDeletedAt())
                .deletedBy(user.getDeletedBy())
                .build();
    }
}
