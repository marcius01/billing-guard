package tech.skullprogrammer.bguard.test.domain;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.util.Assert;
import tech.skullprogrammer.bguard.domain.entity.Customer;
import tech.skullprogrammer.bguard.domain.repository.CustomerRepository;

import java.util.List;

@DataJpaTest
@EntityScan(basePackages = {"tech.skullprogrammer.bguard.domain"})
public class TestCustomerRepository {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testSave() {
        Customer customer = new Customer();
        customer.setName("Mario");
        customerRepository.save(customer);
        List<Customer> allCustomers = customerRepository.findAll();
        Assert.isTrue(allCustomers.size() == 1, "Customer not saved");
    }
}
