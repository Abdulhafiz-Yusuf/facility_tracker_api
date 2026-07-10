package com.ay_smart_tech.facility_tracker_api.facility.dtos;

import com.ay_smart_tech.facility_tracker_api.facility.FacilitySatus;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

/**
 * DTO for {@link com.ay_smart_tech.facility_tracker_api.facility.Facility}
 */
@Value
public class FacilityStatusUpdateRequestDto {
    @NotNull
    FacilitySatus status;
}
