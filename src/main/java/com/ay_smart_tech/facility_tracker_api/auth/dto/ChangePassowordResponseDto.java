package com.ay_smart_tech.facility_tracker_api.auth.dto;

public record ChangePassowordResponseDto(
        String message,
        boolean mustChangePassword,
        boolean success
) {
}
