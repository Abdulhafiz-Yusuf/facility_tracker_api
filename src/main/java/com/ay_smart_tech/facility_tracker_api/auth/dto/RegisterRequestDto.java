package com.ay_smart_tech.facility_tracker_api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link com.ay_smart_tech.facility_tracker_api.user.User}
 */

public record RegisterRequestDto(
        @NotNull(message = "Email is required")
        @Email(message = "Please enter a standard email")
        String email,

        @Size(message = "Password must be atleast 8 characters", min = 8, max = 12)
        String passwordHash,

        @NotBlank
        String fullName
) {}
