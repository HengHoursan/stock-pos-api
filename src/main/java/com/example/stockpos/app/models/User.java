package com.example.stockpos.app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User entity that implements UserDetails for Spring Security integration.
 */
@Data
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Builder.Default
    private Boolean status = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null || role.getName() == null) {
            return Collections.emptyList();
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // 1. Add the role name (e.g., "ADMIN")
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        
        // 2. Add granular permissions (e.g., "read:products", "write:orders")
        if (role.getRolePermissions() != null) {
            List<SimpleGrantedAuthority> permissionAuthorities = role.getRolePermissions()
                .stream()
                .map(rp -> new SimpleGrantedAuthority(rp.getPermission().getName()))
                .collect(Collectors.toList());
            authorities.addAll(permissionAuthorities);
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    // Account is always active unless status is false
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Uses the 'status' field to determine if the user can log in
    @Override
    public boolean isEnabled() {
        return status != null && status;
    }
}
