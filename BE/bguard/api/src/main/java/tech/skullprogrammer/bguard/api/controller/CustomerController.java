package tech.skullprogrammer.bguard.api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.skullprogrammer.bguard.api.dto.CustomerRequest;
import tech.skullprogrammer.bguard.api.dto.CustomerResponse;
import tech.skullprogrammer.bguard.api.dto.PaginationResponse;
import tech.skullprogrammer.bguard.api.mapper.CustomerMapper;
import tech.skullprogrammer.bguard.api.operator.PageRequestFactory;
import tech.skullprogrammer.bguard.api.service.CustomerService;
import tech.skullprogrammer.bguard.domain.entity.Customer;

import java.net.URI;

@Slf4j
@RestController
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    public CustomerController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @GetMapping(value = "/customer")
    public PaginationResponse<CustomerResponse> allCustomers(
            @RequestParam(value = "page", defaultValue = "0") @PositiveOrZero() int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String sort
    ) {
        Pageable pageable = PageRequestFactory.create(page, size, sort);
        Page<Customer> rawResult = customerService.allCustomers(pageable);
        return customerMapper.toRequestDto(rawResult);
    }

    @GetMapping(value = "/customer/{id}")
    public CustomerResponse getCustomerById(@PathVariable("id") Long id) {
        return customerMapper.toResponseDto(customerService.getCustomerById(id));
    }

    @PostMapping(value = "/customer")
    public ResponseEntity<Void> saveCustomer(@RequestBody @Valid CustomerRequest customerRequest) {
        Customer customer = customerService.saveCustomer(customerRequest);
        URI locationUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(customer.getId()).toUri();
        return ResponseEntity.created(locationUri)
                .build();
    }
}
