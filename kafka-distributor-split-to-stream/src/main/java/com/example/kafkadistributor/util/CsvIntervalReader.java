package com.example.kafkadistributor.util;
import com.example.kafkadistributor.models.Entity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

@Slf4j
@Component
public class CsvIntervalReader implements IntervalReader<Entity> {

    @Override
    public List<Entity> read(Path path, int leftIndex, int rightIndex) {

        List<Entity> resultList = new ArrayList<>();
        try (RandomAccessFile src = new RandomAccessFile(path.toFile(), "rw")) {
            int count = 0;
            log.info("Start reading interval with this params: leftIndex: {}, rightIndex: {}", leftIndex, rightIndex);
            src.seek(leftIndex);
            while (( src.getFilePointer() <= rightIndex )) {
                var tmp = src.readLine();
                if (splitString(tmp) != null) {
                    Entity t = splitString(tmp);
                    resultList.add(t);
                    assert t != null;
                    count++;
                    log.info("added new user {}. Amount - {}", t.getName(), count);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }

    static Entity splitString(String s) {

        if (s.length() < 1) {
            return null;
        }
        try {
            String[] csvFields = s.split(",");
            String name = csvFields[0];
            int count = Integer.parseInt(csvFields[1]);
            return Entity.builder()
                    .name(name)
                    .count(count)
                    .build();
        } catch (PatternSyntaxException e) {
            return null;
        }
    }
}
