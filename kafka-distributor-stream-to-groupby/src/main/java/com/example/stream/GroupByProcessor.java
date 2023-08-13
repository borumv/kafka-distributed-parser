package com.example.stream;

import com.example.stream.models.Entity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GroupByProcessor {

    private static final Serde<String> STRING_SERDE = Serdes.String();
    private static final String INPUT_TOPIC = "stream-input";
    private static final String OUTPUT_TOPIC = "stream-output";

    public Serde<Entity> entitySerde() {

        return Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(Entity.class));
    }

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {

        KStream<String, String> messageStream = streamsBuilder.stream(INPUT_TOPIC, Consumed.with(STRING_SERDE, STRING_SERDE));
        var inputStream = messageStream
                .mapValues(this::getUserFromString)
                .peek((k, v) -> {
                    System.out.println("Key - " + k);
                    System.out.println("Value - " + v);
                });
        var GroupByKeyStream = inputStream.groupByKey();
        var groupBy = GroupByKeyStream.reduce((value1, value2) -> {
                                                  log.info("we are trying to work in reduce with name -> {} {}", value2, value1);
                                                  return Entity.builder()
                                                          .name(value2.getName())
                                                          .count(value1.getCount() + value2.getCount())
                                                          .build();
                                              }
                ,
                                              Materialized.with(Serdes.String(), entitySerde()));
        groupBy.toStream().to(OUTPUT_TOPIC, Produced.with(STRING_SERDE, entitySerde()));
    }

    public ObjectMapper objectMapper() {

        return new ObjectMapper();
    }

    Entity getUserFromString(String userString) {

        Entity user = null;
        try {
            user = objectMapper().readValue(userString, Entity.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return user;
    }
}
