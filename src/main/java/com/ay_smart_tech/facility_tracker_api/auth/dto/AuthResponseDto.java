package com.ay_smart_tech.facility_tracker_api.auth.dto;

import com.ay_smart_tech.facility_tracker_api.user.Role;

public record AuthResponseDto(
   String token,
   Role role,
   boolean mustChangePassword
) {}
