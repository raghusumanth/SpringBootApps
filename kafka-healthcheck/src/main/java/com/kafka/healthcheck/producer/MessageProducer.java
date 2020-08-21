package com.kafka.healthcheck.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.kafka.healthcheck.model.AvroEvent;
import com.kafka.healthcheck.model.JsonEvent;
import com.kafka.healthcheck.properties.KafkaProperties;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class MessageProducer {

	@Autowired
	@Qualifier("kafkaJsonProducer")
	private KafkaProducer<String, String> kafkaJSONProducer;

	@Autowired
	@Qualifier("kafkaAvroProducer")
	private KafkaProducer<String, AvroEvent> kafkaAvroProducer;

	@Autowired
	private KafkaProperties kafkaProperties;

	@Autowired
	private Gson gson;

	public void jsonMessageProducer() {
		  JsonEvent jsonEvent=JsonEvent.builder().message("JsonEvent").build();
		  ProducerRecord<String, String> jsonRecord=new ProducerRecord<String, String>(kafkaProperties.getJsonTopic(),gson.toJson(jsonEvent));
		  kafkaJSONProducer.send(jsonRecord,(metadata,exception)->{
			  if(exception==null) {
				  log.info("JsonMessage Published Successfully,{}",metadata.partition(),metadata.offset(),jsonRecord.topic(),jsonRecord.value());
			  }else {
				  log.error("failed to send message to kafka topic: {},{}",jsonRecord.topic(),jsonRecord.value());
			  }
		  });
	  }

	public void avroMessageProducer() {
		  AvroEvent avroEvent=AvroEvent.newBuilder().setMessage("AvroMessage").setIds(100).setHello("Hello World").build();
		  ProducerRecord<String, AvroEvent> avroRecord=new ProducerRecord<String, AvroEvent>(kafkaProperties.getAvroTopic(),avroEvent);
		  kafkaAvroProducer.send(avroRecord,(metadata,exception)->{
			  if(exception==null) {
				  log.info("Avro Message Published Successfully: {}",metadata.partition(),metadata.offset(),avroRecord.topic());
			  }else {
				  log.error("failed to send message to kafka topic: {},{}",avroRecord.topic(),avroRecord.value());
			  }
		  });
	  }

}
