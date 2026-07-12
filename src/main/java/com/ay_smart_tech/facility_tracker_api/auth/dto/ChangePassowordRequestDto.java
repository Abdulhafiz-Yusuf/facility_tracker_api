package com.ay_smart_tech.facility_tracker_api.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePassowordRequestDto(
        @NotBlank
        String oldPassword,

        @NotBlank
        @Size(min = 8)
        String newPassword
) {
}
