//package com.example.kafkadistributor;
//import com.example.kafkadistributor.models.Entity;
//import com.file.dto.IndexTuple;
//import org.apache.kafka.clients.consumer.Consumer;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.config.KafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
//import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.TestPropertySource;
//
//import java.nio.file.Path;
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest
//@TestPropertySource(
//        properties = {
//                "spring.kafka.consumer.auto-offset-reset=earliest"
//        }
//)
//@Testcontainers
//class KafkaListenersIT {
//
//    @Autowired
//    KafkaTemplate<Integer, Object> kafkaTemplate;
////    @Autowired
////    private KafkaConsumer consumer;
//
//    IndexTuple tuple;
//    List<Entity> entityList;
//
//    @BeforeEach
//    void setUp() {
//
//        tuple = new IndexTuple(0, 4444, Path.of("example/to/path"));
//        entityList.add(Entity.builder().name("Entity1").count(10).build());
//        entityList.add(Entity.builder().name("Entity2").count(20).build());
//        entityList.add(Entity.builder().name("Entity3").count(30).build());
//        entityList.add(Entity.builder().name("Entity4").count(40).build());
//        entityList.add(Entity.builder().name("Entity5").count(50).build());
//
//    }
//
//    @Value("${test.topic}")
//    private String topic;
//
//    @Test
//    public void givenEmbeddedKafkaBroker_whenSendingWithSimpleProducer_thenMessageReceived()
//            throws Exception {
//
//        String data = "Sending with our own simple KafkaProducer";
//        kafkaTemplate.send(topic, data);
//        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
//        assertTrue(messageConsumed);
//        assertThat(consumer.getPayload(), containsString(data));
//    }
//
//    @TestConfiguration
//    static class KafkaTestContainersConfiguration {
//
//        @Value("${spring.kafka.bootstrap-servers}")
//        private String bootstrapServers;
//        public Map<String, Object> consumerConfig() {
//
//            Map<String, Object> props = new HashMap<>();
//            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//            props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//            return props;
//        }
//
//        @Bean
//        public ConsumerFactory<String, String> consumerFactory() {
//
//            return new DefaultKafkaConsumerFactory<>(consumerConfig());
//        }
//
//        @Bean
//        public KafkaListenerContainerFactory<
//                ConcurrentMessageListenerContainer<String, String>> factory(
//                ConsumerFactory<String, String> consumerFactory
//        )
//        {
//
//            ConcurrentKafkaListenerContainerFactory<String, String> factory =
//                    new ConcurrentKafkaListenerContainerFactory<>();
//            factory.setConsumerFactory(consumerFactory);
//            return factory;
//        }
//    }
//
//}