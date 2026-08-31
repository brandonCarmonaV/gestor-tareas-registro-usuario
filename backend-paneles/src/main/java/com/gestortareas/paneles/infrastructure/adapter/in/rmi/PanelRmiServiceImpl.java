package com.gestortareas.paneles.infrastructure.adapter.in.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PanelRmiServiceImpl extends UnicastRemoteObject implements PanelRemoteService {

    public PanelRmiServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public boolean validarPanel(Long panelId) throws RemoteException {
        throw new UnsupportedOperationException("TODO: implementar servicio remoto");
    }
}
