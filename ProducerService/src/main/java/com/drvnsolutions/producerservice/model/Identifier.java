package com.drvnsolutions.producerservice.model;

import lombok.Data;

@Data
public class Identifier {
    private Type type;
    private String value;
}
