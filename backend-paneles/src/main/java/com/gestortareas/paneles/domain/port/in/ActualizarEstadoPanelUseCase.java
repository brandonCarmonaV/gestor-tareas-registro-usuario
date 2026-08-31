package com.gestortareas.paneles.domain.port.in;

import com.gestortareas.paneles.domain.model.EstadoPanel;
import com.gestortareas.paneles.domain.model.Panel;

public interface ActualizarEstadoPanelUseCase {
    Panel actualizarEstado(String panelId, EstadoPanel nuevoEstado);
}
