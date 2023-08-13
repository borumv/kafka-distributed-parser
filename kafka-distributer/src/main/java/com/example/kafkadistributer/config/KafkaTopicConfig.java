package com.example.kafkadistributer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic splitIssues() {

        return TopicBuilder.name("split_issues")
                .build();
    }

    @Bean
    public NewTopic streamInputIssues() {

        return TopicBuilder.name("stream-input")
                .build();
    }

    @Bean
    public NewTopic streamOutputIssues() {

        return TopicBuilder.name("stream-output")
                .build();
    }
}
