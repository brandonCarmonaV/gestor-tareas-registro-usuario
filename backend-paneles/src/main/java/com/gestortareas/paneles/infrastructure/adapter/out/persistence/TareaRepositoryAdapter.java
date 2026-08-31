package com.gestortareas.paneles.infrastructure.adapter.out.persistence;

import com.gestortareas.paneles.domain.model.Tarea;
import com.gestortareas.paneles.domain.port.out.TareaRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TareaRepositoryAdapter implements TareaRepositoryPort {

    private final TareaJpaRepository repository;

    public TareaRepositoryAdapter(TareaJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Tarea guardar(Tarea tarea) {
        throw new UnsupportedOperationException("TODO: mapear y persistir tarea");
    }

    @Override
    public List<Tarea> buscarPorPanel(Long panelId) {
        throw new UnsupportedOperationException("TODO: mapear tareas persistidas");
    }
}
