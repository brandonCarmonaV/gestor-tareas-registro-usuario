package com.gestortareas.paneles.infrastructure.adapter.out.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gestortareas.paneles.domain.model.entity.Tablero;
import com.gestortareas.paneles.domain.port.TableroRepository;
import com.gestortareas.paneles.infrastructure.adapter.out.persistence.entity.TableroEntity;
import com.gestortareas.paneles.infrastructure.adapter.out.persistence.mapper.TableroEntityMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TableroRepositoryAdapter implements TableroRepository {

	private final TableroJpaRepository repository;

	@Override
	public Tablero crear(Tablero tablero) {
		TableroEntity entity = TableroEntityMapper.toTableroEntity(tablero);
		TableroEntity entityGuardada = repository.save(entity);
		return TableroEntityMapper.toTableroDomain(entityGuardada);
	}

	@Override
	public List<Tablero> listarPorPropietario(String propietarioId) {
		List<TableroEntity> entities = repository.findByPropietarioId(propietarioId);

		List<Tablero> tableros = entities.stream().map(TableroEntityMapper::toTableroDomain).collect(Collectors.toList());
		return tableros;
	}

	@Override
	public Optional<Tablero> buscarPorId(String tableroId) {
		Optional<TableroEntity> entityOpt = repository.findById(tableroId);

		Optional<Tablero> result = entityOpt.map(TableroEntityMapper::toTableroDomain);

		if (!result.isPresent()) {
			log.warn("Tablero no encontrado: " + tableroId);
		}

		return result;
	}

	@Override
	public void eliminar(String tableroId) {
		repository.deleteById(tableroId);
	}
}
