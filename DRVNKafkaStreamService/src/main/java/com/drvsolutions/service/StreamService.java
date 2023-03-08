package com.drvsolutions.service;

import com.drvsolutions.bindings.StreamBindings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import javax.validation.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@EnableBinding(StreamBindings.class)
@Service
public class StreamService {
//	@StreamListener("vehicle-input-channel")
//	@SendTo("vehicle-output-channel")
//	public KStream<String, Vehicle> outputVehicleStreaming(KStream<String, Vehicle> vehicle) {
//		return vehicle;
//	}

	ObjectMapper mapper = new ObjectMapper();

	@StreamListener("vehicle-input-channel")
	@SendTo("vehicle-output-channel")
	public KStream<String, String> outputVehicleStreaming(KStream<String, String> vehicle) {
		KStream<String, String> streaming = vehicle.map((key, value) -> {
				try {
					JsonNode jsonNode = mapper.readTree(value);
					int i = 0;

					while (i < jsonNode.get("inVehicleResponse").get("inVehicleData").size()) {
						JsonNode inVehicleDataNode = jsonNode.get("inVehicleResponse").get("inVehicleData").get(0);

						// Validate fields
						if (!inVehicleDataNode.has("response") || inVehicleDataNode.get("response").isNull() ||
								!inVehicleDataNode.get("response").has("mileage") || inVehicleDataNode.get("response").get("mileage").isNull() ||
								!inVehicleDataNode.get("response").get("mileage").has("dataPoint") || inVehicleDataNode.get("response").get("mileage").get("dataPoint").isNull() ||
								!inVehicleDataNode.has("identifier") || inVehicleDataNode.get("identifier").isNull() ||
								!inVehicleDataNode.get("identifier").has("type") || !inVehicleDataNode.get("identifier").get("type").isTextual() ||
								!inVehicleDataNode.get("identifier").has("value") || !inVehicleDataNode.get("identifier").get("value").isTextual()) {
							throw new IllegalArgumentException("Invalid JSON format");
						}

						String vin = inVehicleDataNode.get("identifier").get("value").asText();
						String typeCar = inVehicleDataNode.get("identifier").get("type").asText();
						double mileage = inVehicleDataNode.get("response").get("mileage").get("dataPoint").get("value").asDouble();
						String unit = inVehicleDataNode.get("response").get("mileage").get("dataPoint").get("unit").asText();
						String timestampStr = inVehicleDataNode.get("response").get("mileage").get("dataPoint").get("timestamp").asText();
						Date timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'").parse(timestampStr);
						long timestampMillis = timestamp.getTime();

						String type;

						if (typeCar.equals("VIN")) {
							type = "honda";
						} else type = "No type defined correctly!";

						// validation
//					ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
//					Validator validator =  validatorFactory.getValidator();

						ObjectNode outputJsonNode = mapper.createObjectNode();
						outputJsonNode.put("time", timestampStr);
						outputJsonNode.put("odometer", (long) (mileage * 1000));
						outputJsonNode.put("unit", unit);
						outputJsonNode.put("source_id", type + "-" + vin);
						outputJsonNode.put("source_type", type);
						outputJsonNode.put("message_id", UUID.randomUUID().toString());
						outputJsonNode.put("timestamp_millis", timestampMillis);

//					Set<ConstraintViolation<Vehicle>> violations = validator.validate(outputJsonNode);
//					if(!violations.isEmpty()) {
//						throw new ConstraintViolationException(violations);
//					}
						i++;
						return KeyValue.pair(key, outputJsonNode.toString());
					}

					} catch(JsonProcessingException | ParseException | ConstraintViolationException e){
						e.printStackTrace();
					}

				return null;
		}).filter((key, value) -> value != null);
				return streaming;
	}
}
