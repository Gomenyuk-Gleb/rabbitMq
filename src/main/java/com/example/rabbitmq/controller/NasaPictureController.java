package com.example.rabbitmq.controller;

import com.example.rabbitmq.request.PictureRequest;
import com.example.rabbitmq.rabbit.RabbitMQPublisher;
import com.example.rabbitmq.service.NasaPictureService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RestController
@AllArgsConstructor
public class NasaPictureController {
    private final RabbitMQPublisher nasaService;
    private final NasaPictureService nasaPictureService;

    @PostMapping("/mars/pictures/largest")
    public ResponseEntity<?> postFindLargestPictureCommand(@RequestBody PictureRequest pictureRequest, HttpServletRequest httpServletRequest) {
        nasaService.nasaPublisher(pictureRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .location(URI.create(httpServletRequest.getRequestURL() + "/" + pictureRequest.getCommandId()))
                .build();
    }

    @GetMapping(value = "/mars/pictures/largest/{commandId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getLargestPicture(@PathVariable String commandId) {
        return nasaPictureService.findPictureInDB(commandId);
    }
}