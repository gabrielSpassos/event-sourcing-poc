package com.gabrielpassos.event.producer.controllers;

import com.gabrielpassos.event.producer.controllers.dto.EventDTO;
import com.gabrielpassos.event.producer.model.Event;
import com.gabrielpassos.event.producer.service.ProducerService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.stream.Stream;

@RestController
public class EventController extends BaseVersion {

    private ModelMapper modelMapper;
    private ProducerService producerService;

    @Autowired
    public EventController(ModelMapper modelMapper, ProducerService producerService) {
        this.modelMapper = modelMapper;
        this.producerService = producerService;
    }

    @PostMapping(value = "/events")
    @ApiOperation(value = "This endpoint create's an event")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "eventDTO", value = "New event.",
                    dataType = "EventDTO", paramType = "body"),
    })
    @ApiResponses(value = {
            @ApiResponse(code = CREATED, message = CREATED_MESSAGE, response = EventDTO.class),
    })
    public ResponseEntity<?> createEvent(@RequestBody @Valid EventDTO eventDTO) {
        return Stream.of(eventDTO)
                .map(this::convertDTOToEvent)
                .map(event -> producerService.sendEventToStore(event))
                .map(this::convertModelToDTO)
                .map(ResponseEntity::ok)
                .findFirst()
                .get();
    }

    private Event convertDTOToEvent(EventDTO eventDTO) {
        return modelMapper.map(eventDTO, Event.class);
    }

    private EventDTO convertModelToDTO (Event event) {
        return modelMapper.map(event, EventDTO.class);
    }
}
