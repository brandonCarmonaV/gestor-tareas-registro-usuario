package com.gestortareas.paneles.infrastructure.adapter.in.rmi;

import com.gestortareas.paneles.application.service.PanelService;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

/**
 * Bootstrap para inicializar el servidor RMI.
 * 
 * Responsabilidades:
 * - Crear el registry RMI (o usar uno existente)
 * - Instanciar PanelRmiServiceImpl
 * - Registrar la implementación en el registry con nombre conocido
 * 
 * Se invoca automáticamente al inicio de la aplicación Spring (via BeanConfig.initRmiServer)
 */
@Component
public class RmiServerBootstrap {

    private static final Logger logger = Logger.getLogger(RmiServerBootstrap.class.getName());
    
    private final PanelService panelService;

    public RmiServerBootstrap(PanelService panelService) {
        this.panelService = panelService;
    }

    /**
     * Inicia el servidor RMI.
     * 
     * Flujo:
     * 1. Crea el registry RMI en el puerto especificado (o localiza uno existente)
     * 2. Crea instancia de PanelRmiServiceImpl
     * 3. Registra la instancia en el registry con el nombre "PanelService"
     * 4. Loguea éxito
     * 
     * @param registryPort puerto del registry RMI (tipicamente 1099)
     * @throws RemoteException si hay error al crear/registrar en el registry
     */
    public void start(int registryPort) throws RemoteException {
        try {
            logger.info("Iniciando servidor RMI en puerto " + registryPort + "...");
            
            // Crear o localizar registry RMI
            Registry registry = LocateRegistry.createRegistry(registryPort);
            logger.info("Registry RMI creado/localizado en puerto " + registryPort);
            
            // Crear implementación RMI
            PanelRemoteService panelRmiService = new PanelRmiServiceImpl(panelService);
            logger.info("PanelRmiServiceImpl instanciado");
            
            // Registrar en el registry con nombre conocido
            String serviceName = "PanelService"; // Debe coincidir con nombre usado por clientes
            registry.rebind(serviceName, panelRmiService);
            logger.info("PanelRemoteService registrado en RMI registry con nombre='" + serviceName + "'");
            
            logger.info("✓ Servidor RMI iniciado correctamente. Servicios disponibles:");
            logger.info("  - rmi://localhost:" + registryPort + "/" + serviceName);
            
        } catch (RemoteException ex) {
            logger.severe("Error fatal al inicializar servidor RMI: " + ex.getMessage());
            throw ex;
        }
    }
}
