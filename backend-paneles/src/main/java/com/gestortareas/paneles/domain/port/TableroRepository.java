package com.gestortareas.paneles.domain.port;

import java.util.List;
import java.util.Optional;

import com.gestortareas.paneles.domain.model.entity.Tablero;

public interface TableroRepository {

	Tablero crear(Tablero tablero);
	List<Tablero> listarPorPropietario(String propietarioId);
	Optional<Tablero> buscarPorId(String tableroId);
	void eliminar(String tableroId);

}
