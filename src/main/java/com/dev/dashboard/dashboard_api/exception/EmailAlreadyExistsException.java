package com.dev.dashboard.dashboard_api.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BaseException {
    public EmailAlreadyExistsException(String email) {
        super("Email zaten kayıtlı: " + email, HttpStatus.CONFLICT, "AUTH_001");
    }
}