package com.example.stockpos.app.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    public static UserResponse fromEntity(com.example.stockpos.app.models.User user) {
        if (user == null) return null;
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .status(user.getStatus())
                .role(RoleResponse.fromEntity(user.getRole()))
                .build();
    }
}
