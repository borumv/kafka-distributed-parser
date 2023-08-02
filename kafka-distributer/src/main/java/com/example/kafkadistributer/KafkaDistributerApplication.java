package com.example.kafkadistributer;

import com.file.dto.IndexTuple;
import com.file.splitters.Splitter;
import com.file.splitters.SplitterFactory;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import java.nio.file.Path;
import java.util.Map;

@SpringBootApplication
public class KafkaDistributerApplication implements CommandLineRunner {

    private static Logger LOG = LoggerFactory
            .getLogger(KafkaDistributerApplication.class);
    KafkaTemplate<Integer, Object> kafkaTemplate;

    public KafkaDistributerApplication(KafkaTemplate<Integer, Object> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    public static void main(String[] args) {

        SpringApplication.run(KafkaDistributerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Path source;
        if (args.length < 1) {
            source = Path.of("kafka-distributer/src/main/resources/", "targetforsplit.csv");
        } else {
            source = Path.of(args[0]);
        }
        Splitter splitter = new SplitterFactory().create(FilenameUtils.getExtension(source.toString()));
        Map<Integer, IndexTuple> indexMap = splitter.generateIndexMap(source, 1024);
        indexMap.forEach((key, value) -> {
            LOG.info("send -> " + " key:" + key + " " + "value: " + value);
            kafkaTemplate.send("split_issues", value);
        });
    }
}
