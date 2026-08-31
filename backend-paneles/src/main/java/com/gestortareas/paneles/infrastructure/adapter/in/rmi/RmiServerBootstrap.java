package com.gestortareas.paneles.infrastructure.adapter.in.rmi;

import com.gestortareas.paneles.application.service.PanelService;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@Component
public class RmiServerBootstrap {

    private final PanelService panelService;

    public RmiServerBootstrap(PanelService panelService) {
        this.panelService = panelService;
    }

    public void start(int registryPort) throws RemoteException {
        // TODO: Crear PanelRmiServiceImpl con panelService
        // TODO: Registrar PanelRmiServiceImpl en el registry con nombre "PanelService"
        Registry registry = LocateRegistry.createRegistry(registryPort);
    }
}
