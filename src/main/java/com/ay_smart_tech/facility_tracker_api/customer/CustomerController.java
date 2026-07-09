package com.ay_smart_tech.facility_tracker_api.customer;

import com.ay_smart_tech.facility_tracker_api.customer.dto.CustomerRequest;
import com.ay_smart_tech.facility_tracker_api.customer.dto.CustomerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    public final CustomerService service;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid  @RequestBody CustomerRequest request){
        CustomerResponse response = service.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // getCustomerById
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(
            @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.getCustomerById(id));
    //  return ResponseEntity.ok(service.getCustomerById(id));
    }

    // getAllCustomers
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllCustomers());
    }



}
