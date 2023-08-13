package com.example.kafkadistributer;

import com.example.kafkadistributer.config.KafkaCountingMessagesComponent;
import com.example.kafkadistributer.util.progressprinter.ProgressPrinter;
import com.file.dto.IndexTuple;
import com.file.splitters.Splitter;
import com.file.splitters.SplitterFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class KafkaDistributerApplication implements CommandLineRunner {

    private final KafkaTemplate<Integer, Object> kafkaTemplate;

    private final KafkaCountingMessagesComponent kafkaCountingMessagesComponent;

    private final ProgressPrinter progressPrinter;

    @Value(value = "${spring.kafka.count-topic}")
    private String countingTopic;

    @Value(value= "${path-to-file}")
    private String pathToFile;

    public static void main(String[] args) {

        SpringApplication.run(KafkaDistributerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Path source;
        if (args.length < 1) {
            source = Path.of(pathToFile);
        } else {
            source = Path.of(args[0]);
        }
        Splitter splitter = getSplitter(source);
        int countRows = getCountRows(source);
        Map<Integer, IndexTuple> indexMap = getIndexMap(source, splitter);
        var countFuture = getCountFuture(countRows);
        indexMap.forEach((key, value) -> {
            kafkaTemplate.send("split_issues", value);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        countFuture.get();

    }

    private Map<Integer, IndexTuple> getIndexMap(Path source, Splitter splitter) {

        return splitter.generateIndexMap(source, 1024);
    }

    private Splitter getSplitter(Path source) {

        return new SplitterFactory().create(FilenameUtils.getExtension(source.toString()));
    }

    private int getCountRows(Path filePath) {

        try {
            long rowCount = Files.lines(filePath).count();
            return (int) rowCount;
        } catch (IOException e) {
            throw new RuntimeException("Error while counting rows: " + e.getMessage(), e);
        }
    }

    private CompletableFuture<Long> getCountFuture(int desiredCount) {

        return CompletableFuture.supplyAsync(() -> {
            while (true) {
                long currentCount = kafkaCountingMessagesComponent.getTotalNumberOfMessagesInATopic(countingTopic);
                progressPrinter.printProgress(System.currentTimeMillis(), desiredCount, currentCount);
                if (currentCount >= desiredCount) {
                    return currentCount;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
