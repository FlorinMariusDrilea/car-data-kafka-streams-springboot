package com.drvsolutions.bindings;

import com.drvsolutions.model.Vehicle;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

public interface StreamBindings {

	@Input("vehicle-input-channel")
	KStream<String, Vehicle> inputStream();

	@Output("vehicle-output-channel")
	KStream<String, Vehicle> outputStream();

}
