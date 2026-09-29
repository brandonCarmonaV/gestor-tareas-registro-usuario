package com.gestortareas.paneles.infrastructure.adapter.out.persistence.mapper;

import com.gestortareas.paneles.domain.model.entity.Panel;
import com.gestortareas.paneles.infrastructure.adapter.out.persistence.entity.PanelEntity;

public class PanelEntityMapper {

	public static PanelEntity toPanelEntity(Panel panel) {
		return new PanelEntity(panel.getId(), panel.getNombre(), panel.getColor(), panel.getEstado(),
				panel.getFechaInicio(), panel.getFechaFin(), panel.getPrioridad(), panel.getPropietarioId(),
				panel.getFechaCreacion(), panel.getDescripcion(), panel.getFechaCompletado());
	}

	public static Panel toPanelDomain(PanelEntity entity) {
		return new Panel(entity.getId(), entity.getNombre(), entity.getColor(), entity.getEstado(),
				entity.getFechaInicio(), entity.getFechaFin(), entity.getPrioridad(), entity.getPropietarioId(),
				entity.getFechaCreacion(), entity.getDescripcion());
	}
	
}
