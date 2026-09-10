package com.gestortareas.paneles.application.service;

import com.gestortareas.paneles.application.exception.UnauthorizedException;
import com.gestortareas.paneles.application.exception.ValidationException;
import com.gestortareas.paneles.domain.model.EstadoPanel;
import com.gestortareas.paneles.domain.model.Panel;
import com.gestortareas.paneles.domain.port.in.CrearPanelUseCase;
import com.gestortareas.paneles.domain.port.in.ListarPanelesUseCase;
import com.gestortareas.paneles.domain.port.out.PanelRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PanelService implements CrearPanelUseCase, ListarPanelesUseCase {

    private static final Logger logger = Logger.getLogger(PanelService.class.getName());
    
    private final PanelRepositoryPort panelRepository;

    public PanelService(PanelRepositoryPort panelRepository) {
        this.panelRepository = panelRepository;
    }

    @Override
    public Panel crearPanel(String nombre, String color, Integer prioridad,
                            LocalDate fechaInicio, LocalDate fechaFin, String propietarioId) {
        
        try {
            Panel panel = Panel.crear(nombre, color, prioridad, fechaInicio, fechaFin, propietarioId);
            Panel panelGuardado = panelRepository.guardar(panel);
            
            logger.info("Panel creado exitosamente: " + panelGuardado.getId() + 
                       " por propietario: " + propietarioId);
            return panelGuardado;
            
        } catch (IllegalArgumentException ex) {
            logger.warning("Validación fallida al crear panel: " + ex.getMessage());
            throw new ValidationException("Error de validación al crear panel: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Panel> listarPaneles(String propietarioId) {
        List<Panel> paneles = panelRepository.listarPorPropietario(propietarioId);
        
        logger.info("Listados " + paneles.size() + " paneles del propietario: " + propietarioId);
        return paneles;
    }

    public Panel actualizarPanel(String panelId, String nombre, String color,
                                 LocalDate fechaInicio, LocalDate fechaFin,
                                 Integer prioridad, EstadoPanel estado,
                                 String propietarioId) {
        Panel panel = panelRepository.buscarPorId(panelId)
                .orElseThrow(() -> {
                    logger.warning("Intento de actualizar panel inexistente: " + panelId);
                    return new IllegalArgumentException("Panel no encontrado: " + panelId);
                });
        if (!panel.getPropietarioId().equals(propietarioId)) {
            logger.severe("Intento de actualizar panel de otro usuario. Panel: " + panelId + 
                         ", Propietario: " + panel.getPropietarioId() + ", Usuario: " + propietarioId);
            throw new UnauthorizedException("No tienes permisos para actualizar este panel");
        }
        try {
            panel.actualizarDatos(nombre, color, fechaInicio, fechaFin, prioridad, estado);
            return panelRepository.actualizar(panel);
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Error de validación al actualizar panel: " + ex.getMessage(), ex);
        }
    }

    public void eliminarPanel(String panelId, String propietarioId) {
        Panel panel = panelRepository.buscarPorId(panelId)
                .orElseThrow(() -> new IllegalArgumentException("Panel no encontrado: " + panelId));

        if (!panel.getPropietarioId().equals(propietarioId)) {
            throw new UnauthorizedException("No tienes permisos para eliminar este panel");
        }

        panelRepository.eliminar(panelId);
    }

}
