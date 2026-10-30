package com.gestortareas.paneles.infrastructure.adapter.in.rest;

import com.gestortareas.paneles.domain.model.entity.Tablero;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.TableroResponseDTO;

public class TableroMapper {
    public static TableroResponseDTO toTableroResponseDTO(Tablero tablero) {
        if (tablero == null) {
            return null;
        }

        return new TableroResponseDTO(
                tablero.getId(),
                tablero.getNombre(),
                tablero.getPropietarioId(),
                tablero.getFechaCreacion()
        );
    }
}
