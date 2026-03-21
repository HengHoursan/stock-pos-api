package com.example.stockpos.app.exception.brand;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BrandNotFoundException extends RuntimeException {
    public BrandNotFoundException(Integer id) {
        super("Brand not found with id: " + id);
    }
    public BrandNotFoundException(String message) {
        super(message);
    }
}
