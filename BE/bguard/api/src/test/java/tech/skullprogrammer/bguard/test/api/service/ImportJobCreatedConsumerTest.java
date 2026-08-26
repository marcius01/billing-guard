package tech.skullprogrammer.bguard.test.api.service;

import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import tech.skullprogrammer.bguard.api.kafka.KafkaTopicConfig;
import tech.skullprogrammer.bguard.api.kafka.event.ImportJobCreatedEvent;
import tech.skullprogrammer.bguard.api.service.JobService;
import tech.skullprogrammer.bguard.domain.entity.ImportJob;
import tech.skullprogrammer.bguard.domain.enumeration.EImportJobStatus;
import tech.skullprogrammer.bguard.domain.repository.ImportJobRepository;
import tech.skullprogrammer.bguard.domain.repository.InvoiceRepository;
import tech.skullprogrammer.bguard.test.SpringTestConfiguration;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest(classes = {SpringTestConfiguration.class})
@ActiveProfiles("test")
@Sql(scripts = {"classpath:db/insert-test-invoices.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
public class ImportJobCreatedConsumerTest {

    @Autowired
    private JobService jobService;
    @Autowired
    private KafkaTemplate<String, ImportJobCreatedEvent> kafkaTemplate;
    @Autowired
    private ImportJobRepository importJobRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Container
    @ServiceConnection
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("apache/kafka:latest"));

    @Test
    public void testConsume() throws Exception {
        Path resourcePath = Path.of(getClass().getResource("/jobs/test-invoices.csv").toURI());
        ImportJob importJob = new ImportJob();
        importJob.setFilename(resourcePath.toFile().getAbsolutePath());
        importJob.setStatus(EImportJobStatus.CREATED);
        importJobRepository.save(importJob);

        ImportJobCreatedEvent event = new ImportJobCreatedEvent(importJob.getId());
        kafkaTemplate.send(KafkaTopicConfig.TOPIC_IMPORT_JOB_CREATED, String.valueOf(importJob.getId()), event).get();
        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() ->
                importJobRepository.findById(importJob.getId()).map(j -> j.getStatus() == EImportJobStatus.COMPLETED_WITH_ERRORS).orElse(false)
        );
        ImportJob importJobOnDB = importJobRepository.findById(importJob.getId()).get();
        Assertions.assertEquals(10, importJobOnDB.getTotalRows());
        Assertions.assertEquals(9, importJobOnDB.getProcessedRows());
        Assertions.assertEquals(1, importJobOnDB.getDiscardedRows());
        Assertions.assertEquals(1, importJobOnDB.getAnomalyRows());
    }

    @Test
    public void testIdempotence () throws  Exception {
        Long initialCount = invoiceRepository.count();
        Assertions.assertEquals(3, initialCount);
        Path resourcePath = Path.of(getClass().getResource("/jobs/test-invoices.csv").toURI());
        ImportJob importJob = new ImportJob();
        importJob.setFilename(resourcePath.toFile().getAbsolutePath());
        importJob.setStatus(EImportJobStatus.CREATED);
        importJobRepository.save(importJob);

        ImportJobCreatedEvent event = new ImportJobCreatedEvent(importJob.getId());
        kafkaTemplate.send(KafkaTopicConfig.TOPIC_IMPORT_JOB_CREATED, String.valueOf(importJob.getId()), event).get();
        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() ->
                importJobRepository.findById(importJob.getId()).map(j -> j.getStatus() == EImportJobStatus.COMPLETED_WITH_ERRORS).orElse(false)
        );

        Long firstCount = invoiceRepository.count();
        Assertions.assertEquals(12, firstCount);
        kafkaTemplate.send(KafkaTopicConfig.TOPIC_IMPORT_JOB_CREATED, String.valueOf(importJob.getId()), event).get();
        Thread.sleep(5000);
        Long secondCount = invoiceRepository.count();
        Assertions.assertEquals(12, secondCount);
    }

}
