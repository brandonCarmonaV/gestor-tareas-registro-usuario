package com.gestortareas.paneles.infrastructure.adapter.in.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PanelRemoteService extends Remote {
    String PING = "PANEL_SERVICE";

    boolean validarPanel(Long panelId) throws RemoteException;
}
