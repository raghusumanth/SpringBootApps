package com.kafka.healthcheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class KafkHealthCheckApplication {
	public static void main(String[] args) {
		SpringApplication.run(KafkHealthCheckApplication.class, args);
		log.info("KafkHealthCheckApplication started successfully");

	}
}
