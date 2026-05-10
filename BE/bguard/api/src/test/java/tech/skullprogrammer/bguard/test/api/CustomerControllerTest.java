package tech.skullprogrammer.bguard.test.api;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import tech.skullprogrammer.bguard.api.controller.CustomerController;
import tech.skullprogrammer.bguard.api.dto.CustomerRequest;
import tech.skullprogrammer.bguard.api.service.CustomerService;
import tech.skullprogrammer.bguard.domain.entity.Customer;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(CustomerController.class)
@AutoConfigureRestTestClient
public class CustomerControllerTest {

    @Autowired
    private RestTestClient testClient;

    @MockitoBean
    private CustomerService customerService;

    @Test
    public void testAllCustomers() {
        Customer customer = new Customer();
        customer.setName("Mario");
        given(customerService.allCustomers())
                .willReturn(List.of(customer));
        testClient.get().uri("/customer").exchange().expectStatus().isOk();
    }

    @Test
    public void testSaveCustomer() {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("Mario");
        Customer customer = new Customer();
        customer.setName("Mario");
        customer.setId(111L);
        given(customerService.saveCustomer(any(CustomerRequest.class))).willReturn(customer);
        testClient.post().uri("/customer").body(customerRequest).exchange().expectStatus()
                .isCreated()
                .expectHeader().location("http://localhost/customer/111");
    }
}
