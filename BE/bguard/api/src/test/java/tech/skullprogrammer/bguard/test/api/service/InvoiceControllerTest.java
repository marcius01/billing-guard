package tech.skullprogrammer.bguard.test.api.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import tech.skullprogrammer.bguard.api.controller.InvoiceController;
import tech.skullprogrammer.bguard.api.dto.*;
import tech.skullprogrammer.bguard.api.mapper.InvoiceMapperImpl;
import tech.skullprogrammer.bguard.api.service.InvoiceService;
import tech.skullprogrammer.bguard.domain.entity.Invoice;
import tech.skullprogrammer.bguard.test.SpringTestConfigurationMVC;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Import(InvoiceMapperImpl.class)
@WebMvcTest(InvoiceController.class)
@ContextConfiguration(classes = SpringTestConfigurationMVC.class)
@AutoConfigureRestTestClient
public class InvoiceControllerTest {

    @Autowired
    private RestTestClient testClient;

    @MockitoBean
    private InvoiceService invoiceService;

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
        testClient.post().uri("/api/invoices").body(invoiceDTO).exchange().expectStatus()
                .isCreated()
                .expectHeader().location("http://localhost/api/invoices/333");
    }

}
