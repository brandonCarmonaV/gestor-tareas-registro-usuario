package com.gestortareas.paneles.infrastructure.adapter.in.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TareaResponseDTO {
    private Long id;
    private Long panelId;
    private String titulo;
    private String estado;
    private Integer prioridad;
    private LocalDate fechaLimite;
    private Integer progreso;
}
