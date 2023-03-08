package com.drvsolutions.model;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class Vehicle {

	@NotNull
	private String time;
	@NotNull
	private Long odometer;
	@NotNull
	private String unit;
	@NotNull
	private String source_id;
	@NotNull
	private String source_type;
	@NotNull
	private String message_id;
	@NotNull
	private Long timestamp_millis;

}
