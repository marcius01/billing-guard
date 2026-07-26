package tech.skullprogrammer.bguard.test.api.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import tech.skullprogrammer.bguard.api.dto.AnomalyResponse;
import tech.skullprogrammer.bguard.api.dto.FilterForRequest;
import tech.skullprogrammer.bguard.api.operator.PageRequestFactory;
import tech.skullprogrammer.bguard.api.service.AnomalyService;
import tech.skullprogrammer.bguard.domain.SkullException;
import tech.skullprogrammer.bguard.domain.entity.Anomaly;
import tech.skullprogrammer.bguard.domain.entity.Invoice;
import tech.skullprogrammer.bguard.domain.enumeration.EAnomalyStatus;
import tech.skullprogrammer.bguard.domain.enumeration.EAnomalyType;
import tech.skullprogrammer.bguard.domain.enumeration.EInvoiceStatus;
import tech.skullprogrammer.bguard.domain.repository.CustomerRepository;
import tech.skullprogrammer.bguard.domain.repository.SupplyPointRepository;
import tech.skullprogrammer.bguard.test.SpringTestConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@SpringBootTest(classes = {SpringTestConfiguration.class})
@ActiveProfiles("test")
@Sql(scripts = {"classpath:db/insert-test-anomalies.sql"})
@Transactional
public class AnomalyServiceTest {

    @Autowired
    private AnomalyService anomalyService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SupplyPointRepository supplyPointRepository;

    @Test
    public void testFindById() {
        Anomaly anomalyById = anomalyService.getAnomalyById(6001L);
        Assertions.assertEquals(EAnomalyType.NEGATIVE_AMOUNT, anomalyById.getType());
        Assertions.assertEquals(EAnomalyStatus.OPEN, anomalyById.getStatus());
        Assertions.assertEquals("Negative amount", anomalyById.getDescription());
    }

    @Test
    public void testFindFiltered(){
        FilterForRequest<EAnomalyStatus> filters1 = FilterForRequest.<EAnomalyStatus>builder()
                .status(EAnomalyStatus.OPEN)
                .build();
        Page<Anomaly> anomalies1 = anomalyService.getAnomalies(filters1, PageRequestFactory.create(0, 10, "id,desc"));
        Assertions.assertEquals(2, anomalies1.getTotalElements());
        Assertions.assertEquals(2, anomalies1.getNumberOfElements());

        FilterForRequest<EAnomalyStatus> filters2 = FilterForRequest.<EAnomalyStatus>builder()
                .status(EAnomalyStatus.RESOLVED)
                .build();
        Page<Anomaly> anomalies2 = anomalyService.getAnomalies(filters2, PageRequestFactory.create(0, 2, "id,desc"));
        Assertions.assertEquals(1, anomalies2.getTotalElements());
        Assertions.assertEquals(1, anomalies2.getNumberOfElements());
    }

    @Test
    public void changeStatus() {
        Anomaly anomalyChanged = anomalyService.resolveAnomaly(6001L);
        Anomaly anomalyChanged2 = anomalyService.ignoreAnomaly(6002L);
        Anomaly anomalyById = anomalyService.getAnomalyById(6001L);
        Anomaly anomalyById2 = anomalyService.getAnomalyById(6002L);
        Assertions.assertEquals(EAnomalyStatus.RESOLVED, anomalyChanged.getStatus());
        Assertions.assertEquals(EAnomalyStatus.RESOLVED, anomalyById.getStatus());
        Assertions.assertEquals(EAnomalyStatus.IGNORED, anomalyChanged2.getStatus());
        Assertions.assertEquals(EAnomalyStatus.IGNORED, anomalyById2.getStatus());
        Assertions.assertNotNull(anomalyChanged.getResolvedBy());
        Assertions.assertTrue(anomalyChanged.getResolvedAt().isBefore(LocalDateTime.now()));
        Assertions.assertTrue(anomalyChanged.getResolvedAt().isAfter(LocalDateTime.now().minusSeconds(20)));
        SkullException skullException = Assertions.assertThrows(SkullException.class, () -> anomalyService.resolveAnomaly(6003L));
        Assertions.assertEquals(SkullException.ErrorType.UNCHANGED_DATA, skullException.getErrorType());
    }


}
