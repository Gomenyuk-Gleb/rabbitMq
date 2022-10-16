package com.example.rabbitmq.request;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data
public class PictureRequest {
    private Integer sol;
    private String camera;
    private String commandId = RandomStringUtils.randomAlphabetic(4);
}
