package com.gestortareas.paneles.infrastructure.config;

import java.util.logging.Logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {
    private static final Logger logger = Logger.getLogger(BeanConfig.class.getName());

    @Bean
    public RestTemplate restTemplate() {
        logger.info("Instanciando Rest Template...");
        return new RestTemplate();
    }
}
