package com.gestortareas.paneles.domain.model.entity;

import java.time.LocalDateTime;

import com.gestortareas.paneles.domain.exception.UnauthorizedException;

public class Tablero {
	private String id;
	private String nombre;
	private String propietarioId;
	private LocalDateTime fechaCreacion;

	public Tablero(String id, String nombre, String propietarioId, LocalDateTime fechaCreacion) {

		validarNombre(nombre);

		this.id = id;
		this.nombre = nombre.trim();
		this.propietarioId = propietarioId;
		this.fechaCreacion = fechaCreacion;
	}

	public boolean esPropietario(String usuarioId) {
		return this.propietarioId.equals(usuarioId);
	}

	public void validarPropietario(String usuarioId) {
		if (!esPropietario(usuarioId)) {
			throw new UnauthorizedException("Solo el propietario puede realizar esta acción sobre el tablero");
		}
	}

	private static void validarNombre(String nombre) {
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del tablero es obligatorio y no puede estar vacío");
		}
	}

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getPropietarioId() {
		return propietarioId;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}
}
