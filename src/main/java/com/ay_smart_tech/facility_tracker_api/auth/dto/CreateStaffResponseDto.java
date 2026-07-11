package com.ay_smart_tech.facility_tracker_api.auth.dto;

public record CreateStaffResponseDto(
        Long StaffId,
        String email,
        String temporaryPassword // shown exactly once, never stored in plaintext
) {
}
