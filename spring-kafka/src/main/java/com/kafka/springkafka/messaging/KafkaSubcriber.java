package com.kafka.springkafka.messaging;

import java.util.Arrays;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import lombok.extern.log4j.Log4j2;

@Configuration @Log4j2
public class KafkaSubcriber {

	@KafkaListener(topics = {"userRegistration"}, id = "userregistration1")
	public void subcriber(String... data){
		for(String d : data){
			log.info(d);
		}
	}


	
	@KafkaListener(topics = {"userRegistration"}, id = "userregistration2", topicPattern = "userRegistration1")
	public void listener(
	 @Header(name = KafkaHeaders.RECEIVED_KEY, required= false) String key,
	 @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
	 @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
	 @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
	 @Payload String... data){
		log.info(Arrays.toString(data));
		log.info("Key = "+key);
		log.info("Partition = "+partition);
		log.info("Topic = "+topic);
		log.info("timestamp = "+timestamp);
	}


	// this method will subcribe from example-for-batch-listener topic if the data alredy more than 10
	// the configure of batch listener can see in KafkaConfiguration.class
	@KafkaListener(topics = {"example-for-batch-listener"}, 
					id = "batchListener", containerFactory = "batchFactory",
					properties = {ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG+"=60000",
					ConsumerConfig.MAX_POLL_RECORDS_CONFIG+"=10", 
					ConsumerConfig.GROUP_ID_CONFIG+"=batch"})
	public void batchListener(java.util.List<String> messageList){
		System.out.println(messageList.size());
	}


}
