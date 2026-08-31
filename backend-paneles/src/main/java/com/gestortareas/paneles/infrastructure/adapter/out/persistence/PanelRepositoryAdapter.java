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
        // TODO: Mapear Panel a PanelEntity y persistir usando repository.save()
        throw new UnsupportedOperationException("TODO: mapear y persistir panel");
    }

    @Override
    public List<Panel> listarPorPropietario(String propietarioId) {
        // TODO: Usar repository.findByPropietarioId() y mapear resultado a List<Panel>
        throw new UnsupportedOperationException("TODO: mapear paneles persistidos");
    }

    @Override
    public Optional<Panel> buscarPorId(String panelId) {
        // TODO: Usar repository.findById() y mapear resultado a Optional<Panel>
        throw new UnsupportedOperationException("TODO: mapear panel persistido");
    }

    @Override
    public Panel actualizar(Panel panel) {
        // TODO: Mapear Panel a PanelEntity y persistir usando repository.save()
        throw new UnsupportedOperationException("TODO: mapear y actualizar panel");
    }
}
