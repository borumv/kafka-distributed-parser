package com.example.kafkadistributer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaListeners {


    @KafkaListener(topics = "stream-output",
            groupId = "split",
            containerFactory = "factory"
    )
    void listener(String message) {

        System.out.println("COMPLETE -> " + message);
        log.info("Received the data -> " + message);

    }
}
