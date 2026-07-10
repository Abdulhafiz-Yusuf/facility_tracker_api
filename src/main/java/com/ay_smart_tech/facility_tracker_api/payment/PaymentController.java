package com.ay_smart_tech.facility_tracker_api.payment;

import com.ay_smart_tech.facility_tracker_api.payment.dtos.PaymentRequestDto;
import com.ay_smart_tech.facility_tracker_api.payment.dtos.PaymentResponseDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@AllArgsConstructor
class PaymentController {

    private final PaymentService service;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(
            @Valid @RequestBody PaymentRequestDto request){

        return ResponseEntity.status(HttpStatus.CREATED).body(service.createPayment(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.getPaymentById(id));
    }


}
