package com.drvnsolutions.producerservice.model;

import lombok.Data;

import java.util.Date;

@Data
public class DataPoint {
    private Long value;
    private String unit;
    private String timestamp;
}
