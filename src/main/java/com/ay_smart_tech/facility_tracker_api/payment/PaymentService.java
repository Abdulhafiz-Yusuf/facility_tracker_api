package com.ay_smart_tech.facility_tracker_api.payment;

import com.ay_smart_tech.facility_tracker_api.common.exceptions.BusinessRuleException;
import com.ay_smart_tech.facility_tracker_api.common.exceptions.ResourceNotFoundException;
import com.ay_smart_tech.facility_tracker_api.facility.Facility;
import com.ay_smart_tech.facility_tracker_api.facility.FacilityRepository;
import com.ay_smart_tech.facility_tracker_api.facility.FacilityStatus;
import com.ay_smart_tech.facility_tracker_api.facility.FacilityStatus;
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
        Facility facility = findFacilityById(request.facilityId());

        if(facility.getStatus() != FacilityStatus.ACTIVE){
            throw new BusinessRuleException("Facility with Id " + request.facilityId() + " not ACTIVE");
        }


        BigDecimal totalAmountPaid = paymentRepo.sumAmountPaid(request.facilityId());
        BigDecimal currentOutstandingBal = facility.getPrincipal().subtract(totalAmountPaid);

        if(request.amountPaid().compareTo(currentOutstandingBal) > 0 ){
            throw new BusinessRuleException("Payment amount " + request.amountPaid()
                    + " exceed outstanding balance "+ currentOutstandingBal);
        }
        Payment payment = new Payment();
        payment.setAmountPaid(request.amountPaid());
        payment.setFacilityId(request.facilityId());


        BigDecimal outstandingBalAfter = currentOutstandingBal.subtract(request.amountPaid());


        if(outstandingBalAfter.compareTo(BigDecimal.ZERO) == 0 ) {
            facility.transitionTo(FacilityStatus.CLOSED);
            facilityRepo.save(facility);
        }
//            return toResponse(SavedPayment, outstandingBalAfter, facility.getStatus());

        Payment SavedPayment = paymentRepo.save(payment);
        return new PaymentResponseDto(
                SavedPayment.getId(),
                SavedPayment.getFacilityId(),
                SavedPayment.getAmountPaid(),
                outstandingBalAfter,
                SavedPayment.getPaymentDate(),
                facility.getStatus()
        );
    }


    //getPaymentById
    @Transactional
    public PaymentResponseDto getPaymentById(Long id){
        Payment payment = findPaymentById(id);
        Facility facility = findFacilityById(payment.getFacilityId());
        BigDecimal totalPaidAmount = paymentRepo.sumAmountPaidToDate(facility.getId(),id);
        BigDecimal outstandingBal = facility.getPrincipal().subtract(totalPaidAmount);
        return toResponse(payment,outstandingBal,facility.getStatus());
    }



    //getPaymentsByFacility
    @Transactional
    public List<PaymentResponseDto> getPaymentsByFacilityId(Long facilityId){
        Facility facility = findFacilityById(facilityId);
        List<Payment> payments = paymentRepo.findByFacilityId(facilityId);
        return payments.stream().map(p -> getPaymentById(p.getId())).toList();
    }


    //getAllPayments
    @Transactional
    public List<PaymentResponseDto> getAllPayments() {
        return paymentRepo.findAll().stream()
                .map(p -> getPaymentById(p.getId()))
                .toList();
    }


    // toResponse
    private PaymentResponseDto toResponse(Payment payment, BigDecimal outstandingBal,FacilityStatus facilityStatus){
        return new PaymentResponseDto(
                payment.getId(),
                payment.getFacilityId(),
                payment.getAmountPaid(),
                outstandingBal,
                payment.getPaymentDate(),
                facilityStatus

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
