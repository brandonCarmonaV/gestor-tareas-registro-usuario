package com.gestortareas.paneles.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestortareas.paneles.infrastructure.adapter.out.persistence.entity.TableroEntity;

import java.util.List;

public interface TableroJpaRepository extends JpaRepository<TableroEntity, String> {

    List<TableroEntity> findByPropietarioId(String propietarioId);
    List<TableroEntity> findByMiembrosIdsContaining(String usuarioId);

}
