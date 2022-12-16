package com.company.mapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper createModelMapper(List<Converter> converterList) {
        ModelMapper modelMapper = new ModelMapper();
        converterList.forEach(modelMapper::addConverter);
        return modelMapper;
    }
}
