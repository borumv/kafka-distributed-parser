package com.example.kafkadistributor;

import com.example.kafkadistributor.models.Entity;
import com.example.kafkadistributor.util.IntervalReader;
import com.file.dto.IndexTuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaListeners {

    private static Logger LOG = LoggerFactory
            .getLogger(KafkaListeners.class);
    KafkaTemplate<String, Entity> kafkaTemplate;

    public KafkaListeners(KafkaTemplate<String, Entity> kafkaTemplate, IntervalReader<Entity> intervalReader) {

        this.kafkaTemplate = kafkaTemplate;
        this.intervalReader = intervalReader;
    }

    IntervalReader<Entity> intervalReader;

    @KafkaListener(topics = "split_issues",
            groupId = "split",
            containerFactory = "factory"
    )
    void listener(IndexTuple data) {

        LOG.info("Received the data -> " + data);
        List<? extends Entity> messages = intervalReader.read(data.path(), data.leftIndex(), data.rightIndex());
        messages.forEach(item -> kafkaTemplate.send("stream-input", item.getName(), item));
    }
}
