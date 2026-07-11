package com.ay_smart_tech.facility_tracker_api.facility;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findByCustomerId(Long customerId);
    List<Facility> findByStatus(FacilityStatus status);


}
