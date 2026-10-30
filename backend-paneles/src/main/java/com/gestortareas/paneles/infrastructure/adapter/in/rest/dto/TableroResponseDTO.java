package com.gestortareas.paneles.infrastructure.adapter.in.rest.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TableroResponseDTO {
    private String id;
    private String nombre;
    private String propietarioId;
    private LocalDateTime fechaCreacion;
}
