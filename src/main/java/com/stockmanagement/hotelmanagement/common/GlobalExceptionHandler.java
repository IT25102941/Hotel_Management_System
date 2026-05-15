package com.stockmanagement.hotelmanagement.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.stockmanagement.hotelmanagement.api")
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(
            ResourceNotFoundException ex, WebRequest request) {
        ApiResponse<Object> response = new ApiResponse<>(
                false,
                ex.getMessage(),
                null,
                "NOT_FOUND"
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidRequest(
            InvalidRequestException ex, WebRequest request) {
        ApiResponse<Object> response = new ApiResponse<>(
                false,
                ex.getMessage(),
                null,
                "INVALID_REQUEST"
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage())
        );
        ApiResponse<Object> response = new ApiResponse<>(
                false,
                "Validation failed",
                errors,
                "VALIDATION_ERROR"
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(
            Exception ex, WebRequest request) {
        ApiResponse<Object> response = new ApiResponse<>(
                false,
                "An unexpected error occurred",
                null,
                "INTERNAL_SERVER_ERROR"
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}




