package com.gestortareas.paneles.infrastructure.adapter.out.persistence;

import com.gestortareas.paneles.domain.port.out.PanelRepositoryPort;
import com.gestortareas.paneles.domain.model.Panel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class PanelRepositoryAdapter implements PanelRepositoryPort {

    private static final Logger logger = Logger.getLogger(PanelRepositoryAdapter.class.getName());
    
    private final PanelJpaRepository repository;

    public PanelRepositoryAdapter(PanelJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Panel guardar(Panel panel) {
        try {
            PanelEntity entity = toPanelEntity(panel);
            PanelEntity entityGuardada = repository.save(entity);
            
            logger.info("Panel guardado en BD: " + entityGuardada.getId());
            
            return toPanelDomain(entityGuardada);
            
        } catch (Exception ex) {
            logger.severe("Error al guardar panel: " + ex.getMessage());
            throw new RuntimeException("Error al persistir panel", ex);
        }
    }

    @Override
    public List<Panel> listarPorPropietario(String propietarioId) {
        try {
            List<PanelEntity> entities = repository.findByPropietarioId(propietarioId);
            
            List<Panel> paneles = entities.stream()
                    .map(this::toPanelDomain)
                    .collect(Collectors.toList());
            
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
            
            Optional<Panel> result = entityOpt.map(this::toPanelDomain);
            
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
            PanelEntity entity = toPanelEntity(panel);
            PanelEntity entityActualizada = repository.save(entity);
            
            logger.info("Panel actualizado en BD: " + entityActualizada.getId());
            
            return toPanelDomain(entityActualizada);
            
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

    private PanelEntity toPanelEntity(Panel panel) {
        return new PanelEntity(
                panel.getId(),
                panel.getNombre(),
                panel.getColor(),
                panel.getEstado(),
                panel.getFechaInicio(),
                panel.getFechaFin(),
                panel.getPrioridad(),
                panel.getPropietarioId(),
                panel.getFechaCreacion()
        );
    }

    private Panel toPanelDomain(PanelEntity entity) {
        return Panel.reconstituit(
                entity.getId(),
                entity.getNombre(),
                entity.getColor(),
                entity.getEstado(),
                entity.getFechaInicio(),
                entity.getFechaFin(),
                entity.getPrioridad(),
                entity.getPropietarioId(),
                entity.getFechaCreacion()
        );
    }
}
