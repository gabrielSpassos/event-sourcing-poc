package com.gabrielpassos.event.producer.configs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class RabbitConfig {

    @Value("${rabbit.url}")
    private String rabbitUrl;
    @Value("${rabbit.port}")
    private String rabbitPort;
    @Value("${rabbit.exchange}")
    private String rabbitExchange;
    @Value("${rabbit.queue}")
    private String rabbitQueue;
    @Value("${rabbit.user}")
    private String rabbitUser;
    @Value("${rabbit.pass}")
    private String rabbitPass;
}
