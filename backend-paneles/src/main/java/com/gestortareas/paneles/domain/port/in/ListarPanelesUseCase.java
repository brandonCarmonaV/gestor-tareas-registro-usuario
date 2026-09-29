package com.gestortareas.paneles.domain.port.in;

import java.util.List;

import com.gestortareas.paneles.domain.model.entity.Panel;

public interface ListarPanelesUseCase {
    List<Panel> listarPaneles(String propietarioId);
}
