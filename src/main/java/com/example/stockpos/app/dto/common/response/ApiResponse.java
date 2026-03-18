package com.example.stockpos.app.dto.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private Boolean success;
    private Integer status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return success("Operation successful", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return success(message, data, 200);
    }

    public static <T> ApiResponse<T> success(String message, T data, Integer status) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(status)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(Integer status, String message) {
        return error(status, message, null);
    }

    public static <T> ApiResponse<T> error(Integer status, String message, T data) {
        return ApiResponse.<T>builder()
                .success(false)
                .status(status)
                .message(message)
                .data(data)
                .build();
    }
}
