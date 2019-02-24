package com.gabrielpassos.event.producer.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value="/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public abstract class BaseVersion {
    static final int OK = 200;
    static final String OK_MESSAGE = "Successful operation.";
    static final int CREATED = 201;
    static final String CREATED_MESSAGE = "Event successfully send to event store";
}