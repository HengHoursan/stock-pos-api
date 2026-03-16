package com.example.stockpos.app.db.seeds;

import com.example.stockpos.app.models.Permission;
import com.example.stockpos.app.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PermissionSeeder {
    private final PermissionRepository permissionRepository;

    public void seed() {
        if (permissionRepository.count() == 0) {
            List<Permission> permissions = Arrays.asList(
                // User Management
                Permission.builder().name("all:user").displayName("All Users").group("User Management").sort(1).build(),
                Permission.builder().name("view:user").displayName("View Users").group("User Management").sort(2).build(),
                Permission.builder().name("create:user").displayName("Create User").group("User Management").sort(3).build(),
                Permission.builder().name("update:user").displayName("Update User").group("User Management").sort(4).build(),
                Permission.builder().name("delete:user").displayName("Delete User").group("User Management").sort(5).build(),
                
                // Role Management
                Permission.builder().name("all:role").displayName("All Roles").group("Role Management").sort(1).build(),
                Permission.builder().name("view:role").displayName("View Roles").group("Role Management").sort(2).build(),
                Permission.builder().name("create:role").displayName("Create Role").group("Role Management").sort(3).build(),
                Permission.builder().name("update:role").displayName("Update Role").group("Role Management").sort(4).build(),
                Permission.builder().name("delete:role").displayName("Delete Role").group("Role Management").sort(5).build()
            );
            permissionRepository.saveAll(permissions);
        }
    }
}
