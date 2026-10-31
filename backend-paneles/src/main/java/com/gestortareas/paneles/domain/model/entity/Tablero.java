package com.gestortareas.paneles.domain.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gestortareas.paneles.domain.exception.UnauthorizedException;

public class Tablero {
	private String id;
	private String nombre;
	private String propietarioId;
	private LocalDateTime fechaCreacion;
	private List<String> miembrosIds;

	public Tablero(String id, String nombre, String propietarioId, LocalDateTime fechaCreacion) {
		this(id, nombre, propietarioId, fechaCreacion, new ArrayList<>());
	}

	public Tablero(String id, String nombre, String propietarioId, LocalDateTime fechaCreacion,
			List<String> miembrosIds) {

		validarNombre(nombre);

		this.id = id;
		this.nombre = nombre.trim();
		this.propietarioId = propietarioId;
		this.fechaCreacion = fechaCreacion;
		this.miembrosIds = new ArrayList<>(miembrosIds);
	}

	public boolean esPropietario(String usuarioId) {
		return this.propietarioId.equals(usuarioId);
	}

	public void validarPropietario(String usuarioId) {
		if (!esPropietario(usuarioId)) {
			throw new UnauthorizedException("Solo el propietario puede realizar esta acción sobre el tablero");
		}
	}

	public boolean esMiembro(String usuarioId) {
		return this.miembrosIds.contains(usuarioId);
	}

	public boolean tieneAcceso(String usuarioId) {
		return esPropietario(usuarioId) || esMiembro(usuarioId);
	}

	public void validarAcceso(String usuarioId) {
		if (!tieneAcceso(usuarioId)) {
			throw new UnauthorizedException("No tienes acceso a este tablero");
		}
	}

	public void agregarMiembro(String usuarioId) {
		if (usuarioId == null || usuarioId.trim().isEmpty()) {
			throw new IllegalArgumentException("El usuario a agregar es obligatorio");
		}

		if (esPropietario(usuarioId)) {
			throw new IllegalArgumentException("El propietario ya pertenece al tablero");
		}

		if (esMiembro(usuarioId)) {
			throw new IllegalArgumentException("El usuario ya es miembro del tablero");
		}

		this.miembrosIds.add(usuarioId);
	}

	public void quitarMiembro(String usuarioId) {
		if (!esMiembro(usuarioId)) {
			throw new IllegalArgumentException("El usuario no es miembro del tablero");
		}

		this.miembrosIds.remove(usuarioId);
	}

	public void validarResponsable(String responsableId) {
		if (responsableId != null && !tieneAcceso(responsableId)) {
			throw new IllegalArgumentException("El responsable debe ser participante del tablero");
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

	public List<String> getMiembrosIds() {
		return Collections.unmodifiableList(miembrosIds);
	}
}
