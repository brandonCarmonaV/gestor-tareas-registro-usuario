package com.gestortareas.paneles.infrastructure.adapter.out.persistence;

import com.gestortareas.paneles.domain.model.EstadoPanel;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "paneles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PanelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String color;
    @Enumerated(EnumType.STRING)
    private EstadoPanel estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer prioridad;
    private Long propietarioId;
}
