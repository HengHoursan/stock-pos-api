package com.example.stockpos.app.db.seeds;

import com.example.stockpos.app.models.Permission;
import com.example.stockpos.app.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermissionSeeder {
    private final PermissionRepository permissionRepository;

    public void seed() {
        seedEntityPermissions("User", "user");
        seedEntityPermissions("Role", "role");
        seedEntityPermissions("Permission", "permission");
        seedEntityPermissions("Product", "product");
        seedEntityPermissions("Category", "category");
    }

    private void seedEntityPermissions(String displayName, String name) {
        String group = displayName + " Management";
        
        buildPermission("all:" + name, "All " + displayName + "s", group, 1);
        buildPermission("view:" + name, "View " + displayName + "s", group, 2);
        buildPermission("create:" + name, "Create " + displayName, group, 3);
        buildPermission("update:" + name, "Update " + displayName, group, 4);
        buildPermission("delete:" + name, "Delete " + displayName, group, 5);
    }

    private void buildPermission (String name, String displayName, String group, Integer sort) {
        if (!permissionRepository.existsByName(name)) {
            permissionRepository.save(Permission.builder()
                    .name(name)
                    .displayName(displayName)
                    .group(group)
                    .sort(sort)
                    .build());
        }
    }
}
