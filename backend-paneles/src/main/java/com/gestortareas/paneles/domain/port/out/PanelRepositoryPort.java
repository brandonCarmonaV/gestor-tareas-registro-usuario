package com.gestortareas.paneles.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.gestortareas.paneles.domain.model.entity.Panel;

public interface PanelRepositoryPort {
    List<Panel> listarPorPropietario(String propietarioId);
    Optional<Panel> buscarPorId(String panelId);
    Panel actualizar(Panel panel);
    void eliminar(String panelId);
}
