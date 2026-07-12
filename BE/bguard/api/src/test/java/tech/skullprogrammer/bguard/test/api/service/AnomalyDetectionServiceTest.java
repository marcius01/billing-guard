package tech.skullprogrammer.bguard.test.api.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import tech.skullprogrammer.bguard.api.service.AnomalyDetectionService;
import tech.skullprogrammer.bguard.domain.entity.Anomaly;
import tech.skullprogrammer.bguard.domain.entity.Invoice;
import tech.skullprogrammer.bguard.domain.enumeration.EAnomalyType;
import tech.skullprogrammer.bguard.test.SpringTestConfiguration;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@SpringBootTest(classes = {SpringTestConfiguration.class})
@ActiveProfiles("test")
@Sql(scripts = {"classpath:db/insert-test-invoices.sql"})
@Transactional
public class AnomalyDetectionServiceTest {

    @Autowired
    private AnomalyDetectionService anomalyDetectionService;

    @Test
    public void testShouldCreateAnomalyWhenAmountIsNegative() throws IOException {
        List<Invoice> invoices = List.of(
                Invoice.builder()
                        .periodStart(LocalDate.now().minusDays(1))
                        .periodEnd(LocalDate.now())
                        .issueDate(LocalDate.now().minusDays(1))
                        .dueDate(LocalDate.now().plusDays(1))
                        .paymentDate(LocalDate.now())
                        .amount(-100.00)
                        .build(),
                Invoice.builder()
                        .periodStart(LocalDate.now().minusDays(1))
                        .periodEnd(LocalDate.now())
                        .issueDate(LocalDate.now().minusDays(1))
                        .dueDate(LocalDate.now().plusDays(1))
                        .paymentDate(LocalDate.now())
                        .amount(100.00)
                        .paidAmount(100.00)
                        .build()
        );
        List<Anomaly> anomalies = anomalyDetectionService.checkForAnomalies(invoices, null);
        Assertions.assertEquals(1, anomalies.size());
        Anomaly anomaly = anomalies.get(0);
        Assertions.assertEquals(EAnomalyType.NEGATIVE_AMOUNT, anomaly.getType());
    }

}
