package com.example.rabbitmq.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public Exchange pictureExchange() {
        return new DirectExchange("nasa-pictures-exchange");
    }
    @Bean
    public Queue picturesQueue() {
        return QueueBuilder.durable("nasa-pictures-queue").build();
    }

    @Bean
    public Binding picturesQueueBinding() {
        return BindingBuilder
                .bind(picturesQueue())
                .to(pictureExchange())
                .with("")
                .noargs();
    }
}