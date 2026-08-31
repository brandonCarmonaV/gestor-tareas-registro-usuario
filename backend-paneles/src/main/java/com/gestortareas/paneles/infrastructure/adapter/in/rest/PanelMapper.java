package com.gestortareas.paneles.infrastructure.adapter.in.rest;

import com.gestortareas.paneles.domain.model.Panel;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.PanelResponseDTO;

/**
 * Mapper para convertir entre objetos del dominio (Panel) y DTOs REST.
 * 
 * Responsabilidades:
 * - Panel → PanelResponseDTO (serializar para respuestas)
 * 
 * Nota: No mapeamos PanelRequestDTO → Panel aquí porque Panel debe crearse
 * únicamente a través de Panel.crear() que valida reglas de negocio.
 * El controlador extrae parámetros individuales del DTO y llama a crearPanel().
 */
public class PanelMapper {

    /**
     * Convierte un Panel del dominio a PanelResponseDTO para respuesta REST.
     * 
     * @param panel panel del dominio
     * @return DTO con todos los datos del panel
     */
    public static PanelResponseDTO toPanelResponseDTO(Panel panel) {
        if (panel == null) {
            return null;
        }

        return new PanelResponseDTO(
                panel.getId(),
                panel.getNombre(),
                panel.getColor(),
                panel.getEstado(),
                panel.getFechaInicio(),
                panel.getFechaFin(),
                panel.getPrioridad(),
                panel.getPropietarioId(),
                panel.getFechaCreacion()
        );
    }
}
