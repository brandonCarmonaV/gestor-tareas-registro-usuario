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
     * Método de fábrica para RECONSTRUIR un Panel desde la base de datos.
     * NO aplica validaciones (asume que los datos ya fueron validados al persistir).
     * Usado únicamente por PanelRepositoryAdapter para mapear PanelEntity → Panel.
     * 
     * @param id id del panel (desde BD)
     * @param nombre nombre del panel (desde BD)
     * @param color color del panel (desde BD)
     * @param estado estado del panel (desde BD)
     * @param fechaInicio fecha de inicio (desde BD)
     * @param fechaFin fecha de fin (desde BD)
     * @param prioridad prioridad (desde BD)
     * @param propietarioId id del propietario (desde BD)
     * @param fechaCreacion fecha de creación (desde BD)
     * @return Panel reconstruido sin validaciones
     */
    public static Panel reconstituit(String id, String nombre, String color, EstadoPanel estado,
                                     LocalDate fechaInicio, LocalDate fechaFin, Integer prioridad,
                                     String propietarioId, LocalDateTime fechaCreacion) {
        return new Panel(id, nombre, color, estado, fechaInicio, fechaFin, prioridad, propietarioId, fechaCreacion);
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

    /**
     * Renombra el panel con validación de reglas de negocio.
     * El nombre no puede ser null o vacío.
     * 
     * @param nuevoNombre el nuevo nombre del panel
     * @throws IllegalArgumentException si el nombre es null o vacío
     */
    public void renombrar(String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del panel es obligatorio y no puede estar vacío");
        }
        this.nombre = nuevoNombre.trim();
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
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

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
}
