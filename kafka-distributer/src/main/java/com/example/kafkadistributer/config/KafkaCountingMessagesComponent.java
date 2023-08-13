package com.example.kafkadistributer.config;

import com.file.dto.IndexTuple;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class KafkaCountingMessagesComponent {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    public static Map<String, Object> props = new HashMap<>();
//    @PostConstruct
//    public void init(){
//        System.out.println(getTotalNumberOfMessagesInATopic("stream input"));
//    }

    KafkaConsumer<String, String> consumer;

    public KafkaCountingMessagesComponent(KafkaConsumer<String, String> consumer) {

        this.consumer = consumer;
    }

    public Long getTotalNumberOfMessagesInATopic(String topic) {

        List<TopicPartition> partitions = consumer.partitionsFor(topic).stream()
                .map(p -> new TopicPartition(topic, p.partition()))
                .collect(Collectors.toList());
        consumer.assign(partitions);
        consumer.seekToEnd(Collections.emptySet());
        Map<TopicPartition, Long> endPartitions = partitions.stream()
                .collect(Collectors.toMap(Function.identity(), consumer::position));
        return partitions.stream().mapToLong(endPartitions::get).sum();
    }


//    @Override
//    public void run() {
//
//        while (true){
//            System.out.println("Prepare...");
//            System.out.println(getTotalNumberOfMessagesInATopic(countingTopic));
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
}
