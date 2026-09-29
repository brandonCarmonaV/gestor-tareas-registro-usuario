package com.gestortareas.paneles.infrastructure.adapter.in.rest;

import com.gestortareas.paneles.domain.model.entity.Panel;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.PanelResponseDTO;

public class PanelMapper {
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
