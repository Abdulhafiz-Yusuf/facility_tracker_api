package com.ay_smart_tech.facility_tracker_api.facility;

import com.ay_smart_tech.facility_tracker_api.common.exceptions.DuplicateResourceException;
import com.ay_smart_tech.facility_tracker_api.common.exceptions.ResourceNotFoundException;
import com.ay_smart_tech.facility_tracker_api.customer.CustomerRepository;
import com.ay_smart_tech.facility_tracker_api.facility.dtos.FacilityRequestDto;
import com.ay_smart_tech.facility_tracker_api.facility.dtos.FacilityResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityService {
    private final FacilityRepository facilityRepo;
    private final CustomerRepository customerRepo;

    @Transactional
    public FacilityResponseDto createFacility(FacilityRequestDto request){
        if(!customerRepo.existsById(request.getCustomerId())){
            throw new ResourceNotFoundException(
                    "Customer not found with id " +  request.getCustomerId());
        }

        if(facilityRepo.findByCustomerId(request.getCustomerId()).stream()
                .anyMatch(f->
                        f.getStatus()  == FacilitySatus.PENDING
                                && f.getFacilityType() == request.getFacilityType()
                )
        ){
            throw new DuplicateResourceException("Customer with id "
                    + request.getCustomerId()
                    + " have pending facility" );
        }

        Facility facility = new Facility();
        facility.setCustomerId(request.getCustomerId());
        facility.setFacilityType(request.getFacilityType());
        facility.setPrincipal(request.getPrincipal());
        facility.setProfitRate(request.getProfitRate());
        // status defaults to PENDING via @PrePersist
        Facility saved = facilityRepo.save(facility);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public FacilityResponseDto getFacilityById(Long id) {
        Facility facility = facilityRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found with id: " + id));
        return toResponse(facility);
    }

    @Transactional(readOnly = true)
    public List<FacilityResponseDto> getAllFacilities() {
        return facilityRepo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FacilityResponseDto> getFacilitiesByCustomer(Long customerId) {
        return facilityRepo.findByCustomerId(customerId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public FacilityResponseDto updateStatus(Long facilityId, FacilitySatus newStatus) {
        Facility facility = facilityRepo.findById(facilityId).orElseThrow(()->
                new ResourceNotFoundException("Facility not found with id " + facilityId));


        facility.transitionTo(newStatus);
        // IllegalStateException thrown if transition is illegal
        // Handled by GlobalExceptionHandler

        Facility updated = facilityRepo.save(facility);
        return toResponse(updated);
    }

    @Transactional
    public FacilityResponseDto toResponse(Facility facility){
        return new FacilityResponseDto(
                facility.getId(),
                facility.getCustomerId(),
                facility.getFacilityType(),
                facility.getPrincipal(),
                facility.getProfitRate(),
                facility.getStatus(),
                facility.getStartDate(),
                facility.getCreatedAt()
        );
    }





}
