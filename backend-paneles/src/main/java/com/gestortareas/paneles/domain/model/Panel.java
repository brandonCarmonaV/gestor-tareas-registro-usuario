package com.gestortareas.paneles.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Panel {
    private String id;
    private String nombre;
    private String color;
    private EstadoPanel estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer prioridad;
    private String propietarioId;
    private LocalDateTime fechaCreacion;

    public Panel() {
    }

    private Panel(String id, String nombre, String color, EstadoPanel estado, LocalDate fechaInicio,
                  LocalDate fechaFin, Integer prioridad, String propietarioId, LocalDateTime fechaCreacion) {
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

    /**
     * Método de fábrica para crear un Panel nuevo.
     * Valida reglas de negocio y asigna automáticamente:
     * - estado = PENDIENTE
     * - fechaCreacion = ahora
     * - id = UUID único
     * 
     * @param nombre nombre del panel (obligatorio, no vacío)
     * @param color color opcional del panel
     * @param prioridad prioridad del panel
     * @param fechaInicio fecha de inicio opcional
     * @param fechaFin fecha de fin opcional
     * @param propietarioId id del propietario (validado externamente, asumido válido)
     * @return Panel nuevo creado con reglas de negocio aplicadas
     * @throws IllegalArgumentException si nombre es null o vacío, o si fechaFin < fechaInicio
     */
    public static Panel crear(String nombre, String color, Integer prioridad,
                               LocalDate fechaInicio, LocalDate fechaFin, String propietarioId) {
        // Validar nombre
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del panel es obligatorio y no puede estar vacío");
        }

        // Validar fechas
        if (fechaInicio != null && fechaFin != null && fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        // Crear Panel con estado PENDIENTE y fecha de creación actual
        return new Panel(
                UUID.randomUUID().toString(),
                nombre.trim(),
                color,
                EstadoPanel.PENDIENTE,
                fechaInicio,
                fechaFin,
                prioridad,
                propietarioId,
                LocalDateTime.now()
        );
    }

    /**
     * Cambia el estado del panel si es diferente al actual.
     * Es idempotente: si el nuevo estado es igual al actual, no hace nada.
     * Cualquier transición de estado es válida.
     * 
     * @param nuevoEstado el nuevo estado
     */
    public void cambiarEstado(EstadoPanel nuevoEstado) {
        if (nuevoEstado != null && !this.estado.equals(nuevoEstado)) {
            this.estado = nuevoEstado;
        }
    }

    // Getters y Setters
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
}
