package com.ge.digital.simplepredixservice.model;

import lombok.Data;

@Data
public class PostSensorRequestBody {

    private Integer value;

    private String deviceId;

    private String ip;

    private String type;

    private Integer sec;

    private Integer usec;
}