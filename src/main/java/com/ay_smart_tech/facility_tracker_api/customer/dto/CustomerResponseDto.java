package com.ay_smart_tech.facility_tracker_api.customer.dto;

import com.ay_smart_tech.facility_tracker_api.customer.KycStatus;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.ay_smart_tech.facility_tracker_api.customer.Customer}
 */
@Value
public class CustomerResponseDto implements Serializable {
    Long id;
    String fullName;
    String email;
    KycStatus kycStatus;
    LocalDateTime createdAt;

}
