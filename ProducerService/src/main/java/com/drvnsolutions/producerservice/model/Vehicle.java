package com.drvnsolutions.producerservice.model;

import lombok.Data;

@Data
public class Vehicle {

	private String forwardedAt;
	private String subscriptionId;
	private InVehicleResponse inVehicleResponse;
	private String source_id;
	private String source_type;
	private String message_id;
	private Double timestamp_millis;

}
