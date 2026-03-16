package com.example.stockpos.app.db.seeds;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final PermissionSeeder permissionSeeder;
    private final RoleSeeder roleSeeder;
    private final UserSeeder userSeeder;

    @Override
    public void run(String... args) {
        permissionSeeder.seed();
        roleSeeder.seed();
        userSeeder.seed();
    }
}
