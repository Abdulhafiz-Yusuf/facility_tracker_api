package com.ay_smart_tech.facility_tracker_api.auth.dto;

import com.ay_smart_tech.facility_tracker_api.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateStaffRequestDto(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String fullName,

        @NotNull
        Role role
) {
}
