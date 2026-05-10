package tech.skullprogrammer.bguard.test.api;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import tech.skullprogrammer.bguard.api.controller.CustomerController;
import tech.skullprogrammer.bguard.api.service.CustomerService;
import tech.skullprogrammer.bguard.domain.entity.Customer;

import java.util.List;

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
        BDDMockito.given(customerService.allCustomers())
                .willReturn(List.of(customer));
        testClient.get().uri("/customer").exchange().expectStatus().isOk();
    }
}
