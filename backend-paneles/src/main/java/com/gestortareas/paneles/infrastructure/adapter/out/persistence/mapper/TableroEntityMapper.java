package com.gestortareas.paneles.infrastructure.adapter.out.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import com.gestortareas.paneles.domain.model.entity.Tablero;
import com.gestortareas.paneles.infrastructure.adapter.out.persistence.entity.TableroEntity;

public class TableroEntityMapper {

	public static TableroEntity toTableroEntity(Tablero tablero) {
		return new TableroEntity(tablero.getId(), tablero.getNombre(), tablero.getPropietarioId(),
				tablero.getFechaCreacion(), new ArrayList<>(tablero.getMiembrosIds()));
	}

	public static Tablero toTableroDomain(TableroEntity entity) {
		List<String> miembrosIds = entity.getMiembrosIds() != null ? entity.getMiembrosIds() : new ArrayList<>();

		return new Tablero(entity.getId(), entity.getNombre(), entity.getPropietarioId(),
				entity.getFechaCreacion(), miembrosIds);
	}

}
