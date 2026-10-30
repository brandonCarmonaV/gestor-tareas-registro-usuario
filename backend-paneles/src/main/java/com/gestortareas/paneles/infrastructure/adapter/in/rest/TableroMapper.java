package com.gestortareas.paneles.infrastructure.adapter.in.rest;

import com.gestortareas.paneles.domain.model.Usuario;
import com.gestortareas.paneles.domain.model.entity.Tablero;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.ParticipanteResponseDTO;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.TableroResponseDTO;

public class TableroMapper {
    public static TableroResponseDTO toTableroResponseDTO(Tablero tablero, String usuarioId) {
        if (tablero == null) {
            return null;
        }

        return new TableroResponseDTO(
                tablero.getId(),
                tablero.getNombre(),
                tablero.getPropietarioId(),
                tablero.getFechaCreacion(),
                tablero.esPropietario(usuarioId)
        );
    }

    public static ParticipanteResponseDTO toParticipanteResponseDTO(Usuario usuario, Tablero tablero) {
        return new ParticipanteResponseDTO(
                usuario.id(),
                usuario.nombre(),
                usuario.email(),
                tablero.esPropietario(usuario.id())
        );
    }
}
