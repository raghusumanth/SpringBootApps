package com.kafka.healthcheck.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Component
@ConfigurationProperties("kafka")
public class KafkaProperties {
	private String jsonTopic;
	private String avroTopic;
	private Producer producer = new Producer();
	private Consumer consumer = new Consumer();

	@Data
	public static class Producer {
		private String bootstrapServers;
		private String clientId;
		private String acks;
		private String retries;
		private String batchSize;
		private String lingerMs;
		private String bufferMemory;
		private String keySerializer;
		private String valueJsonSerializer;
		private String valueAvroSerializer;
		private String schemaregistryUrl;
		private String interceptor;

	}

	@Data
	public static class Consumer {
		private String bootstrapServers;
		private String groupId;
		private String autoOffsetReset;
		private Integer sessionTimeoutMs;
		private Boolean enableAutoCommit;
		private Integer autoCommitIntervalMs;
		private Integer maxPollRecords;
		private Integer topicConcurrency;
		private Integer pollTimeout;
		private String clientId;
		private String schemaregistryUrl;
		private String interceptor;

	}

}
