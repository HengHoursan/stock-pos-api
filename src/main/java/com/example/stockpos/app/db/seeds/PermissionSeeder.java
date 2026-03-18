package com.example.stockpos.app.db.seeds;

import com.example.stockpos.app.models.Permission;
import com.example.stockpos.app.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PermissionSeeder {
    private final PermissionRepository permissionRepository;

    public void seed() {
        if (permissionRepository.count() == 0) {
            seedEntityPermissions("User", "user");
            seedEntityPermissions("Role", "role");
            seedEntityPermissions("Permission", "permission");
        }
    }

    private void seedEntityPermissions(String displayName, String name) {
        String group = displayName + " Management";
        permissionRepository.saveAll(List.of(
            buildPermission("all:" + name, "All " + displayName + "s", group, 1),
            buildPermission("view:" + name, "View " + displayName + "s", group, 2),
            buildPermission("create:" + name, "Create " + displayName, group, 3),
            buildPermission("update:" + name, "Update " + displayName, group, 4),
            buildPermission("delete:" + name, "Delete " + displayName, group, 5)
        ));
    }

    private Permission buildPermission(String name, String displayName, String group, Integer sort) {
        return Permission.builder()
                .name(name)
                .displayName(displayName)
                .group(group)
                .sort(sort)
                .build();
    }
}
