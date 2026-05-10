package tech.skullprogrammer.bguard.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.skullprogrammer.bguard.api.service.CustomerService;
import tech.skullprogrammer.bguard.domain.entity.Customer;

import java.util.List;

@Slf4j
@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(value = "/customer")
    public List<Customer> allCustomers() {
        return customerService.allCustomers();
    }
}
