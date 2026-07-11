package com.ay_smart_tech.facility_tracker_api.facility.dtos;

import com.ay_smart_tech.facility_tracker_api.facility.FacilityStatus;
import com.ay_smart_tech.facility_tracker_api.facility.FacilityType;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.ay_smart_tech.facility_tracker_api.facility.Facility}
 */
@Value
public class FacilityResponseDto {
    Long id;
    Long customerId;
    FacilityType facilityType;
    BigDecimal principal;
    BigDecimal profitRate;
    FacilityStatus status;
    LocalDateTime createdAt;
}
