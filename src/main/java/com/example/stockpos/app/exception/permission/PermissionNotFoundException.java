package com.example.stockpos.app.exception.permission;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PermissionNotFoundException extends RuntimeException {
    public PermissionNotFoundException(Integer id) {
        super("Permission not found with id: " + id);
    }
    public PermissionNotFoundException(String message) {
        super(message);
    }
}
