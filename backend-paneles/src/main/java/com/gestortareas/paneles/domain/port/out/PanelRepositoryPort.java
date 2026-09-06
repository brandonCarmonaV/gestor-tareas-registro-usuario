package com.gestortareas.paneles.domain.port.out;

import rmi.shared.Panel;

import java.util.List;
import java.util.Optional;

public interface PanelRepositoryPort {
    Panel guardar(Panel panel);
    List<Panel> listarPorPropietario(String propietarioId);
    Optional<Panel> buscarPorId(String panelId);
    Panel actualizar(Panel panel);
}
