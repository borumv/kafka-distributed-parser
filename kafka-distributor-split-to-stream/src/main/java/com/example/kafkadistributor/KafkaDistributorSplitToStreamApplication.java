package com.example.kafkadistributor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class KafkaDistributorSplitToStreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaDistributorSplitToStreamApplication.class, args);
	}

}
