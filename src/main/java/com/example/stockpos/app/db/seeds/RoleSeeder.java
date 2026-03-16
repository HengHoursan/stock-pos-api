package com.example.stockpos.app.db.seeds;

import com.example.stockpos.app.models.Permission;
import com.example.stockpos.app.models.Role;
import com.example.stockpos.app.repository.PermissionRepository;
import com.example.stockpos.app.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleSeeder {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public void seed() {
        if (roleRepository.count() == 0) {
            List<Permission> allPermissions = permissionRepository.findAll();

            // ADMIN Role
            Role adminRole = Role.builder()
                .name("ADMIN")
                .displayName("Administrator")
                .status(true)
                .permissions(allPermissions)
                .build();
            roleRepository.save(adminRole);

            // USER Role (minimal permissions)
            Role userRole = Role.builder()
                .name("USER")
                .displayName("Standard User")
                .status(true)
                .permissions(Collections.emptyList()) // Add specific permissions if needed
                .build();
            roleRepository.save(userRole);
        }
    }
}
