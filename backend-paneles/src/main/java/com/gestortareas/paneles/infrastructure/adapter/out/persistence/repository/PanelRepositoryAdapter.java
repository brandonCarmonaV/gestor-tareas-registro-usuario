package com.gestortareas.paneles.infrastructure.adapter.out.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gestortareas.paneles.domain.model.entity.Panel;
import com.gestortareas.paneles.domain.port.PanelRepository;
import com.gestortareas.paneles.infrastructure.adapter.out.persistence.entity.PanelEntity;
import com.gestortareas.paneles.infrastructure.adapter.out.persistence.mapper.PanelEntityMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PanelRepositoryAdapter implements PanelRepository {

	private final PanelJpaRepository repository;

	@Override
	public Panel crearPanel(Panel panel) {
		PanelEntity entity = PanelEntityMapper.toPanelEntity(panel);
		PanelEntity entityGuardada = repository.save(entity);
		return PanelEntityMapper.toPanelDomain(entityGuardada);
	}

	@Override
	public List<Panel> listarPorPropietario(String propietarioId) {
		List<PanelEntity> entities = repository.findByPropietarioId(propietarioId);

		List<Panel> paneles = entities.stream().map(PanelEntityMapper::toPanelDomain).collect(Collectors.toList());
		return paneles;
	}

	@Override
	public Optional<Panel> buscarPorId(String panelId) {
		Optional<PanelEntity> entityOpt = repository.findById(panelId);

		Optional<Panel> result = entityOpt.map(PanelEntityMapper::toPanelDomain);

		if (!result.isPresent()) {
			log.warn("Panel no encontrado: " + panelId);
		}

		return result;
	}

	@Override
	public Panel actualizar(Panel panel) {
		PanelEntity entity = PanelEntityMapper.toPanelEntity(panel);
		PanelEntity entityActualizada = repository.save(entity);
		return PanelEntityMapper.toPanelDomain(entityActualizada);

	}

	@Override
	public void eliminar(String panelId) {
		repository.deleteById(panelId);
	}
}
