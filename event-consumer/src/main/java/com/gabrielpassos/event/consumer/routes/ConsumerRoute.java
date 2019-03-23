package com.gabrielpassos.event.consumer.routes;

import com.gabrielpassos.event.consumer.config.RabbitConfig;
import com.gabrielpassos.event.consumer.model.Event;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ConsumerRoute extends RouteBuilder {

    private RabbitConfig rabbitConfig;

    @Autowired
    public ConsumerRoute(RabbitConfig rabbitConfig) {
        this.rabbitConfig = rabbitConfig;
    }

    @Override
    public void configure() {
        from(getFaturamentoNonameRabbitUri())
                .routeId("eventConsumer")
                .log(LoggingLevel.INFO, "Consuming event from rabbit queue")
                .unmarshal()
                .json(JsonLibrary.Jackson, Event.class)
                .log(LoggingLevel.INFO, "Body: ${body}.")
                .process(this::fetchEvent)
                .end();
    }

    private String getFaturamentoNonameRabbitUri() {
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
                .queryParam("requestedHeartbeat", rabbitConfig.getRequestedHeartbeat())
                .queryParam("automaticRecoveryEnabled", true)
                .queryParam("deadLetterExchangeType", "fanout")
                .queryParam("deadLetterExchange", "events.dead")
                .queryParam("deadLetterQueue", "dead.events")
                .queryParam("deadLetterRoutingKey", "#")
                .build()
                .toString();
    }

    private void fetchEvent(Exchange exchange) {
        Event event = exchange.getIn().getBody(Event.class);
        System.out.println("Consumed: " + event);
    }

    private String getRabbitHost() {
        return String.format("%s:%s/%s",
                rabbitConfig.getRabbitUrl(),
                rabbitConfig.getRabbitPort(),
                rabbitConfig.getRabbitExchange());
    }
}
