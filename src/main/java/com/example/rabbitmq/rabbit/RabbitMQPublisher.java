package com.example.rabbitmq.rabbit;

import com.example.rabbitmq.request.PictureRequest;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RabbitMQPublisher {

    private RabbitTemplate rabbitTemplate;

    public void nasaPublisher(PictureRequest pictureRequest) {
        rabbitTemplate.convertAndSend("pictures-direct-exchange", "", pictureRequest);
    }

}