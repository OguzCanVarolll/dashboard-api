package com.dev.dashboard.dashboard_api.dto;

public record LoginResponse(
        String message,
        String accessToken,
        String refreshToken,
        String username
){
}
