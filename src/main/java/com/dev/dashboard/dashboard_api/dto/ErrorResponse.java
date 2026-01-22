package com.dev.dashboard.dashboard_api.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        String message,
        String details
) {
}
