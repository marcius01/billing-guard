package tech.skullprogrammer.bguard.api.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import tech.skullprogrammer.bguard.api.kafka.event.ImportJobCreatedEvent;
import tech.skullprogrammer.bguard.api.kafka.KafkaTopicConfig;
import tech.skullprogrammer.bguard.api.service.ImportProcessingService;
import tech.skullprogrammer.bguard.domain.SkullException;
import tech.skullprogrammer.bguard.domain.entity.ImportJob;

@Slf4j
@Component
public class ImportJobConsumer {

    private final ImportProcessingService importProcessingService;
    private final String correlationKey;

    public ImportJobConsumer(ImportProcessingService importProcessingService, @Value("${skullprogrammer.observability.mdc.key}") String correlationKey) {
        this.importProcessingService = importProcessingService;
        this.correlationKey = correlationKey;
    }

    @KafkaListener(topics = KafkaTopicConfig.TOPIC_IMPORT_JOB_CREATED, groupId = "first")
    public void consumeImportEventCreated(ImportJobCreatedEvent event,
                                          @Header(value = "correlationId", required = false) String correlationId) {
        try {
            if (correlationId != null) {
                MDC.put(correlationKey, correlationId);
            }
            ImportJob importJob = importProcessingService.importJob(event.importJobId());
        } catch (SkullException e) {
            if (e.getErrorType() == SkullException.ErrorType.IMPORT_JOB_ALREADY_PROCESSED) {
                log.info("Import job {} already processed, skipping duplicate delivery", event.importJobId());
            } else {
                log.error("exception on event {} - exception: {}", event, e.toString());
                throw e;
            }
        } catch (Exception e) {
            log.error("exception on event {} - exception: {}", event, e.toString());
            throw e;
        } finally {
            MDC.remove(correlationKey);
        }
    }

}
