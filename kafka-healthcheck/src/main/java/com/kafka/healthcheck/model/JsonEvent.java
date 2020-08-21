package com.kafka.healthcheck.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JsonEvent {

	private String message;
}
