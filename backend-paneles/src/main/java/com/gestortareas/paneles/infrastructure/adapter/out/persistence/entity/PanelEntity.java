package com.gestortareas.paneles.infrastructure.adapter.out.persistence.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.gestortareas.paneles.domain.model.EstadoPanelEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private EstadoPanelEnum estado;
    
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
    
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    @Column(name = "fecha_completado")
    private LocalDateTime fechaCompletado;
}
