package com.ay_smart_tech.facility_tracker_api.customer;

import com.ay_smart_tech.facility_tracker_api.common.exceptions.DuplicateResourceException;
import com.ay_smart_tech.facility_tracker_api.common.exceptions.ResourceNotFoundException;
import com.ay_smart_tech.facility_tracker_api.customer.dto.CustomerRequestDto;
import com.ay_smart_tech.facility_tracker_api.customer.dto.CustomerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepo;

    // createCustomer()
    @Transactional
    public CustomerResponseDto createCustomer(CustomerRequestDto request){
        // throw a duplicateResource exception if customer with request.email already exist
        if(customerRepo.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException(
                    "Customer with email " + request.getEmail() + " already exist"
            );
        }
        // create a new instance of Customer and setEmail(requet.getEmail) setFullname(request.getFullName)
        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());

        // use customerRepo to save and return toRespose of the saved Item.
        Customer saved = customerRepo.save(customer);

        return toResponse(saved);

    }

    // getCustomerById()
    @Transactional(readOnly = true)
    public CustomerResponseDto getCustomerById(Long id){
        Customer customer = customerRepo.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Customer not found with id: " + id));

        return toResponse(customer);
    }
    // getAllCustomers()
    @Transactional(readOnly = true)
    public List<CustomerResponseDto> getAllCustomers(){
        return customerRepo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }


    // toReponse()
    private CustomerResponseDto toResponse(Customer customer) {
        return new CustomerResponseDto(
                customer.getId(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getKycStatus(),
                customer.getCreatedAt()
        );
    }


}
