package com.gestortareas.paneles.domain.port.in;

import com.gestortareas.paneles.domain.model.Panel;
import com.gestortareas.paneles.domain.model.EstadoPanel;

import java.time.LocalDate;

public interface CrearPanelUseCase {
    Panel crearPanel(String nombre, String color, EstadoPanel estado, Integer prioridad,
                     LocalDate fechaInicio, LocalDate fechaFin, String propietarioId);
}
