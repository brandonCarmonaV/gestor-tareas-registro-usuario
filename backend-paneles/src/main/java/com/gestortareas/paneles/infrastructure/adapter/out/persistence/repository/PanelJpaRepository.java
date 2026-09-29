package com.gestortareas.paneles.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestortareas.paneles.infrastructure.adapter.out.persistence.entity.PanelEntity;

import java.util.List;

public interface PanelJpaRepository extends JpaRepository<PanelEntity, String> {
	
    List<PanelEntity> findByPropietarioId(String propietarioId);

}
