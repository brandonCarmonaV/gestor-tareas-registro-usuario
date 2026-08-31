package com.gestortareas.paneles.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO para solicitud de creación de panel.
 * 
 * Notas de seguridad:
 * - propietarioId NO se incluye en la solicitud (se extrae del token JWT validado)
 * - estado NO se incluye en la solicitud (siempre inicia como PENDIENTE)
 * - fechaCreacion se asigna automáticamente en el servidor
 */
@Data
public class PanelRequestDTO {
    @NotBlank(message = "El nombre del panel es obligatorio")
    private String nombre;
    
    private String color;
    
    private LocalDate fechaInicio;
    
    private LocalDate fechaFin;
    
    private Integer prioridad;
}
