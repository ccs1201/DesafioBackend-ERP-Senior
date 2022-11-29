package com.ccs.erp.core.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Produtor do bean ModelMapper</p>
 * <p>
 * Um ModelMapper é responsável pela transformação
 * de entidades para modelos de representação e vice-versa.
 * </p>
 */
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

}
