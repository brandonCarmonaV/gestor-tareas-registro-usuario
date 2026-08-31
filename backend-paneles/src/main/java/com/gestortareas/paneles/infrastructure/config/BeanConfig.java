package com.gestortareas.paneles.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RmiConfig.class)
public class BeanConfig {
}
