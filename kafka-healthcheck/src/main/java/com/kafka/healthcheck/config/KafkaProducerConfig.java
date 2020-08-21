package com.kafka.healthcheck.config;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kafka.healthcheck.model.AvroEvent;
import com.kafka.healthcheck.properties.KafkaProperties;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;

@Configuration
public class KafkaProducerConfig {

	@Autowired
	private KafkaProperties kafkaProperties;

	private KafkaProducer<String, String> kafkaJsonProducer;

	private KafkaProducer<String, AvroEvent> kafkaAvroProducer;

	@Bean(name = "kafkaAvroProducer")
	public KafkaProducer<String, AvroEvent> kafkaProducer() {
		Properties props = getDefaultProperties();
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getValueAvroSerializer());
		props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProperties.getProducer().getClientId());
		props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
				kafkaProperties.getProducer().getSchemaregistryUrl());
		props.put(AbstractKafkaAvroSerDeConfig.AUTO_REGISTER_SCHEMAS, true);
		kafkaAvroProducer = new KafkaProducer<String, AvroEvent>(props);
		return kafkaAvroProducer;
	}

	@Bean(name = "kafkaJsonProducer")
	public KafkaProducer<String, String> kafkaJSONProducer() {
		Properties props = getDefaultProperties();
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getValueJsonSerializer());
		kafkaJsonProducer = new KafkaProducer<String, String>(props);
		return kafkaJsonProducer;
	}

	public Properties getDefaultProperties() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getProducer().getBootstrapServers());
		props.put(ProducerConfig.ACKS_CONFIG, kafkaProperties.getProducer().getAcks());
		props.put(ProducerConfig.RETRIES_CONFIG, kafkaProperties.getProducer().getRetries());
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProperties.getProducer().getBatchSize());
		props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProperties.getProducer().getLingerMs());
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProperties.getProducer().getBufferMemory());
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getKeySerializer());
		props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, kafkaProperties.getProducer().getInterceptor());
		return props;
	}

}
