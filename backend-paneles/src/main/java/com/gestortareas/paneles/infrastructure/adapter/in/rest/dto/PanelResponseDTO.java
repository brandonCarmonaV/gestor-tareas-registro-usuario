package com.gestortareas.paneles.infrastructure.adapter.in.rest.dto;

import com.gestortareas.paneles.domain.model.EstadoPanel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PanelResponseDTO {
    private String id;
    private String nombre;
    private String color;
    private EstadoPanel estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer prioridad;
    private String propietarioId;
    private LocalDateTime fechaCreacion;
}
