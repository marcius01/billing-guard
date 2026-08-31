package tech.skullprogrammer.bguard.api.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;
import tech.skullprogrammer.bguard.api.kafka.event.ImportJobCreatedEvent;
import tech.skullprogrammer.bguard.api.kafka.KafkaTopicConfig;
import tech.skullprogrammer.bguard.api.operator.FileOperator;
import tech.skullprogrammer.bguard.domain.SkullException;
import tech.skullprogrammer.bguard.domain.entity.ImportJob;
import tech.skullprogrammer.bguard.domain.enumeration.EImportJobStatus;
import tech.skullprogrammer.bguard.domain.repository.ImportJobRepository;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class JobService {

    private final ImportJobRepository importJobRepository;
    private final KafkaTemplate<String, ImportJobCreatedEvent> kafkaTemplate;
    private final String jobFolderPath;
    private final String correlationKey;

    public JobService(ImportJobRepository importJobRepository,
                      @Value("${app.import.upload-dir}") String jobFolderPath, @Value("${skullprogrammer.observability.mdc.key}") String correlationKey,
                      KafkaTemplate<String, ImportJobCreatedEvent> kafkaTemplate) {
        this.importJobRepository = importJobRepository;
        this.jobFolderPath = jobFolderPath;
        this.correlationKey = correlationKey;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Page<ImportJob> getJobs(Pageable pagination) {
        return importJobRepository.findAll(pagination);
    }

    public ImportJob getJobById(Long id) {
        return importJobRepository.findById(id).orElse(null);
    }

    @Transactional
    public ImportJob uploadJobs(MultipartFile multipartFile) {
        ImportJob importJob = new ImportJob();
        importJob.setStatus(EImportJobStatus.CREATED);
        importJobRepository.save(importJob);
        try {
            File file = new FileOperator().saveToFile(multipartFile, jobFolderPath, importJob.getId() + "");
            importJob.setFilename(file.getAbsolutePath());
        } catch (IOException e) {
            throw new SkullException(e.getMessage(), SkullException.ErrorType.INVALID_DATA);
        }
        ImportJobCreatedEvent event = new ImportJobCreatedEvent(importJob.getId());

        String correlationId = MDC.get(correlationKey) == null ? "" : MDC.get(correlationKey);
        ProducerRecord<String, ImportJobCreatedEvent> record = new ProducerRecord<>(
                KafkaTopicConfig.TOPIC_IMPORT_JOB_CREATED, importJob.getId().toString(), event
        );
        record.headers().add(correlationKey, correlationId.getBytes(StandardCharsets.UTF_8));
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    kafkaTemplate.send(record).get();
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Error while sending event to kafka", e);
                    throw new SkullException(SkullException.ErrorType.IMPORT_JOB_ERROR);
                }
            }
        });
        return importJob;
    }

}