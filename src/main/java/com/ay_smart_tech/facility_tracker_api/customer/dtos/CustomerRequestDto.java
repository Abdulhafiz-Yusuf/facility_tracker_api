package com.ay_smart_tech.facility_tracker_api.customer.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.ay_smart_tech.facility_tracker_api.customer.Customer}
 */
@Value
public class CustomerRequestDto implements Serializable {

    @NotBlank(message = "Full Name is required")
    String fullName;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email name is required")
    String email;
}
