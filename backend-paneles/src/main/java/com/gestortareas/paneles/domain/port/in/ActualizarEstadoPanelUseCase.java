package com.gestortareas.paneles.domain.port.in;

import com.gestortareas.paneles.domain.model.EstadoPanel;
import com.gestortareas.paneles.domain.model.Panel;

/**
 * Use Case para actualizar el estado de un panel.
 * 
 * Reglas de negocio aplicadas:
 * - Cualquier transición de estado es válida (PENDIENTE → EN_PROGRESO → COMPLETADO, etc.)
 * - El cambio es idempotente: si el nuevo estado es igual al actual, no hace nada
 */
public interface ActualizarEstadoPanelUseCase {
    /**
     * Actualiza el estado de un panel existente.
     * 
     * @param panelId id del panel a actualizar
     * @param nuevoEstado nuevo estado del panel
     * @return Panel actualizado
     * @throws IllegalArgumentException si el panel no existe
     */
    Panel actualizarEstado(String panelId, EstadoPanel nuevoEstado);
}
