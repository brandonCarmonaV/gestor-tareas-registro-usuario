package com.gestortareas.paneles.domain.port.in;

import com.gestortareas.paneles.domain.model.Panel;

import java.util.List;

public interface ListarPanelesUseCase {
    List<Panel> listarPaneles(String propietarioId);
}
