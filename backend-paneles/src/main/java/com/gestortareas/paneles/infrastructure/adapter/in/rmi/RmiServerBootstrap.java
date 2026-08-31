package com.gestortareas.paneles.infrastructure.adapter.in.rmi;

import org.springframework.stereotype.Component;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;

@Component
public class RmiServerBootstrap {

    public void start(int registryPort) throws RemoteException {
        // TODO: registrar PanelRemoteService en el registry.
        Registry registry = LocateRegistry.createRegistry(registryPort);
    }
}
