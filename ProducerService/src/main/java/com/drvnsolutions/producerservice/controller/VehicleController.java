package com.drvnsolutions.producerservice.controller;

import com.drvnsolutions.producerservice.model.VehicleOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.drvnsolutions.producerservice.model.Vehicle;
import com.drvnsolutions.producerservice.service.KafkaProducerService;

import java.time.Instant;
import java.util.UUID;

@RestController
public class VehicleController {

	@Autowired
	private KafkaProducerService kafkaProducerService;

	@PostMapping("/post")
	public void vehicle(@RequestBody Vehicle vehicle) {
		Integer i = 0;
		while (i < vehicle.getInVehicleResponse().getInVehicleData().size()) {
			String time = vehicle.getInVehicleResponse().getInVehicleData().get(i).getResponse().getMileage().getDataPoint().getTimestamp();
			Long odometer = (vehicle.getInVehicleResponse().getInVehicleData().get(i).getResponse().getMileage().getDataPoint().getValue()) * 1000;
			String unit = vehicle.getInVehicleResponse().getInVehicleData().get(i).getResponse().getMileage().getDataPoint().getUnit();
			String source_id = vehicle.getInVehicleResponse().getInVehicleData().get(i).getIdentifier().getType().getCarType() + "-" + vehicle.getInVehicleResponse().getInVehicleData().get(i).getIdentifier().getValue();
			String source_type = vehicle.getInVehicleResponse().getInVehicleData().get(i).getIdentifier().getType().getCarType();
			String message_id = UUID.randomUUID().toString();
			Long timestamp_millis = Instant.now().toEpochMilli();

			VehicleOutput vehicleOutput = new VehicleOutput(time, odometer, unit, source_id, source_type, message_id, timestamp_millis);

			kafkaProducerService.send(vehicleOutput);
			i++;
		}
	}

}
