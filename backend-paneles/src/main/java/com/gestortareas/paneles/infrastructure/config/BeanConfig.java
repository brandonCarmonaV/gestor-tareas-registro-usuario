package com.gestortareas.paneles.infrastructure.config;

import com.gestortareas.paneles.infrastructure.adapter.in.rmi.RmiServerBootstrap;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RmiConfig.class)
public class BeanConfig {

    private final RmiServerBootstrap rmiServerBootstrap;
    private final RmiConfig rmiConfig;

    public BeanConfig(RmiServerBootstrap rmiServerBootstrap, RmiConfig rmiConfig) {
        this.rmiServerBootstrap = rmiServerBootstrap;
        this.rmiConfig = rmiConfig;
    }

    @PostConstruct
    public void initRmiServer() {
        try {
            // TODO: Iniciar el servidor RMI con el puerto configurado
            rmiServerBootstrap.start(rmiConfig.registryPort());
        } catch (Exception e) {
            // TODO: Manejar errores de inicialización del RMI
            throw new RuntimeException("Error al inicializar servidor RMI", e);
        }
    }
}
