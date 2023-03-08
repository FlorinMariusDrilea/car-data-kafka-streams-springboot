package com.drvnsolutions.producerservice.service;

import com.drvnsolutions.producerservice.model.VehicleOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class KafkaProducerService {

	private static final String TOPIC = "input-vehicle-info";

	@Autowired
	private KafkaTemplate<String, VehicleOutput> kafkaTemplate;

	public void send(VehicleOutput vehicleOutput) {
		log.info("Vehicle object that was sent is {}", vehicleOutput);
		kafkaTemplate.send(TOPIC, vehicleOutput);
	}

}
