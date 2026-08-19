package tech.skullprogrammer.bguard.test.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import tech.skullprogrammer.bguard.domain.entity.Customer;
import tech.skullprogrammer.bguard.domain.repository.CustomerRepository;

import java.util.Optional;

@Testcontainers
@DataJpaTest
@EntityScan(basePackages = {"tech.skullprogrammer.bguard.domain"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryIT {

    @Container
    @ServiceConnection
    private static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18.3-alpine3.23");

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void saveCustomer () {
        Customer customer = new Customer();
        customer.setName("Mario");
        customer.setEmail("mario@fake.com");
        customer.setExternalCode("111");
        customer.setTaxCode("111");
        customerRepository.save(customer);
        Optional<Customer> customerById = customerRepository.findById(customer.getId());
        Assertions.assertTrue(customerById.isPresent());
    }

    @Test
    public void deleteCustomer () {
        Customer customer = new Customer();
        customer.setName("Mario");
        customer.setEmail("mario@fake.com");
        customer.setExternalCode("111");
        customer.setTaxCode("111");
        customerRepository.save(customer);
        Optional<Customer> customerById = customerRepository.findById(customer.getId());
        Assertions.assertTrue(customerById.isPresent());
        customerRepository.deleteById(customerById.get().getId());
        Optional<Customer> customerByIdDeleted = customerRepository.findById(customer.getId());
        Assertions.assertFalse(customerByIdDeleted.isPresent());

    }

    @Test
    public void findByExternalCode () {
        Customer customer = new Customer();
        customer.setName("Mario");
        customer.setEmail("mario@fake.com");
        customer.setExternalCode("111");
        customer.setTaxCode("111");
        customerRepository.save(customer);
        Customer customerByExternalCode = customerRepository.findByExternalCode(customer.getExternalCode());
        boolean exists = customerRepository.existsByExternalCode(customer.getExternalCode());
        Assertions.assertNotNull(customerByExternalCode);
        Assertions.assertTrue(exists);

    }
}
