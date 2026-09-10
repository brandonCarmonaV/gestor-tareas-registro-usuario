package com.gestortareas.paneles.infrastructure.config;

import com.gestortareas.paneles.infrastructure.adapter.in.rmi.RmiServerBootstrap;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.logging.Logger;

@Configuration
@EnableConfigurationProperties(RmiConfig.class)
public class BeanConfig {

    private static final Logger logger = Logger.getLogger(BeanConfig.class.getName());
    
    private final RmiServerBootstrap rmiServerBootstrap;
    private final RmiConfig rmiConfig;

    public BeanConfig(RmiServerBootstrap rmiServerBootstrap, RmiConfig rmiConfig) {
        this.rmiServerBootstrap = rmiServerBootstrap;
        this.rmiConfig = rmiConfig;
    }

    @PostConstruct
    public void initRmiServer() {
        try {
            logger.info("Inicializando configuración del servidor RMI...");
            logger.info("  Registry Port: " + rmiConfig.registryPort());
            logger.info("  Auth Host: " + rmiConfig.authHost());
            logger.info("  Auth Port: " + rmiConfig.authPort());
            
            rmiServerBootstrap.start(rmiConfig.registryPort());
            
            logger.info("✓ Configuración de RMI completada exitosamente");
            
        } catch (Exception e) {
            logger.severe("✗ Error al inicializar servidor RMI: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al inicializar servidor RMI", e);
        }
    }
}
