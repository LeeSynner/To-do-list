package com.example.to_do_list.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleConflictDataExceptions(ConflictDataException ex) {
        Map<String, String> message = new HashMap<>();
        message.put("message", ex.getMessage());
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleAuthenticationExceptions(AuthenticationException ex) {
        Map<String, String> message = new HashMap<>();
        message.put("message", ex.getMessage());
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }
}
