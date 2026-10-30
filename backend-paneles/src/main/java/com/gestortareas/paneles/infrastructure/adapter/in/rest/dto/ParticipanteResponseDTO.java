package com.gestortareas.paneles.infrastructure.adapter.in.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipanteResponseDTO {
    private String usuarioId;
    private String nombre;
    private String email;
    private boolean esPropietario;
}
