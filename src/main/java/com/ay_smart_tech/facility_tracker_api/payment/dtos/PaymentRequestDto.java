package com.ay_smart_tech.facility_tracker_api.payment.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.ay_smart_tech.facility_tracker_api.payment.Payment}
 */
public record PaymentRequestDto(
        @NotNull(message = "Facility ID is required")
        @Positive(message = "Facility ID must be positive and can not be zero")
        Long facilityId,


        @DecimalMin(value = "0.01", message = "Amount paid must be greater than zero")
        @NotNull(message = "Payment amount is required")
        BigDecimal amountPaid
)  {}
