package com.gestortareas.paneles.domain.port.in;

import com.gestortareas.paneles.domain.model.Panel;
import java.time.LocalDate;

/**
 * Use Case para crear un nuevo panel.
 * 
 * Reglas de negocio aplicadas:
 * - El nombre es obligatorio y no puede estar vacío
 * - La fecha de fin no puede ser anterior a la fecha de inicio
 * - El propietarioId es validado externamente (ya viene del token autenticado)
 * - El estado inicial es siempre PENDIENTE
 * - La fecha de creación se asigna automáticamente
 */
public interface CrearPanelUseCase {
    /**
     * Crea un nuevo panel con las validaciones de negocio aplicadas.
     * 
     * @param nombre nombre del panel (obligatorio, no vacío)
     * @param color color opcional del panel
     * @param prioridad prioridad del panel
     * @param fechaInicio fecha de inicio opcional
     * @param fechaFin fecha de fin opcional
     * @param propietarioId id del propietario (validado externamente)
     * @return Panel creado con id único, estado PENDIENTE y fechaCreacion actual
     * @throws IllegalArgumentException si nombre está vacío o fechaFin < fechaInicio
     */
    Panel crearPanel(String nombre, String color, Integer prioridad,
                     LocalDate fechaInicio, LocalDate fechaFin, String propietarioId);
}
