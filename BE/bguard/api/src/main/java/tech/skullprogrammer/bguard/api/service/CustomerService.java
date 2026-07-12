package tech.skullprogrammer.bguard.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.skullprogrammer.bguard.api.dto.CustomerRequest;
import tech.skullprogrammer.bguard.api.mapper.CustomerMapper;
import tech.skullprogrammer.bguard.domain.SkullException;
import tech.skullprogrammer.bguard.domain.entity.Customer;
import tech.skullprogrammer.bguard.domain.repository.CustomerRepository;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;
    private CustomerMapper customerMapper;


    @Autowired
    public CustomerService(CustomerRepository repository, CustomerMapper mapper) {
        this.customerRepository = repository;
        this.customerMapper = mapper;
    }

    public Page<Customer> allCustomers(Pageable pageable) {
        Page<Customer> result = customerRepository.findAll(pageable);
        return result;
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public Customer getCustomerByCode(String code) {
        return customerRepository.findByExternalCode(code);
    }

    public Customer saveCustomer(CustomerRequest customerRequest) {
        boolean exists = customerRepository.existsByExternalCode(customerRequest.getExternalCode());
        if(exists) throw new SkullException(SkullException.ErrorType.CUSTOMER_ALREADY_EXISTS);
        Customer customer = customerMapper.toEntity(customerRequest);
        return customerRepository.save(customer);
    }
}
