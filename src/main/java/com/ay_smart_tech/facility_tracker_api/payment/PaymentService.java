package com.ay_smart_tech.facility_tracker_api.payment;

import com.ay_smart_tech.facility_tracker_api.common.exceptions.BusinessRuleException;
import com.ay_smart_tech.facility_tracker_api.common.exceptions.ResourceNotFoundException;
import com.ay_smart_tech.facility_tracker_api.facility.Facility;
import com.ay_smart_tech.facility_tracker_api.facility.FacilityRepository;
import com.ay_smart_tech.facility_tracker_api.facility.FacilitySatus;
import com.ay_smart_tech.facility_tracker_api.payment.dtos.PaymentRequestDto;
import com.ay_smart_tech.facility_tracker_api.payment.dtos.PaymentResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepo;
    private final FacilityRepository facilityRepo;
    // Direct repository call today (same JVM, same DB).
    // On extraction: replaced by a call to Facility Service's API instead —
    // same pattern as FacilityService -> CustomerRepository.

    //createPayment
    @Transactional
    public PaymentResponseDto createPayment(PaymentRequestDto request){
        Facility facility = findFacilityById(request.facilityId())

        if(facility.getStatus() != FacilitySatus.ACTIVE){
            throw new BusinessRuleException("Facility with Id " + request.facilityId() + " not ACTIVE");
        }

        BigDecimal currentOutstandingBal = facility.getPrincipal()
                .subtract(paymentRepo.sumAmountPaidByFacilityId(request.facilityId()));

        if(request.amountPaid().compareTo(currentOutstandingBal) > 0){
            throw new BusinessRuleException("Payment amount " + request.amountPaid()
                    + " exceed outstanding balance "+ currentOutstandingBal);
        }

        Payment payment = new Payment();
        payment.setAmountPaid(request.amountPaid());
        payment.setFacilityId(request.facilityId());

        BigDecimal outstandingBalAfter = currentOutstandingBal.subtract(request.amountPaid());

        Payment SavedPayment = paymentRepo.save(payment);

        return toResponse(SavedPayment,outstandingBalAfter);

    }


    //getPaymentById
    @Transactional
    public PaymentResponseDto getPaymentById(Long id){
        Payment payment = findPaymentById(id);
        Facility facility = findFacilityById(payment.getFacilityId());
        BigDecimal totalPaidAmount = paymentRepo.sumAmountPaidByFacilityId(facility.getId());
        BigDecimal outstandingBal = facility.getPrincipal().subtract(totalPaidAmount);
        return toResponse(payment,outstandingBal);
    }



    //getPaymentsByFacility
    @Transactional
    public List<PaymentResponseDto> getPaymentsByFacility(Long facilityId){
        Facility facility = findFacilityById(facilityId);


    }


    //getAllPayments
    // toResponse
    //PaymentResponse
    public PaymentResponseDto toResponse(Payment payment, BigDecimal outstandingBal){
        return new PaymentResponseDto(
                payment.getFacilityId(),
                payment.getAmountPaid(),
                outstandingBal,
                payment.getPaymentDate()
        );
    }

    private Facility findFacilityById(Long facilityId){
        return facilityRepo.findById(facilityId).orElseThrow(()->
                new ResourceNotFoundException("Facility with id " + facilityId + " not found"));
    }

   private Payment findPaymentById(Long paymentId){
        return paymentRepo.findById(paymentId).orElseThrow(()->
                new ResourceNotFoundException("Payment with id " + paymentId + " not found"));
    }


}
