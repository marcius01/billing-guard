package tech.skullprogrammer.bguard.test.api.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import tech.skullprogrammer.bguard.api.dto.FilterForRequest;
import tech.skullprogrammer.bguard.api.dto.InvoiceDTO;
import tech.skullprogrammer.bguard.api.operator.PageRequestFactory;
import tech.skullprogrammer.bguard.api.service.InvoiceService;
import tech.skullprogrammer.bguard.api.service.JobService;
import tech.skullprogrammer.bguard.domain.entity.Customer;
import tech.skullprogrammer.bguard.domain.entity.ImportJob;
import tech.skullprogrammer.bguard.domain.entity.Invoice;
import tech.skullprogrammer.bguard.domain.entity.SupplyPoint;
import tech.skullprogrammer.bguard.domain.enumeration.EInvoiceStatus;
import tech.skullprogrammer.bguard.domain.repository.CustomerRepository;
import tech.skullprogrammer.bguard.domain.repository.ImportJobRepository;
import tech.skullprogrammer.bguard.domain.repository.SupplyPointRepository;
import tech.skullprogrammer.bguard.test.SpringTestConfiguration;

import java.io.IOException;
import java.time.LocalDate;

@Slf4j
@SpringBootTest(classes = {SpringTestConfiguration.class})
@ActiveProfiles("test")
@Sql(scripts = {"classpath:db/insert-test-invoices.sql"})
@Transactional
public class JobServiceTest {

    @Autowired
    private JobService jobService;
    @Autowired
    private ImportJobRepository importJobRepository;

    @Test
    public void testSave() throws IOException {
        MockMultipartFile importJobMultiPart = new MockMultipartFile("file", getClass().getResourceAsStream("/jobs/test-invoices.csv"));
        ImportJob importJob = jobService.uploadJobs(importJobMultiPart);
        Assertions.assertEquals(10, importJob.getTotalRows());
        Assertions.assertEquals(9, importJob.getProcessedRows());
        Assertions.assertEquals(1, importJob.getDiscardedRows());
        Assertions.assertEquals(1, importJob.getAnomalyRows());
    }

}
