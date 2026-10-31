package com.gestortareas.paneles.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestortareas.paneles.infrastructure.adapter.out.persistence.entity.PanelEntity;

import java.util.List;

public interface PanelJpaRepository extends JpaRepository<PanelEntity, String> {
	
    List<PanelEntity> findByPropietarioId(String propietarioId);
    List<PanelEntity> findByTableroId(String tableroId);
    List<PanelEntity> findByPropietarioIdAndTableroIdIsNull(String propietarioId);
    // responsable_id = ?1 OR (responsable_id IS NULL AND propietario_id = ?2)
    List<PanelEntity> findByResponsableIdOrResponsableIdIsNullAndPropietarioId(String responsableId, String propietarioId);

}
