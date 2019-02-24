package com.gabrielpassos.event.producer.beans;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModuleMapperBean {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
