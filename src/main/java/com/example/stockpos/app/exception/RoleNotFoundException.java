package com.example.stockpos.app.exception;

public class RoleNotFoundException extends ResourceNotFoundException {
    public RoleNotFoundException(String message) {
        super(message);
    }

    public RoleNotFoundException(Integer id) {
        super("Role not found with id: " + id);
    }
}
