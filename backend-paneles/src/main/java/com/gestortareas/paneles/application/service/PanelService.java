package com.gestortareas.paneles.application.service;

import com.gestortareas.paneles.domain.model.EstadoPanel;
import com.gestortareas.paneles.domain.model.Panel;
import com.gestortareas.paneles.domain.port.in.ActualizarEstadoPanelUseCase;
import com.gestortareas.paneles.domain.port.in.CrearPanelUseCase;
import com.gestortareas.paneles.domain.port.in.ListarPanelesUseCase;
import com.gestortareas.paneles.domain.port.out.AuthServicePort;
import com.gestortareas.paneles.domain.port.out.PanelRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PanelService implements CrearPanelUseCase, ListarPanelesUseCase,
        ActualizarEstadoPanelUseCase {

    private final PanelRepositoryPort panelRepository;
    private final AuthServicePort authService;

    public PanelService(PanelRepositoryPort panelRepository, AuthServicePort authService) {
        this.panelRepository = panelRepository;
        this.authService = authService;
    }

    @Override
    public Panel crearPanel(String nombre, String color, Integer prioridad,
                            LocalDate fechaInicio, LocalDate fechaFin, String propietarioId) {
        // TODO: Validar que propietarioId sea válido usando authService
        // TODO: Agregar logs de auditoría
        
        // Panel.crear() ya valida nombre y fechas según reglas de negocio
        Panel panel = Panel.crear(nombre, color, prioridad, fechaInicio, fechaFin, propietarioId);
        return panelRepository.guardar(panel);
    }

    @Override
    public List<Panel> listarPaneles(String propietarioId) {
        // TODO: Validar que propietarioId sea válido usando authService
        // TODO: Agregar logs de auditoría
        return panelRepository.listarPorPropietario(propietarioId);
    }

    @Override
    public Panel actualizarEstado(String panelId, EstadoPanel nuevoEstado) {
        // TODO: Validar que propietarioId tenga permisos para actualizar este panel
        // TODO: Agregar logs de auditoría
        
        Panel panel = panelRepository.buscarPorId(panelId)
                .orElseThrow(() -> new IllegalArgumentException("Panel no encontrado: " + panelId));
        
        // cambiarEstado() ya implementa idempotencia según reglas de negocio
        panel.cambiarEstado(nuevoEstado);
        return panelRepository.actualizar(panel);
    }
}
