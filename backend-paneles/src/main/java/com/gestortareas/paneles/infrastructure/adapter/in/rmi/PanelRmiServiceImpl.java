package com.gestortareas.paneles.infrastructure.adapter.in.rmi;

import com.gestortareas.paneles.application.service.PanelService;
import com.gestortareas.paneles.domain.model.EstadoPanel;
import com.gestortareas.paneles.domain.model.Panel;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class PanelRmiServiceImpl extends UnicastRemoteObject implements PanelRemoteService {

    private final PanelService panelService;

    public PanelRmiServiceImpl(PanelService panelService) throws RemoteException {
        super();
        this.panelService = panelService;
    }

    @Override
    public Panel crearPanel(Panel panel) throws RemoteException {
        // TODO: Delegar a panelService.crearPanel()
        throw new UnsupportedOperationException("TODO: implementar servicio remoto");
    }

    @Override
    public List<Panel> listarPaneles(String propietarioId) throws RemoteException {
        // TODO: Delegar a panelService.listarPaneles()
        throw new UnsupportedOperationException("TODO: implementar servicio remoto");
    }

    @Override
    public Panel actualizarEstado(String panelId, EstadoPanel nuevoEstado) throws RemoteException {
        // TODO: Delegar a panelService.actualizarEstado()
        throw new UnsupportedOperationException("TODO: implementar servicio remoto");
    }
}
