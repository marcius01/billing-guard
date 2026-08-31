package tech.skullprogrammer.bguard.test.api.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import tech.skullprogrammer.bguard.api.dto.FilterForRequest;
import tech.skullprogrammer.bguard.api.dto.InvoiceDTO;
import tech.skullprogrammer.bguard.api.operator.PageRequestFactory;
import tech.skullprogrammer.bguard.api.service.InvoiceService;
import tech.skullprogrammer.bguard.domain.SkullException;
import tech.skullprogrammer.bguard.domain.entity.Customer;
import tech.skullprogrammer.bguard.domain.entity.Invoice;
import tech.skullprogrammer.bguard.domain.entity.SupplyPoint;
import tech.skullprogrammer.bguard.domain.enumeration.EInvoiceStatus;
import tech.skullprogrammer.bguard.domain.repository.CustomerRepository;
import tech.skullprogrammer.bguard.domain.repository.SupplyPointRepository;
import tech.skullprogrammer.bguard.test.SpringTestConfiguration;

import java.time.LocalDate;

@Slf4j
@SpringBootTest(classes = {SpringTestConfiguration.class})
@ActiveProfiles("test")
@Sql(scripts = {"classpath:db/insert-test-invoices.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class InvoiceServiceTest {

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SupplyPointRepository supplyPointRepository;

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

    @Test
    public void testSave(){
        InvoiceDTO invoiceDTOConsistent = buildConsistentInvoice();
        Invoice savedInvoice = invoiceService.saveInvoice(invoiceDTOConsistent);
        Assertions.assertNotNull(savedInvoice);
        InvoiceDTO inconsistentInvoice1 = buildInconsistentInvoice1();
        Assertions.assertThrows(SkullException.class, () -> invoiceService.saveInvoice(inconsistentInvoice1));
        InvoiceDTO inconsistentInvoice2 = buildInconsistentInvoice2();
        Assertions.assertThrows(SkullException.class, () -> invoiceService.saveInvoice(inconsistentInvoice2));
        InvoiceDTO inconsistentInvoice3 = buildInconsistentInvoice3();
        SkullException skullException1 = Assertions.assertThrows(SkullException.class, () -> invoiceService.saveInvoice(inconsistentInvoice3));
        Assertions.assertEquals(1, skullException1.getPayload().size());
        InvoiceDTO inconsistentInvoice4 = buildInconsistentInvoice4();
        SkullException skullException2 = Assertions.assertThrows(SkullException.class, () -> invoiceService.saveInvoice(inconsistentInvoice4));
        Assertions.assertEquals(2, skullException2.getPayload().size());
        InvoiceDTO inconsistentInvoice5 = buildInconsistentInvoice5();
        SkullException skullException3 = Assertions.assertThrows(SkullException.class, () -> invoiceService.saveInvoice(inconsistentInvoice5));
        Assertions.assertEquals(SkullException.ErrorType.SUPPLY_POINT_NOT_FOUND, skullException3.getErrorType());
    }

    private InvoiceDTO buildConsistentInvoice() {
        Customer customer = customerRepository.findAll().getFirst();
        SupplyPoint supplyPoint = supplyPointRepository.findAll().getFirst();
        return InvoiceDTO.builder()
                .invoiceNumber("INV-TEST-NEW2")
                .issueDate(LocalDate.of(2024, 6, 1))
                .dueDate(LocalDate.of(2024, 6, 30))
                .paymentDate(LocalDate.of(2024, 6, 20))
                .periodStart(LocalDate.of(2024, 5, 1))
                .periodEnd(LocalDate.of(2024, 5, 31))
                .amount(300.00)
                .paidAmount(300.00)   // PAID => paidAmount == amount
                .status(EInvoiceStatus.PAID)
                .supplyPointId(supplyPoint.getId())
                .customerId(customer.getId())
                .build();
    }

    private InvoiceDTO buildInconsistentInvoice1() {
        Customer customer = customerRepository.findAll().getFirst();
        SupplyPoint supplyPoint = supplyPointRepository.findAll().getFirst();
        return InvoiceDTO.builder()
                .invoiceNumber("INV-TEST-NOT1")
                .issueDate(LocalDate.of(2024, 6, 1))
                .dueDate(LocalDate.of(2024, 6, 30))
                .paymentDate(LocalDate.of(2024, 6, 20))
                .periodStart(LocalDate.of(2024, 5, 1))
                .periodEnd(LocalDate.of(2024, 5, 31))
                .amount(300.00)
                .paidAmount(250.00)
                .status(EInvoiceStatus.PAID)
                .supplyPointId(supplyPoint.getId())
                .customerId(customer.getId())
                .build();
    }

    private InvoiceDTO buildInconsistentInvoice2() {
        Customer customer = customerRepository.findAll().getFirst();
        SupplyPoint supplyPoint = supplyPointRepository.findAll().getFirst();
        return InvoiceDTO.builder()
                .invoiceNumber("INV-TEST-NOT2")
                .issueDate(LocalDate.of(2024, 6, 1))
                .dueDate(LocalDate.of(2024, 6, 30))
                .periodStart(LocalDate.of(2024, 5, 1))
                .periodEnd(LocalDate.of(2024, 5, 31))
                .amount(300.00)
                .paidAmount(250.00)
                .status(EInvoiceStatus.PARTIALLY_PAID)
                .supplyPointId(supplyPoint.getId())
                .customerId(customer.getId())
                .build();
    }

    private InvoiceDTO buildInconsistentInvoice3() {
        Customer customer = customerRepository.findAll().getFirst();
        SupplyPoint supplyPoint = supplyPointRepository.findAll().getFirst();
        return InvoiceDTO.builder()
                .invoiceNumber("INV-TEST-NOT3")
                .issueDate(LocalDate.of(2024, 6, 1))
                .dueDate(LocalDate.of(2024, 6, 30))
                .paymentDate(LocalDate.of(2024, 6, 20))
                .periodStart(LocalDate.of(2024, 5, 1))
                .periodEnd(LocalDate.of(2024, 5, 31))
                .amount(300.00)
                .paidAmount(0.00)
                .status(EInvoiceStatus.PARTIALLY_PAID)
                .supplyPointId(supplyPoint.getId())
                .customerId(customer.getId())
                .build();
    }

    private InvoiceDTO buildInconsistentInvoice4() {
        Customer customer = customerRepository.findAll().getFirst();
        SupplyPoint supplyPoint = supplyPointRepository.findAll().getFirst();
        return InvoiceDTO.builder()
                .invoiceNumber("INV-TEST-NOT4")
                .issueDate(LocalDate.of(2024, 6, 1))
                .dueDate(LocalDate.of(2024, 6, 30))
                .periodStart(LocalDate.of(2024, 5, 1))
                .periodEnd(LocalDate.of(2024, 5, 31))
                .amount(300.00)
                .paidAmount(10.00)
                .status(EInvoiceStatus.PAID)
                .supplyPointId(supplyPoint.getId())
                .customerId(customer.getId())
                .build();
    }

    private InvoiceDTO buildInconsistentInvoice5() {
        Customer customer = customerRepository.findAll().getFirst();
        SupplyPoint supplyPoint = supplyPointRepository.findAll().get(1);
        return InvoiceDTO.builder()
                .invoiceNumber("INV-TEST-NOT4")
                .issueDate(LocalDate.of(2024, 6, 1))
                .dueDate(LocalDate.of(2024, 6, 30))
                .periodStart(LocalDate.of(2024, 5, 1))
                .periodEnd(LocalDate.of(2024, 5, 31))
                .amount(300.00)
                .paidAmount(10.00)
                .status(EInvoiceStatus.PAID)
                .supplyPointId(supplyPoint.getId())
                .customerId(customer.getId())
                .build();
    }

}
