package com.gestortareas.paneles.infrastructure.adapter.out.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gestortareas.paneles.domain.model.entity.Panel;
import com.gestortareas.paneles.domain.port.PanelRepository;
import com.gestortareas.paneles.domain.port.out.PanelRepositoryPort;
import com.gestortareas.paneles.infrastructure.adapter.out.persistence.entity.PanelEntity;
import com.gestortareas.paneles.infrastructure.adapter.out.persistence.mapper.PanelEntityMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PanelRepositoryAdapter implements PanelRepositoryPort, PanelRepository {

	private static final Logger logger = Logger.getLogger(PanelRepositoryAdapter.class.getName());

	private final PanelJpaRepository repository;

	@Override
	public Panel crearPanel(Panel panel) {
		PanelEntity entity = PanelEntityMapper.toPanelEntity(panel);
		PanelEntity entityGuardada = repository.save(entity);
		return PanelEntityMapper.toPanelDomain(entityGuardada);
	}

	@Override
	public List<Panel> listarPorPropietario(String propietarioId) {
		try {
			List<PanelEntity> entities = repository.findByPropietarioId(propietarioId);

			List<Panel> paneles = entities.stream().map(PanelEntityMapper::toPanelDomain).collect(Collectors.toList());

			logger.info("Listados " + paneles.size() + " paneles del propietario: " + propietarioId);

			return paneles;

		} catch (Exception ex) {
			logger.severe("Error al listar paneles: " + ex.getMessage());
			throw new RuntimeException("Error al listar paneles del propietario", ex);
		}
	}

	@Override
	public Optional<Panel> buscarPorId(String panelId) {
		try {
			Optional<PanelEntity> entityOpt = repository.findById(panelId);

			Optional<Panel> result = entityOpt.map(PanelEntityMapper::toPanelDomain);

			if (result.isPresent()) {
				logger.info("Panel encontrado: " + panelId);
			} else {
				logger.warning("Panel no encontrado: " + panelId);
			}

			return result;

		} catch (Exception ex) {
			logger.severe("Error al buscar panel: " + ex.getMessage());
			throw new RuntimeException("Error al buscar panel por id", ex);
		}
	}

	@Override
	public Panel actualizar(Panel panel) {
		try {
			PanelEntity entity = PanelEntityMapper.toPanelEntity(panel);
			PanelEntity entityActualizada = repository.save(entity);

			logger.info("Panel actualizado en BD: " + entityActualizada.getId());

			return PanelEntityMapper.toPanelDomain(entityActualizada);

		} catch (Exception ex) {
			logger.severe("Error al actualizar panel: " + ex.getMessage());
			throw new RuntimeException("Error al actualizar panel", ex);
		}
	}

	@Override
	public void eliminar(String panelId) {
		try {
			repository.deleteById(panelId);
			logger.info("Panel eliminado de BD: " + panelId);
		} catch (Exception ex) {
			logger.severe("Error al eliminar panel: " + ex.getMessage());
			throw new RuntimeException("Error al eliminar panel", ex);
		}
	}
}
