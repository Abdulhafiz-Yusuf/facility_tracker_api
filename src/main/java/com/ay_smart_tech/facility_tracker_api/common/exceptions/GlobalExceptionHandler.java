package com.ay_smart_tech.facility_tracker_api.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---- 404 Not Found ----
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildBody(ex.getMessage()));
    }

    // ---- 409 Conflict: duplicate resource (e.g. email already exists) ----
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildBody(ex.getMessage()));
    }

    // ---- 409 Conflict: illegal Facility state transition ----
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildBody(ex.getMessage()));
    }

    // ---- 409 Conflict: business rule violation (e.g. overpayment, inactive facility) ----
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessRule(BusinessRuleException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildBody(ex.getMessage()));
    }

    // ---- 400 Bad Request: Jakarta Validation failures (@Valid on DTOs) ----
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        Map<String, Object> body = buildBody("Validation failed");
        body.put("errors", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // ---- 401 Unauthorized: wrong email/password on login ----
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildBody(ex.getMessage()));
    }

    // ---- 403 Forbidden: authenticated but not permitted (e.g. wrong role) ----
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(buildBody("Access denied"));
    }

    // ---- 500 fallback: anything unhandled above ----
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildBody("An unexpected error occurred"));
    }

    private Map<String, Object> buildBody(String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        return body;
    }


}
