package com.kafka.healthcheck.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kafka.healthcheck.producer.MessageProducer;

@RestController
public class KafkaHealthCheckController {

	@Autowired
	private MessageProducer messageProducer;

	@GetMapping("/post")
	public ResponseEntity<String> postMessage(@RequestParam("type") String type) {

		if (StringUtils.equalsIgnoreCase(type, "avro")) {
			messageProducer.avroMessageProducer();
		} else {
			messageProducer.jsonMessageProducer();
		}
		return new ResponseEntity<>("MessagePosted Successfully", HttpStatus.OK);
	}

}
