package com.ay_smart_tech.facility_tracker_api.facility;

import com.ay_smart_tech.facility_tracker_api.facility.dto.FacilityRequestDto;
import com.ay_smart_tech.facility_tracker_api.facility.dto.FacilityResponseDto;
import com.ay_smart_tech.facility_tracker_api.facility.dto.FacilityStatusUpdateRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/facilities")
class FacilityController {

    private final FacilityService service;

    @PostMapping
    public ResponseEntity<FacilityResponseDto> createFacility(
            @Valid @RequestBody FacilityRequestDto requestDto
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createFacility(requestDto));
    }

    @PatchMapping("{id}/status")
    public ResponseEntity<FacilityResponseDto> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody FacilityStatusUpdateRequestDto requestDto
    ){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateStatus(id,requestDto.getStatus()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacilityResponseDto> getFacilityById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getFacilityById(id));
    }

    @GetMapping
    public ResponseEntity<List<FacilityResponseDto>> getFacilities(
            @Valid @RequestParam(required = false) Long customerId
    ){
    if(customerId !=  null){
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getFacilitiesByCustomer(customerId));
    }
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAllFacilities());
    }


}
