package com.gestortareas.paneles.application.service;

import com.gestortareas.paneles.domain.model.EstadoPanel;
import com.gestortareas.paneles.domain.model.Panel;
import com.gestortareas.paneles.domain.model.Tarea;
import com.gestortareas.paneles.domain.port.in.ActualizarEstadoPanelUseCase;
import com.gestortareas.paneles.domain.port.in.CrearPanelUseCase;
import com.gestortareas.paneles.domain.port.in.CrearTareaUseCase;
import com.gestortareas.paneles.domain.port.in.ListarPanelesUseCase;
import com.gestortareas.paneles.domain.port.in.ListarTareasUseCase;
import com.gestortareas.paneles.domain.port.out.PanelRepositoryPort;
import com.gestortareas.paneles.domain.port.out.TareaRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PanelService implements CrearPanelUseCase, ListarPanelesUseCase,
        ActualizarEstadoPanelUseCase, CrearTareaUseCase, ListarTareasUseCase {

    private final PanelRepositoryPort panelRepository;
    private final TareaRepositoryPort tareaRepository;

    public PanelService(PanelRepositoryPort panelRepository, TareaRepositoryPort tareaRepository) {
        this.panelRepository = panelRepository;
        this.tareaRepository = tareaRepository;
    }

    @Override
    public Panel crear(Panel panel) {
        throw new UnsupportedOperationException("TODO: implementar creacion de panel");
    }

    @Override
    public List<Panel> listarPorPropietario(Long propietarioId) {
        throw new UnsupportedOperationException("TODO: implementar listado de paneles");
    }

    @Override
    public Panel actualizarEstado(Long panelId, EstadoPanel estado) {
        throw new UnsupportedOperationException("TODO: implementar actualizacion de estado");
    }

    @Override
    public Tarea crear(Tarea tarea) {
        throw new UnsupportedOperationException("TODO: implementar creacion de tarea");
    }

    @Override
    public List<Tarea> listarPorPanel(Long panelId) {
        throw new UnsupportedOperationException("TODO: implementar listado de tareas");
    }
}
