package com.gabrielpassos.event.producer.service;

import com.gabrielpassos.event.producer.configs.RabbitConfig;
import com.gabrielpassos.event.producer.model.Event;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    private RabbitTemplate rabbitTemplate;
    private RabbitConfig rabbitConfig;

    @Autowired
    public ProducerService(RabbitTemplate rabbitTemplate, RabbitConfig rabbitConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitConfig = rabbitConfig;
    }

    public Event sendEventToStore(Event event) {
        rabbitTemplate.convertAndSend(
                rabbitConfig.getExchange(),
                rabbitConfig.getRoutingKey(),
                event.toString()
        );
        return event;
    }
}
