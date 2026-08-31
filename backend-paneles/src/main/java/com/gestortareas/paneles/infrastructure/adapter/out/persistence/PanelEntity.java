package com.gestortareas.paneles.infrastructure.adapter.out.persistence;

import com.gestortareas.paneles.domain.model.EstadoPanel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa la tabla 'paneles' en la base de datos.
 * 
 * Responsabilidades:
 * - Mapear atributos del Panel a columnas de BD
 * - Persistencia y recuperación vía Hibernate/JPA
 * - No contiene lógica de negocio (esa está en Panel.java del dominio)
 * 
 * Nota: El adapter PanelRepositoryAdapter convierte entre PanelEntity y Panel.
 */
@Entity
@Table(name = "paneles")
public class PanelEntity {
    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;
    
    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;
    
    @Column(name = "color", length = 50)
    private String color;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPanel estado;
    
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
    
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
    
    @Column(name = "prioridad")
    private Integer prioridad;
    
    @Column(name = "propietario_id", nullable = false, length = 255)
    private String propietarioId;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    public PanelEntity() {
    }

    public PanelEntity(String id, String nombre, String color, EstadoPanel estado,
                       LocalDate fechaInicio, LocalDate fechaFin, Integer prioridad,
                       String propietarioId, LocalDateTime fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.color = color;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.prioridad = prioridad;
        this.propietarioId = propietarioId;
        this.fechaCreacion = fechaCreacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public EstadoPanel getEstado() {
        return estado;
    }

    public void setEstado(EstadoPanel estado) {
        this.estado = estado;
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

    public String getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(String propietarioId) {
        this.propietarioId = propietarioId;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "PanelEntity{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", estado=" + estado +
                ", propietarioId='" + propietarioId + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
