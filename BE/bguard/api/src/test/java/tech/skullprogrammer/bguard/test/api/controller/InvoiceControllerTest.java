package tech.skullprogrammer.bguard.test.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import tech.skullprogrammer.bguard.api.controller.InvoiceController;
import tech.skullprogrammer.bguard.api.dto.InvoiceDTO;
import tech.skullprogrammer.bguard.api.mapper.InvoiceMapperImpl;
import tech.skullprogrammer.bguard.api.security.SecurityConfiguration;
import tech.skullprogrammer.bguard.api.service.InvoiceService;
import tech.skullprogrammer.bguard.api.service.JWTService;
import tech.skullprogrammer.bguard.domain.entity.Invoice;
import tech.skullprogrammer.bguard.domain.enumeration.ERole;
import tech.skullprogrammer.bguard.test.SpringTestConfigurationMVC;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Import({InvoiceMapperImpl.class, SecurityConfiguration.class, JWTService.class})
@WebMvcTest(InvoiceController.class)
@ContextConfiguration(classes = SpringTestConfigurationMVC.class)
@ActiveProfiles("test")
@AutoConfigureRestTestClient
public class InvoiceControllerTest {

    @Autowired
    private RestTestClient testClient;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTService jwtService;

    @MockitoBean
    private InvoiceService invoiceService;

    private String token;

    @BeforeEach
    public void setUp() {
        UserDetails admin = User.builder().username("admin").password(passwordEncoder.encode("admin123")).roles(ERole.ADMIN.name()).build();
        token = jwtService.generateToken(admin);
    }

    @Test
    public void testSaveInvoiceOK() {
        InvoiceDTO invoiceDTO = InvoiceDTO.builder()
                .invoiceNumber("INV-001")
                .issueDate(LocalDate.now())
                .periodStart(LocalDate.now().minusDays(30))
                .periodEnd(LocalDate.now())
                .amount(100.00)
                .customerId(111L)
                .supplyPointId(222L)
                .build();
        Invoice invoice = new Invoice();
        invoice.setId(333L);
        given(invoiceService.saveInvoice(any(InvoiceDTO.class))).willReturn(invoice);
        testClient.post().uri("/api/invoices").body(invoiceDTO)
                .headers(headers -> headers.setBearerAuth(token))
                .exchange().expectStatus()
                .isCreated()
                .expectHeader().location("http://localhost/api/invoices/333");
    }

}
