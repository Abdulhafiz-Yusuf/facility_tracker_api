package com.ay_smart_tech.facility_tracker_api.payment.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link com.ay_smart_tech.facility_tracker_api.payment.Payment}
 */
public record PaymentResponseDto(
        Long facilityId,
        BigDecimal amountPaid,
        BigDecimal OutstandingBalance,
        LocalDate paymentDate
)  {}
