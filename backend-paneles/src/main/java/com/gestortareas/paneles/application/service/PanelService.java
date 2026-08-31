package com.gestortareas.paneles.application.service;

import com.gestortareas.paneles.domain.model.EstadoPanel;
import com.gestortareas.paneles.domain.model.Panel;
import com.gestortareas.paneles.domain.port.in.ActualizarEstadoPanelUseCase;
import com.gestortareas.paneles.domain.port.in.CrearPanelUseCase;
import com.gestortareas.paneles.domain.port.in.ListarPanelesUseCase;
import com.gestortareas.paneles.domain.port.out.AuthServicePort;
import com.gestortareas.paneles.domain.port.out.PanelRepositoryPort;
import org.springframework.stereotype.Service;

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
    public Panel crearPanel(Panel panel) {
        // TODO: Validar que el nombre no esté vacío antes de crear
        // TODO: Validar que el panel tenga todos los campos obligatorios
        // TODO: Usar authService para validar que el usuario tenga permisos
        return panelRepository.guardar(panel);
    }

    @Override
    public List<Panel> listarPaneles(String propietarioId) {
        // TODO: Validar que el propietarioId sea válido
        // TODO: Usar authService para validar que el usuario tenga permisos
        return panelRepository.listarPorPropietario(propietarioId);
    }

    @Override
    public Panel actualizarEstado(String panelId, EstadoPanel nuevoEstado) {
        // TODO: Validar que el panelId sea válido
        // TODO: Validar que el nuevoEstado sea un estado válido (lanzar excepción si no)
        // TODO: Usar authService para validar que el usuario tenga permisos
        Panel panel = panelRepository.buscarPorId(panelId)
                .orElseThrow(() -> new IllegalArgumentException("Panel no encontrado: " + panelId));
        panel.setEstado(nuevoEstado);
        return panelRepository.actualizar(panel);
    }
}
