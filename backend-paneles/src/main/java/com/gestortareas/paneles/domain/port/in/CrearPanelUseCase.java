package com.gestortareas.paneles.domain.port.in;

import com.gestortareas.paneles.domain.model.Panel;

import java.time.LocalDate;

public interface CrearPanelUseCase {
    Panel crearPanel(String nombre, String color, Integer prioridad,
                     LocalDate fechaInicio, LocalDate fechaFin, String propietarioId);
}
