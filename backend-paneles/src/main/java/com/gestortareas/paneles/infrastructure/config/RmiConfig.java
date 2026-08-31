package com.gestortareas.paneles.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rmi")
public record RmiConfig(int registryPort, String authHost, int authPort) {
}
