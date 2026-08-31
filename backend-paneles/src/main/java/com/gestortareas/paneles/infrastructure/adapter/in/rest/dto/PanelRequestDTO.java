package com.gestortareas.paneles.infrastructure.adapter.in.rest.dto;

import com.gestortareas.paneles.domain.model.EstadoPanel;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PanelRequestDTO {
    @NotBlank
    private String nombre;
    private String color;
    private EstadoPanel estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer prioridad;
    private Long propietarioId;
}
