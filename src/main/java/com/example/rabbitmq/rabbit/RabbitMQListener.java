package com.example.rabbitmq.rabbit;

import com.example.rabbitmq.request.PictureRequest;
import com.example.rabbitmq.service.NasaPictureService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {
    @Autowired
    private NasaPictureService nasaPictureService;

    @RabbitListener(queues = "pictures-queue")
    public void processMessages(PictureRequest pictureRequest) throws JsonProcessingException {
        var nasaPictureBytes = nasaPictureService.findLargePicture(pictureRequest);
        nasaPictureService.addPictureToDB(pictureRequest.getCommandId(), nasaPictureBytes);
    }
}
