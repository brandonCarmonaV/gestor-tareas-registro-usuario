package com.gestortareas.paneles.infrastructure.adapter.in.rmi;

import rmi.shared.PanelRemoteService;

import com.gestortareas.paneles.application.service.PanelService;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

@Component
public class RmiServerBootstrap {

    private static final Logger logger = Logger.getLogger(RmiServerBootstrap.class.getName());
    
    private final PanelService panelService;

    public RmiServerBootstrap(PanelService panelService) {
        this.panelService = panelService;
    }

    public void start(int registryPort) throws RemoteException {
        try {
            logger.info("Iniciando servidor RMI en puerto " + registryPort + "...");
            
            Registry registry = LocateRegistry.createRegistry(registryPort);
            logger.info("Registry RMI creado/localizado en puerto " + registryPort);
            
            PanelRemoteService panelRmiService = new PanelRmiServiceImpl(panelService);
            logger.info("PanelRmiServiceImpl instanciado");
            
            String serviceName = "PanelService";
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
