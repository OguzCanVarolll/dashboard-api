package com.dev.dashboard.dashboard_api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Null olan alanları JSON'da gizle (Temiz görünüm)
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String errorCode; // Örn: AUTH_001
    private String message;
    private String path;
    private Map<String, String> validationErrors; // Sadece validasyon hataları için
}