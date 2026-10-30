package com.gestortareas.paneles.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gestortareas.paneles.domain.exception.ValidationException;
import com.gestortareas.paneles.domain.model.entity.Tablero;
import com.gestortareas.paneles.domain.port.TableroRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TableroService {

	private static final String NOMBRE_TABLERO_POR_DEFECTO = "Mi tablero";

	private final TableroRepository tableroRepository;

	public Tablero crearTablero(String nombre, String propietarioId) {

		try {
			Tablero tableroNuevo = new Tablero(null, nombre, propietarioId, LocalDateTime.now());

			return tableroRepository.crear(tableroNuevo);

		} catch (IllegalArgumentException ex) {
			log.warn("Validación fallida al crear tablero: " + ex.getMessage());
			throw new ValidationException("Error de validación al crear tablero: " + ex.getMessage(), ex);
		}
	}

	public List<Tablero> listarTableros(String usuarioId) {
		List<Tablero> tableros = tableroRepository.listarPorPropietario(usuarioId);

		if (tableros.isEmpty()) {
			log.info("El usuario no tiene tableros, se crea el tablero por defecto");
			Tablero tableroPorDefecto = crearTablero(NOMBRE_TABLERO_POR_DEFECTO, usuarioId);
			return List.of(tableroPorDefecto);
		}

		return tableros;
	}

	public void eliminarTablero(String tableroId, String usuarioId) {
		Tablero tablero = tableroRepository.buscarPorId(tableroId)
				.orElseThrow(() -> new IllegalArgumentException("Tablero no encontrado: " + tableroId));

		tablero.validarPropietario(usuarioId);

		tableroRepository.eliminar(tableroId);
	}
}
