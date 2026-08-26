package tech.skullprogrammer.bguard.test.api.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import tech.skullprogrammer.bguard.api.kafka.KafkaTopicConfig;
import tech.skullprogrammer.bguard.api.kafka.event.ImportJobCreatedEvent;
import tech.skullprogrammer.bguard.api.operator.PageRequestFactory;
import tech.skullprogrammer.bguard.api.service.JobService;
import tech.skullprogrammer.bguard.domain.entity.ImportJob;
import tech.skullprogrammer.bguard.test.SpringTestConfiguration;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@Slf4j
@SpringBootTest(classes = {SpringTestConfiguration.class})
@ActiveProfiles("test")
@Sql(scripts = {"classpath:db/insert-test-invoices.sql"})
@Transactional
public class JobServiceTest {

    @Autowired
    private JobService jobService;

    @MockitoBean
    private KafkaTemplate<String, ImportJobCreatedEvent> kafkaTemplate;

    @BeforeEach
    public void setup(){
        ProducerRecord<String, ImportJobCreatedEvent> producerRecord = new ProducerRecord<>(KafkaTopicConfig.TOPIC_IMPORT_JOB_CREATED, "1", new ImportJobCreatedEvent(1L));
        RecordMetadata recordMetadata = new RecordMetadata(
                new TopicPartition(KafkaTopicConfig.TOPIC_IMPORT_JOB_CREATED, 0), // topic + partizione
                0L,   // baseOffset
                0,    // batchIndex
                System.currentTimeMillis(), // timestamp
                0,    // serializedKeySize
                0     // serializedValueSize
        );
        SendResult<String, ImportJobCreatedEvent> sendResult = new SendResult<>(producerRecord, recordMetadata);
        given(kafkaTemplate.send(anyString(), anyString(), any(ImportJobCreatedEvent.class)))
                .willReturn(CompletableFuture.completedFuture(sendResult));
    }

    @Test
    public void testSave() throws IOException {
        MockMultipartFile importJobMultiPart = new MockMultipartFile("file", "test-invoices.csv", "text/csv", getClass().getResourceAsStream("/jobs/test-invoices.csv"));
        ImportJob importJob = jobService.uploadJobs(importJobMultiPart);
        verify(kafkaTemplate).send(
                eq(KafkaTopicConfig.TOPIC_IMPORT_JOB_CREATED),
                eq(String.valueOf(importJob.getId())),
                any(ImportJobCreatedEvent.class));
    }

    @Test
    @Sql(scripts = {"classpath:db/insert-test-import-jobs.sql"})
    public void testFind() throws IOException {
        Pageable pageable = PageRequestFactory.create(0, 10, "id,desc");
        Page<ImportJob> jobs = jobService.getJobs(pageable);
        Assertions.assertEquals(3, jobs.getTotalElements());
        jobs.stream().filter(job -> job.getId().equals(4002L)).findFirst().ifPresent(
                job -> Assertions.assertEquals(2, job.getErrors().size()));
    }

    @Test
    @Sql(scripts = {"classpath:db/insert-test-import-jobs.sql"})
    public void testFindById() throws IOException {
        ImportJob job = jobService.getJobById(4002L);
        Assertions.assertNotNull(job);
        Assertions.assertEquals(2, job.getErrors().size());
    }
}
