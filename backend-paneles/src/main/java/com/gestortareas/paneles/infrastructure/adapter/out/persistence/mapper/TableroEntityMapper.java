package com.gestortareas.paneles.infrastructure.adapter.out.persistence.mapper;

import com.gestortareas.paneles.domain.model.entity.Tablero;
import com.gestortareas.paneles.infrastructure.adapter.out.persistence.entity.TableroEntity;

public class TableroEntityMapper {

	public static TableroEntity toTableroEntity(Tablero tablero) {
		return new TableroEntity(tablero.getId(), tablero.getNombre(), tablero.getPropietarioId(),
				tablero.getFechaCreacion());
	}

	public static Tablero toTableroDomain(TableroEntity entity) {
		return new Tablero(entity.getId(), entity.getNombre(), entity.getPropietarioId(),
				entity.getFechaCreacion());
	}

}
