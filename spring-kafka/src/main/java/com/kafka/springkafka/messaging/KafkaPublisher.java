package com.kafka.springkafka.messaging;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.springkafka.dtos.OrderProduct;
import com.kafka.springkafka.dtos.UserregistrationDto;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor @Configuration
public class KafkaPublisher {

    private final KafkaTemplate<String, String> kafkaTemlate;


    @Transactional
    public void sendToKafka(UserregistrationDto user) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(user);
            CompletableFuture<SendResult<String,String>> future = this.kafkaTemlate.send("userRegistration", UUID.randomUUID().toString(), json);
            future.whenComplete((result, exception) -> {
                if (exception == null) {
                    log.info("message succes publish");
                }
                else {
                    log.error("fail publish a message");
                    log.error(exception.getMessage());
                }
            });

        } catch(JsonProcessingException JPX) {
            log.error(JPX.getMessage());
        }
    }

    @Transactional
    public void sendToKafka(Object... object) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(objectMapper);
            CompletableFuture<SendResult<String,String>> future = this.kafkaTemlate.send("userRegistration", UUID.randomUUID().toString(), json);
            future.whenComplete((result, exception) -> {
                if (exception == null) {
                    log.info("message succes publish");
                }
                else {
                    log.error("fail publish a message");
                    log.error(exception.getMessage());
                }
            });

        } catch(JsonProcessingException JPX) {
            log.error(JPX.getMessage());
        }
    }

    
    //this method to represent bathch Listener
    @Transactional
    public void batch(OrderProduct product) {
        try {
            for (int i = 0; i < 100; i++) {
                String message = new ObjectMapper().writeValueAsString(product);
                CompletableFuture<SendResult<String,String>> future = this.kafkaTemlate.send("example-for-batch-listener", UUID.randomUUID().toString(), message);
                future.whenComplete((result, exception) -> {
                    if (exception == null) {
                        log.info("message succes publish");
                    }
                    else {
                        log.error("fail publish a message");
                        log.error(exception.getMessage());
                    }
                });  
            }
        } catch (JsonProcessingException JPX) {
            log.error(JPX.getMessage());
            JPX.printStackTrace();

        }
    }

    //example for kafka transactional
    @Transactional
    public void transaction(OrderProduct product) {

        try {
            this.kafkaTemlate.send("example-for-kafka-transaction", UUID.randomUUID().toString(), new ObjectMapper().writeValueAsString(product))
            .whenComplete( (result, exception) -> {
                if(exception == null) {
                    log.info("success publish a message");
                }
                else {
                    log.error("fail publish a message the transaction manager will rollback");
                }
            });
        } catch (JsonProcessingException JPX) {
            log.error(JPX.getMessage());
            JPX.printStackTrace();
        }
    }
}
