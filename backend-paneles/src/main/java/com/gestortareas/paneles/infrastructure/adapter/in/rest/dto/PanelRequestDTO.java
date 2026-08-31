package com.gestortareas.paneles.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

/**
 * DTO para solicitud de creación de panel.
 * 
 * Notas de seguridad:
 * - propietarioId NO se incluye en la solicitud (se extrae del token JWT validado)
 * - estado NO se incluye en la solicitud (siempre inicia como PENDIENTE)
 * - fechaCreacion se asigna automáticamente en el servidor
 */
public class PanelRequestDTO {
    @NotBlank(message = "El nombre del panel es obligatorio")
    private String nombre;
    
    private String color;
    
    private LocalDate fechaInicio;
    
    private LocalDate fechaFin;
    
    private Integer prioridad;

    public PanelRequestDTO() {
    }

    public PanelRequestDTO(String nombre, String color, LocalDate fechaInicio, LocalDate fechaFin, Integer prioridad) {
        this.nombre = nombre;
        this.color = color;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.prioridad = prioridad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }
}
