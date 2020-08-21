package com.kafka.healthcheck.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaConsumer {

	@KafkaListener(topics = "${kafka.json-topic}")
	public void jsonTopicListener(ConsumerRecord<String, String> record) {
		log.info("received message:{}, key:{}", record.value(), record.key());

	}

}
