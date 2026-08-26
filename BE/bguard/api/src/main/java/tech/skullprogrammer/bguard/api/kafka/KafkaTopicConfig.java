package tech.skullprogrammer.bguard.api.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String TOPIC_IMPORT_JOB_CREATED = "import-job-created";

    @Bean
    public NewTopic importJobCreatedTopic() {
        return TopicBuilder.name(TOPIC_IMPORT_JOB_CREATED)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
