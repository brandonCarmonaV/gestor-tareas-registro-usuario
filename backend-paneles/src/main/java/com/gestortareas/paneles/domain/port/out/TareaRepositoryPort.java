package com.gestortareas.paneles.domain.port.out;

import com.gestortareas.paneles.domain.model.Tarea;

import java.util.List;

public interface TareaRepositoryPort {
    Tarea guardar(Tarea tarea);
    List<Tarea> buscarPorPanel(Long panelId);
}
