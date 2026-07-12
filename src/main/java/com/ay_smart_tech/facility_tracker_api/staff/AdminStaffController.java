package com.ay_smart_tech.facility_tracker_api.staff;

import com.ay_smart_tech.facility_tracker_api.auth.dto.CreateStaffRequestDto;
import com.ay_smart_tech.facility_tracker_api.auth.dto.CreateStaffResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/staff")
@RequiredArgsConstructor
public class AdminStaffController {

    private final StaffService staffService;

    @PostMapping
    public ResponseEntity<CreateStaffResponseDto> createStaff(@Valid @RequestBody CreateStaffRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(staffService.createStaff(request));
    }
}
