package com.dev.dashboard.dashboard_api.dto;

public record LoginResponse(
        String message,
        String token,
        String refreshToken,
        String username
){
}
