package com.gestortareas.paneles.domain.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.gestortareas.paneles.application.exception.UnauthorizedException;
import com.gestortareas.paneles.domain.model.EstadoPanelEnum;

public class Panel {
	private String id;
	private String nombre;
	private String color;
	private EstadoPanelEnum estado;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private Integer prioridad;
	private String propietarioId;
	private LocalDateTime fechaCreacion;
	private String descripcion;

	public Panel(String id, String nombre, String color, EstadoPanelEnum estado, LocalDate fechaInicio, LocalDate fechaFin,
			Integer prioridad, String propietarioId, LocalDateTime fechaCreacion, String descripcion) {

		validarFechas(fechaInicio, fechaFin);
		validarNombre(nombre);

		this.id = id;
		this.nombre = nombre;
		this.color = color;
		this.estado = estado;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.prioridad = prioridad;
		this.propietarioId = propietarioId;
		this.fechaCreacion = fechaCreacion;
		this.descripcion = descripcion;
	}

//    public static Panel crear(String nombre, String color, Integer prioridad,
//                              LocalDate fechaInicio, LocalDate fechaFin,
//                              String propietarioId) {
//        validarNombre(nombre);
//        validarFechas(fechaInicio, fechaFin);
//
//        return new Panel(UUID.randomUUID().toString(), nombre.trim(), color,
//                EstadoPanel.PENDIENTE, fechaInicio, fechaFin, prioridad,
//                propietarioId, LocalDateTime.now());
//    }
//
//    public static Panel reconstituit(String id, String nombre, String color,
//                                     EstadoPanel estado, LocalDate fechaInicio,
//                                     LocalDate fechaFin, Integer prioridad,
//                                     String propietarioId, LocalDateTime fechaCreacion) {
//        return new Panel(id, nombre, color, estado, fechaInicio, fechaFin,
//                prioridad, propietarioId, fechaCreacion);
//    }

    public void actualizarDatos(String nuevoNombre, String nuevoColor,
                                LocalDate nuevaFechaInicio, LocalDate nuevaFechaFin,
                                Integer nuevaPrioridad, EstadoPanelEnum nuevoEstado, String idUsuarioEdita, String descripcion) {
        validarNombre(nuevoNombre);
        validarFechas(nuevaFechaInicio, nuevaFechaFin);
        validarUsuarioPanel(idUsuarioEdita);

        this.descripcion = descripcion;
        this.nombre = nuevoNombre.trim();
        this.color = nuevoColor;
        this.fechaInicio = nuevaFechaInicio;
        this.fechaFin = nuevaFechaFin;
        this.prioridad = nuevaPrioridad;
        if (nuevoEstado != null) {
            this.estado = nuevoEstado;
        }
    }

    public void cambiarEstado(EstadoPanelEnum nuevoEstado) {
        if (nuevoEstado != null && !this.estado.equals(nuevoEstado)) {
            this.estado = nuevoEstado;
        }
    }

    private void validarUsuarioPanel(String idUsuarioEdita) {
    	if(this.propietarioId != idUsuarioEdita) {
    		throw new UnauthorizedException("No tienes permisos para actualizar este panel");
    	}
    }
    
	private static void validarNombre(String nombre) {
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del panel es obligatorio y no puede estar vacío");
		}
	}

	private static void validarFechas(LocalDate fechaInicio, LocalDate fechaFin) {
		if (fechaInicio != null && fechaFin != null && fechaFin.isBefore(fechaInicio)) {
			throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
		}
	}

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getColor() {
		return color;
	}

	public EstadoPanelEnum getEstado() {
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

	public String getPropietarioId() {
		return propietarioId;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
