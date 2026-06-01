package tech.skullprogrammer.bguard.test.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import tech.skullprogrammer.bguard.api.service.CustomerService;
import tech.skullprogrammer.bguard.domain.SkullException;
import tech.skullprogrammer.bguard.domain.entity.Customer;
import tech.skullprogrammer.bguard.test.SpringTestConfiguration;
import tech.skullprogrammer.bguard.test.SpringTestConfigurationMVC;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Import(CustomerMapperImpl.class)
@WebMvcTest(CustomerController.class)
@ContextConfiguration(classes = SpringTestConfigurationMVC.class)
@AutoConfigureRestTestClient
public class CustomerControllerTest {

    @Autowired
    private RestTestClient testClient;
    @Autowired
    private CustomerMapper customerMapper;

    @MockitoBean
    private CustomerService customerService;

    @Test
    public void testFindCustomerByIdOK() {
        Customer customer = new Customer();
        customer.setName("Mario");
        customer.setId(111L);
        given(customerService.getCustomerById(any()))
                .willReturn(customer);
        CustomerResponse responseBody = testClient.get().uri("/customer/111")
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
        ErrorResponse responseBody = testClient.get().uri("/customer/1ko")
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
        PaginationResponse responseBody = testClient.get().uri("/customer").exchange().expectStatus()
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
        testClient.post().uri("/customer").body(customerRequest).exchange().expectStatus()
                .isCreated()
                .expectHeader().location("http://localhost/customer/111");
    }

    @Test
    public void testSaveCustomerKO() {
        CustomerRequest request = CustomerRequest.builder().name("Mario").build();
        Customer customer = new Customer();
        customer.setName("Mario");
        customer.setId(111L);
        given(customerService.saveCustomer(any(CustomerRequest.class))).willReturn(customer);
        ErrorResponse responseBody = testClient.post().uri("/customer").body(request).exchange().expectStatus()
                .isEqualTo(SkullException.ErrorType.INVALID_DATA.getHttpStatus())
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();
    }

}
