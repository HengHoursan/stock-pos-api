package com.example.stockpos.app.exception.role;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(Integer id) {
        super("Role not found with id: " + id);
    }
    public RoleNotFoundException(String message) {
        super(message);
    }
}
