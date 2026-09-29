package com.gestortareas.paneles.application.models.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PanelRequestDTO {

    @NotBlank(message = "El nombre del panel es obligatorio")
    private String nombre;
    
    private String color;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer prioridad;
    private String descripcion;
	
}
