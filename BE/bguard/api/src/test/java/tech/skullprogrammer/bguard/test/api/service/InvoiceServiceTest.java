package tech.skullprogrammer.bguard.test.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import tech.skullprogrammer.bguard.api.dto.FilterForRequest;
import tech.skullprogrammer.bguard.api.operator.FilterSpecificationFactory;
import tech.skullprogrammer.bguard.api.operator.PageRequestFactory;
import tech.skullprogrammer.bguard.api.service.InvoiceService;
import tech.skullprogrammer.bguard.domain.entity.Invoice;
import tech.skullprogrammer.bguard.domain.enumeration.EInvoiceStatus;
import tech.skullprogrammer.bguard.test.SpringTestConfiguration;

import java.time.LocalDate;

@SpringBootTest(classes = {SpringTestConfiguration.class})
@ActiveProfiles("test")
@Sql(scripts = {"classpath:db/insert-test-invoices.sql"})
@Transactional
public class InvoiceServiceTest {

    @Autowired
    private InvoiceService invoiceService;

    @Test
    public void testFind() {
        Invoice invoiceById = invoiceService.getInvoiceById(3001L);
        Assertions.assertEquals("INV-TEST-001", invoiceById.getInvoiceNumber());
        Assertions.assertEquals(EInvoiceStatus.PAID, invoiceById.getStatus());
        Assertions.assertEquals("EXT-TEST-001", invoiceById.getCustomer().getExternalCode());
        Assertions.assertEquals("SP-TEST-001", invoiceById.getSupplyPoint().getCode());
    }

    @Test
    public void testFindAll(){
        FilterForRequest filters1 = FilterForRequest.builder()
                .issueDateFrom(LocalDate.of(2024, 4,1))
                .build();
        Page<Invoice> invoices1 = invoiceService.getAllInvoices(filters1, PageRequestFactory.create(0, 10, "id,desc"));
        Assertions.assertEquals(2, invoices1.getTotalElements());
        Assertions.assertEquals(2, invoices1.getNumberOfElements());

        FilterForRequest filters2 = FilterForRequest.builder()
                .issueDateFrom(LocalDate.of(2024, 3,1))
                .build();
        Page<Invoice> invoices2 = invoiceService.getAllInvoices(filters2, PageRequestFactory.create(0, 2, "id,desc"));
        Assertions.assertEquals(3, invoices2.getTotalElements());
        Assertions.assertEquals(2, invoices2.getNumberOfElements());
    }


}
