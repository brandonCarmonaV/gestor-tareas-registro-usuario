package com.gestortareas.paneles.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TareaJpaRepository extends JpaRepository<TareaEntity, Long> {
    List<TareaEntity> findByPanelId(Long panelId);
}
