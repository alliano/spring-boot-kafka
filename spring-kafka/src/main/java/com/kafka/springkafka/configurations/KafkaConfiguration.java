package com.kafka.springkafka.configurations;

// import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.KafkaAdmin.NewTopics;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.transaction.KafkaTransactionManager;

// import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;

// import org.springframework.kafka.core.MicrometerProducerListener;


@Configuration @EnableKafka @AllArgsConstructor
public class KafkaConfiguration {

    // private final MeterRegistry meterRegistry;

    // ini untuk mengkonfigurasi kafka admin
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return new KafkaAdmin(props);
    }

    //bean in untuk membuat topic saat applikasi pertama dinyalakan
    @Bean
    public KafkaAdmin.NewTopics topics() {
        return new NewTopics(
            TopicBuilder.name("userRegistration")
            .partitions(3)
            .replicas(1)
            .build(),
            TopicBuilder.name("otpCode")
            .replicas(1)
            .partitions(3)
            .build(),
            TopicBuilder.name("example-for-batch-listener")
            .partitions(3)
            .replicas(1)
            .build(),
            TopicBuilder.name("example-for-kafka-transaction")
            .partitions(3)
            .replicas(1)
            .build()
        );
    }

    @Bean
    public ProducerFactory<String, String> producerFactory(){
        DefaultKafkaProducerFactory<String,String> factory = new DefaultKafkaProducerFactory<String, String>(producerConfig());
        //set transaction id prefix nya => depend on producer config
        factory.setTransactionIdPrefix("__tx--");
      
        // factory.addListener(new MicrometerProducerListener<String, String>(meterRegistry(), Collections.singletonList("customTag", "customValue")));
        return factory;
    }

    @Bean
    public KafkaTransactionManager<String, String> kafkaTransactionManager() {
        KafkaTransactionManager<String,String> kafkaTransactionManager = new KafkaTransactionManager<String, String>(producerFactory());
        return kafkaTransactionManager;
    }

    @Bean
    public Map<String, Object> producerConfig() {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // membuat id transaction prefix
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "__tx-");
        return props;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(){
        return new KafkaTemplate<String, String>(producerFactory());
    }


    // config for kafka subcriber
    @Bean
    public KafkaListenerContainerFactory
        <ConcurrentMessageListenerContainer<String, String>> kafkaContainerListenerFactory() {
            ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
            factory.setConsumerFactory(consumerFactory());
            return factory;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory(){
        return new DefaultKafkaConsumerFactory<String, String>(consumerConfig());
    }

    @Bean
    public Map<String, Object> consumerConfig() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        //konfig untuk transaction
        configMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        configMap.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        return configMap;
    }

    @Bean
    public KafkaListenerContainerFactory<?> batchFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(true);
        return factory;
    }


}
