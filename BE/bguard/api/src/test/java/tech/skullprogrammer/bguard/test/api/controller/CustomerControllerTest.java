package tech.skullprogrammer.bguard.test.api.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import tech.skullprogrammer.bguard.api.controller.CustomerController;
import tech.skullprogrammer.bguard.api.dto.CustomerRequest;
import tech.skullprogrammer.bguard.api.dto.CustomerResponse;
import tech.skullprogrammer.bguard.api.dto.ErrorResponse;
import tech.skullprogrammer.bguard.api.dto.PaginationResponse;
import tech.skullprogrammer.bguard.api.mapper.CustomerMapper;
import tech.skullprogrammer.bguard.api.mapper.CustomerMapperImpl;
import tech.skullprogrammer.bguard.api.security.SecurityConfiguration;
import tech.skullprogrammer.bguard.api.service.CustomerService;
import tech.skullprogrammer.bguard.domain.SkullException;
import tech.skullprogrammer.bguard.domain.entity.Customer;
import tech.skullprogrammer.bguard.domain.enumeration.ERole;
import tech.skullprogrammer.bguard.test.SpringTestConfigurationMVC;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Import({CustomerMapperImpl.class, SecurityConfiguration.class})
@WebMvcTest(CustomerController.class)
@ContextConfiguration(classes = SpringTestConfigurationMVC.class)
@AutoConfigureRestTestClient
public class CustomerControllerTest {

    @Autowired
    private RestTestClient testClient;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private CustomerService customerService;
    @MockitoBean
    private UserDetailsService userDetailsService;

    @BeforeEach
    public void setUp() {
        UserDetails admin = User.builder().username("admdin").password(passwordEncoder.encode("admin123")).roles(ERole.ADMIN.name()).build();
        given(userDetailsService.loadUserByUsername("admdin"))
                .willReturn(admin);
    }

    @Test
    public void testFindCustomerByIdOK() {
        Customer customer = new Customer();
        customer.setName("Mario");
        customer.setId(111L);
        given(customerService.getCustomerById(any()))
                .willReturn(customer);
        CustomerResponse responseBody = testClient.get().uri("/api/customers/111")
                .headers(headers -> headers.setBasicAuth("admdin", "admin123"))
                .exchange().expectStatus()
                .isOk().returnResult(CustomerResponse.class)
                .getResponseBody();
        Assertions.assertEquals("Mario", responseBody.getName());
    }

    @Test
    public void testFindCustomerByIdKO() {
        Customer customer = new Customer();
        customer.setName("Mario");
        customer.setId(111L);
        given(customerService.getCustomerById(any()))
                .willReturn(customer);
        ErrorResponse responseBody = testClient.get().uri("/api/customers/1ko")
                .headers(headers -> headers.setBasicAuth("admdin", "admin123"))
                .exchange().expectStatus()
                .isEqualTo(SkullException.ErrorType.INVALID_DATA.getHttpStatus())
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();
    }

    @Test
    public void testAllCustomers() {
        Customer customer = new Customer();
        customer.setName("Mario");
        customer.setId(111L);
        Page<Customer> customerPage = new PageImpl<>(
                List.of(customer),
                PageRequest.of(0, 21),
                1
        );
        given(customerService.allCustomers(any()))
                .willReturn(customerPage);
        PaginationResponse responseBody = testClient.get().uri("/api/customers")
                .headers(headers -> headers.setBasicAuth("admdin", "admin123"))
                .exchange().expectStatus()
                .isOk().returnResult(PaginationResponse.class)
                .getResponseBody();
    }

    @Test
    public void testSaveCustomerOK() {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("Mario");
        customerRequest.setEmail("mario@fake.com");
        customerRequest.setExternalCode("111");
        customerRequest.setTaxCode("111");
        Customer customer = new Customer();
        customer.setName("Mario");
        customer.setId(111L);
        given(customerService.saveCustomer(any(CustomerRequest.class))).willReturn(customer);
        testClient.post().uri("/api/customers").body(customerRequest)
                .headers(headers -> headers.setBasicAuth("admdin", "admin123"))
                .exchange().expectStatus()
                .isCreated()
                .expectHeader().location("http://localhost/api/customers/111");
    }

    @Test
    public void testSaveCustomerKO() {
        CustomerRequest request = CustomerRequest.builder().name("Mario").build();
        Customer customer = new Customer();
        customer.setName("Mario");
        customer.setId(111L);
        given(customerService.saveCustomer(any(CustomerRequest.class))).willReturn(customer);
        ErrorResponse responseBody = testClient.post().uri("/api/customers").body(request)
                .headers(headers -> headers.setBasicAuth("admdin", "admin123"))
                .exchange().expectStatus()
                .isEqualTo(SkullException.ErrorType.INVALID_DATA.getHttpStatus())
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();
    }

}
