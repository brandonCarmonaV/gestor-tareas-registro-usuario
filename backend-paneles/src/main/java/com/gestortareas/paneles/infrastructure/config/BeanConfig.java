package com.gestortareas.paneles.infrastructure.config;

import com.gestortareas.paneles.infrastructure.adapter.in.rmi.RmiServerBootstrap;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.logging.Logger;

/**
 * Configuración central de beans de infraestructura.
 * 
 * Responsabilidades:
 * - Habilitar propiedades de configuración (RmiConfig)
 * - Inicializar el servidor RMI al arrancar Spring
 */
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

    /**
     * Inicializa el servidor RMI al completar la construcción del bean.
     * 
     * Se ejecuta automáticamente después de que Spring inyecte todas las dependencias.
     * Si falla, previene que la aplicación continúe (lanza excepción).
     * 
     * Flujo:
     * 1. Log de inicio
     * 2. Llama a rmiServerBootstrap.start() con puerto de RmiConfig
     * 3. Si todo va bien, log de éxito
     * 4. Si falla, envuelve en RuntimeException para fallar el startup
     * 
     * @throws RuntimeException si RmiServerBootstrap.start() falla
     */
    @PostConstruct
    public void initRmiServer() {
        try {
            logger.info("Inicializando configuración del servidor RMI...");
            logger.info("  Registry Port: " + rmiConfig.registryPort());
            logger.info("  Auth Host: " + rmiConfig.authHost());
            logger.info("  Auth Port: " + rmiConfig.authPort());
            
            // Iniciar el servidor RMI con el puerto configurado
            rmiServerBootstrap.start(rmiConfig.registryPort());
            
            logger.info("✓ Configuración de RMI completada exitosamente");
            
        } catch (Exception e) {
            logger.severe("✗ Error al inicializar servidor RMI: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al inicializar servidor RMI", e);
        }
    }
}
