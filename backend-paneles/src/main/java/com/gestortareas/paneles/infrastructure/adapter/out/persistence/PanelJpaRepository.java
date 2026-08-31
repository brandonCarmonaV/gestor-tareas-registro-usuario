package com.gestortareas.paneles.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PanelJpaRepository extends JpaRepository<PanelEntity, Long> {
    List<PanelEntity> findByPropietarioId(Long propietarioId);
}
