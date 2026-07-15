package com.ay_smart_tech.facility_tracker_api.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByFacilityId(Long facilityId);

    @Query("""
    SELECT COALESCE(SUM(p.amountPaid), 0)
    FROM Payment p
    WHERE p.facilityId = :facilityId
      AND p.id <= :paymentId
    """)
    BigDecimal sumAmountPaidToDate(@Param("facilityId") Long facilityId,
                                   @Param("paymentId") Long paymentId);


    @Query("""
    SELECT COALESCE(SUM(p.amountPaid), 0)
    FROM Payment p
    WHERE p.facilityId = :facilityId
    """)
    BigDecimal sumAmountPaid(@Param("facilityId") Long facilityId);


}
