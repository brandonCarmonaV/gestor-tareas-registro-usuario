package com.gestortareas.paneles.domain.port.in;

import com.gestortareas.paneles.domain.model.Tarea;

import java.util.List;

public interface ListarTareasUseCase {
    List<Tarea> listarPorPanel(Long panelId);
}
