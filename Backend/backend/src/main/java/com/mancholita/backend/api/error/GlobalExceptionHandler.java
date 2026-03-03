package com.mancholita.backend.api.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 🔹 1️⃣ Validaciones @Valid (DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest req) {

        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> fieldErrors.put(e.getField(), e.getDefaultMessage()));

        ApiError err = ApiError.of(
                400,
                "Validation Error",
                "Request has invalid fields",
                req.getRequestURI()
        );

        err.setFieldErrors(fieldErrors);

        return ResponseEntity.badRequest().body(err);
    }

    // 🔹 2️⃣ Validaciones tipo @RequestParam / @PathVariable
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraint(
            ConstraintViolationException ex,
            HttpServletRequest req) {

        ApiError err = ApiError.of(
                400,
                "Bad Request",
                ex.getMessage(),
                req.getRequestURI()
        );

        return ResponseEntity.badRequest().body(err);
    }

    // 🔹 3️⃣ Errores controlados del negocio
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest req) {

        ApiError err = ApiError.of(
                400,
                "Bad Request",
                ex.getMessage(),
                req.getRequestURI()
        );

        return ResponseEntity.badRequest().body(err);
    }

    // 🔹 4️⃣ Acceso denegado (ROLE incorrecto)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest req) {

        ApiError err = ApiError.of(
                403,
                "Forbidden",
                "You do not have permission to access this resource",
                req.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

    // 🔹 5️⃣ No autenticado (sin Basic Auth)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuth(
            AuthenticationException ex,
            HttpServletRequest req) {

        ApiError err = ApiError.of(
                401,
                "Unauthorized",
                "Authentication is required",
                req.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
    }

    // 🔹 6️⃣ Cualquier error inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(
            Exception ex,
            HttpServletRequest req) {

        ApiError err = ApiError.of(
                500,
                "Internal Server Error",
                "Unexpected error occurred",
                req.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
}