package com.gestortareas.paneles.infrastructure.adapter.in.rmi;

import com.gestortareas.paneles.application.exception.UnauthorizedException;
import com.gestortareas.paneles.application.exception.ValidationException;
import com.gestortareas.paneles.application.service.PanelService;
import com.gestortareas.paneles.domain.model.Panel;
import rmi.shared.PanelRemoteService;
import rmi.shared.RmiPanelData;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PanelRmiServiceImpl extends UnicastRemoteObject implements PanelRemoteService {

    private static final Logger logger = Logger.getLogger(PanelRmiServiceImpl.class.getName());
    
    private final PanelService panelService;

    public PanelRmiServiceImpl(PanelService panelService) throws RemoteException {
        super();
        this.panelService = panelService;
    }

    @Override
    public RmiPanelData crearPanel(RmiPanelData panel) throws RemoteException {
        try {
            if (panel == null) {
                throw new IllegalArgumentException("Panel no puede ser null");
            }

            logger.info("RMI: Creando panel con nombre='" + panel.getNombre()
                    + "' propietarioId='" + panel.getPropietarioId() + "'");

            Panel panelCreado = panelService.crearPanel(
                    panel.getNombre(),
                    panel.getColor(),
                    panel.getPrioridad(),
                    panel.getFechaInicio(),
                    panel.getFechaFin(),
                    panel.getPropietarioId());

            return toRmiPanelData(panelCreado);
        } catch (ValidationException | UnauthorizedException ex) {
            logger.severe("Error en RMI crearPanel: " + ex.getMessage());
            throw new RemoteException(ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            logger.warning("Validación fallida en RMI crearPanel: " + ex.getMessage());
            throw new RemoteException(ex.getMessage(), ex);
        }
    }

    @Override
    public List<RmiPanelData> listarPaneles(String propietarioId) throws RemoteException {
        try {
            if (propietarioId == null || propietarioId.trim().isEmpty()) {
                throw new IllegalArgumentException("propietarioId no puede ser null o vacío");
            }
            
            logger.info("RMI: Listando paneles del propietario='" + propietarioId + "'");
            
            List<Panel> paneles = panelService.listarPaneles(propietarioId);
            
            logger.info("RMI: Retornando " + paneles.size() + " paneles");
            return paneles.stream()
                    .map(this::toRmiPanelData)
                    .collect(Collectors.toList());
            
        } catch (UnauthorizedException ex) {
            logger.severe("Autorización fallida en RMI listarPaneles: " + ex.getMessage());
            throw new RemoteException(ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            logger.warning("Validación fallida en RMI listarPaneles: " + ex.getMessage());
            throw new RemoteException(ex.getMessage(), ex);
        }
    }

    private RmiPanelData toRmiPanelData(Panel panel) {
        return new RmiPanelData(
                panel.getId(),
                panel.getNombre(),
                panel.getColor(),
                panel.getEstado().name(),
                panel.getFechaInicio(),
                panel.getFechaFin(),
                panel.getPrioridad(),
                panel.getPropietarioId(),
                panel.getFechaCreacion());
    }

}
