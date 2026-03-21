package com.example.stockpos.app.exception.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Integer id) {
        super("Category not found with id: " + id);
    }
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
