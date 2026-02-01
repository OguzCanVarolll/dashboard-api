package com.dev.dashboard.dashboard_api.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials (BadCredentialsException ex,HttpServletRequest request){
        return buildResponseEntity(
                HttpStatus.UNAUTHORIZED,
                "AUTH_001",
                "Hatalı mail veya şifre",
                request.getRequestURI()
        );
    }
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, HttpServletRequest request) {
        log.warn("İş Mantığı Hatası [{}]: {}", ex.getErrorCode(), ex.getMessage());

        return buildResponseEntity(
                ex.getStatus(),
                ex.getErrorCode(),
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> vErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> vErrors.put(e.getField(), e.getDefaultMessage()));

        ErrorResponse res = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .errorCode("VALIDATION_ERROR")
                .message("Veri giriş hatası")
                .validationErrors(vErrors)
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.badRequest().body(res);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, HttpServletRequest request) {
        log.error("Beklenmeyen Hata: ", ex);
        return buildResponseEntity(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "Sunucu tarafında beklenmeyen bir hata oluştu. Lütfen daha sonra tekrar deneyiniz.",
                request.getRequestURI()
        );
    }
    private ResponseEntity<ErrorResponse> buildResponseEntity(HttpStatus status, String errorCode, String message, String path) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .errorCode(errorCode)
                .message(message)
                .path(path)
                .build();
        return new ResponseEntity<>(error, status);
    }
}