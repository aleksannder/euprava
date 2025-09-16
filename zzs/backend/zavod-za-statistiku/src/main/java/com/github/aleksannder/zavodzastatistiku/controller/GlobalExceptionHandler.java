package com.github.aleksannder.zavodzastatistiku.controller;

import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException exception) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", exception.getMessage(),
                "type", exception.getClass().getSimpleName()
        ));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleGenericException(Exception exception) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", exception.getMessage(),
                "type", exception.getClass().getSimpleName()
        ));
    }


}
