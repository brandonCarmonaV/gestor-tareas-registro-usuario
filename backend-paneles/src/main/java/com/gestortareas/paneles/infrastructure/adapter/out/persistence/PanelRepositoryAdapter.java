package com.gestortareas.paneles.infrastructure.adapter.out.persistence;

import com.gestortareas.paneles.domain.model.Panel;
import com.gestortareas.paneles.domain.port.out.PanelRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PanelRepositoryAdapter implements PanelRepositoryPort {

    private final PanelJpaRepository repository;

    public PanelRepositoryAdapter(PanelJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Panel guardar(Panel panel) {
        throw new UnsupportedOperationException("TODO: mapear y persistir panel");
    }

    @Override
    public List<Panel> buscarPorPropietario(Long propietarioId) {
        throw new UnsupportedOperationException("TODO: mapear paneles persistidos");
    }

    @Override
    public Optional<Panel> buscarPorId(Long panelId) {
        throw new UnsupportedOperationException("TODO: mapear panel persistido");
    }
}
