package com.gestortareas.paneles.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarea {
    private Long id;
    private Long panelId;
    private String titulo;
    private String estado;
    private Integer prioridad;
    private LocalDate fechaLimite;
    private Integer progreso;
}
