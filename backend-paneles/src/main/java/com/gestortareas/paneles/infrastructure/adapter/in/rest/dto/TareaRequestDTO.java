package com.gestortareas.paneles.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TareaRequestDTO {
    private Long panelId;
    @NotBlank
    private String titulo;
    private String estado;
    private Integer prioridad;
    private LocalDate fechaLimite;
    private Integer progreso;
}
