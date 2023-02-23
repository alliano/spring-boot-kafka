package com.kafka.springkafka.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kafka.springkafka.dtos.OrderProduct;
import com.kafka.springkafka.dtos.UserregistrationDto;
import com.kafka.springkafka.messaging.KafkaPublisher;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController @AllArgsConstructor
@RequestMapping(method = RequestMethod.GET, path = "/")
public class UserController {

    private final KafkaPublisher publisher;

    @GetMapping(path="register")
    public void registration(@RequestBody UserregistrationDto user) {
        this.publisher.sendToKafka(user);
    }

    @GetMapping(path="batch")
    public void batchData(@RequestBody OrderProduct product) {
        this.publisher.batch(product);
    }

    @GetMapping(path="transaction")
    public void transactional(@RequestBody OrderProduct product) {
        this.publisher.transaction(product);
    }
}
