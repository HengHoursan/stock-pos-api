package com.example.stockpos.app.exception;

public class PermissionNotFoundException extends ResourceNotFoundException {
    public PermissionNotFoundException(String message) {
        super(message);
    }

    public PermissionNotFoundException(Integer id) {
        super("Permission not found with id: " + id);
    }
}
