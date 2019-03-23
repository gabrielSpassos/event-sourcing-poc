package com.gabrielpassos.event.producer.routes;

import com.gabrielpassos.event.producer.configs.RabbitConfig;
import com.gabrielpassos.event.producer.model.Event;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Component
public class ProducerRoute extends RouteBuilder {

    private RabbitConfig rabbitConfig;

    @Autowired
    public ProducerRoute(RabbitConfig rabbitConfig) {
        this.rabbitConfig = rabbitConfig;
    }

    @Override
    public void configure() {
        from("direct:createEvent")
                .validate(this::isEventValid)
                .marshal()
                .json(JsonLibrary.Jackson)
                .to(buildRabbitUri());
    }

    private Boolean isEventValid(Exchange exchange) {
        Event event = exchange.getIn().getBody(Event.class);
        return Optional.ofNullable(event).isPresent();
    }

    private String buildRabbitUri() {
        return UriComponentsBuilder
                .newInstance()
                .scheme("rabbitmq")
                .host(getRabbitHost())
                .queryParam("queue", rabbitConfig.getRabbitQueue())
                .queryParam("exchangeType", "direct")
                .queryParam("username", rabbitConfig.getRabbitUser())
                .queryParam("password", rabbitConfig.getRabbitPass())
                .queryParam("autoDelete", false)
                .queryParam("autoAck", false)
                .queryParam("deadLetterExchangeType", "fanout")
                .queryParam("deadLetterExchange", "events.dead")
                .queryParam("deadLetterQueue", "dead.events")
                .queryParam("deadLetterRoutingKey", "#")
                .build()
                .toString();
    }

    private String getRabbitHost() {
        return String.format("%s:%s/%s",
                rabbitConfig.getRabbitUrl(),
                rabbitConfig.getRabbitPort(),
                rabbitConfig.getRabbitExchange());
    }
}
