package com.ay_smart_tech.facility_tracker_api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String password
) {}
