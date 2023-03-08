package com.drvnsolutions.producerservice.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class InVehicleResponse {
    private String version;
    private String deliveredAt;
    private ArrayList<InVehicleData> inVehicleData;
}
