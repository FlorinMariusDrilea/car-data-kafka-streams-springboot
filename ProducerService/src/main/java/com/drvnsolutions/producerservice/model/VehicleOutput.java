package com.drvnsolutions.producerservice.model;

import lombok.Data;

import java.util.Date;

@Data
public class VehicleOutput {
    private String time;
    private Long odometer;
    private String unit;
    private String source_id;
    private String source_type;
    private String message_id;
    private Long timestamp_millis;

    public VehicleOutput(String time, Long odometer, String unit, String source_id, String source_type, String message_id, Long timestamp_millis) {
        this.time = time;
        this.odometer = odometer;
        this.unit = unit;
        this.source_id = source_id;
        this.source_type = source_type;
        this.message_id = message_id;
        this.timestamp_millis = timestamp_millis;
    }
}
