package com.ay_smart_tech.facility_tracker_api.facility.dto;

import com.ay_smart_tech.facility_tracker_api.facility.FacilitySatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * DTO for {@link com.ay_smart_tech.facility_tracker_api.facility.Facility}
 */
@Value
public class FacilityStatusUpdateRequestDto {
    @NotNull
    FacilitySatus status;
}
