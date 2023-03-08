package com.drvnsolutions.producerservice.model;

public enum Type {
    VIN("honda");

    private String car;

    Type(String carType) {
        this.car = carType;
    }

    public String getCarType() {
        return car;
    }
}
