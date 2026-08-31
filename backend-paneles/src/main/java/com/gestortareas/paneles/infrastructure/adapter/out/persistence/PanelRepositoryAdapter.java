package com.gestortareas.paneles.infrastructure.adapter.out.persistence;

import com.gestortareas.paneles.domain.model.Panel;
import com.gestortareas.paneles.domain.port.out.PanelRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Adapter de persistencia que implementa PanelRepositoryPort usando Spring Data JPA.
 * 
 * Responsabilidades:
 * - Convertir entre Panel (dominio) y PanelEntity (JPA)
 * - Delegar operaciones CRUD al PanelJpaRepository
 * - Mantener la puerta de salida (output port) independiente de JPA
 * 
 * Notas:
 * - Las operaciones guardar() y actualizar() usan repository.save() (JPA merge/insert)
 * - Los métodos de búsqueda convierten PanelEntity a Panel
 * - No contiene lógica de negocio, solo mapeos
 */
@Component
public class PanelRepositoryAdapter implements PanelRepositoryPort {

    private static final Logger logger = Logger.getLogger(PanelRepositoryAdapter.class.getName());
    
    private final PanelJpaRepository repository;

    public PanelRepositoryAdapter(PanelJpaRepository repository) {
        this.repository = repository;
    }

    /**
     * Persiste un panel nuevo en la base de datos.
     * 
     * Flujo:
     * 1. Mapea Panel (dominio) a PanelEntity (JPA)
     * 2. Usa repository.save() para persiste
     * 3. Mapea PanelEntity guardada de vuelta a Panel
     * 
     * @param panel panel del dominio a persistir
     * @return panel con id confirmado en BD
     */
    @Override
    public Panel guardar(Panel panel) {
        try {
            // Mapear Panel → PanelEntity
            PanelEntity entity = toPanelEntity(panel);
            
            // Persistir
            PanelEntity entityGuardada = repository.save(entity);
            
            logger.info("Panel guardado en BD: " + entityGuardada.getId());
            
            // Mapear PanelEntity → Panel
            return toPanelDomain(entityGuardada);
            
        } catch (Exception ex) {
            logger.severe("Error al guardar panel: " + ex.getMessage());
            throw new RuntimeException("Error al persistir panel", ex);
        }
    }

    /**
     * Lista todos los paneles de un propietario.
     * 
     * Flujo:
     * 1. Usa repository.findByPropietarioId() para consultar
     * 2. Mapea lista de PanelEntity a Panel
     * 
     * @param propietarioId id del propietario
     * @return lista de paneles del propietario (vacía si no hay)
     */
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

    /**
     * Busca un panel por id.
     * 
     * Flujo:
     * 1. Usa repository.findById() para consultar
     * 2. Si existe, mapea PanelEntity a Panel
     * 3. Retorna Optional<Panel>
     * 
     * @param panelId id del panel
     * @return Optional con el panel si existe, vacío si no
     */
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

    /**
     * Actualiza un panel existente en la base de datos.
     * 
     * Flujo:
     * 1. Mapea Panel (dominio) a PanelEntity (JPA)
     * 2. Usa repository.save() (merge en BD)
     * 3. Mapea PanelEntity actualizada de vuelta a Panel
     * 
     * @param panel panel del dominio con cambios
     * @return panel con cambios confirmados en BD
     */
    @Override
    public Panel actualizar(Panel panel) {
        try {
            // Mapear Panel → PanelEntity
            PanelEntity entity = toPanelEntity(panel);
            
            // Actualizar (merge en BD)
            PanelEntity entityActualizada = repository.save(entity);
            
            logger.info("Panel actualizado en BD: " + entityActualizada.getId());
            
            // Mapear PanelEntity → Panel
            return toPanelDomain(entityActualizada);
            
        } catch (Exception ex) {
            logger.severe("Error al actualizar panel: " + ex.getMessage());
            throw new RuntimeException("Error al actualizar panel", ex);
        }
    }

    /**
     * Mapea Panel (dominio) a PanelEntity (JPA).
     * 
     * @param panel panel del dominio
     * @return entidad JPA equivalente
     */
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

    /**
     * Mapea PanelEntity (JPA) a Panel (dominio).
     * 
     * Nota: Se usa el método reconstituit() de Panel para evitar validaciones
     * de negocio (ya que los datos vienen de BD y fueron validados al persistir).
     * 
     * @param entity entidad JPA
     * @return panel del dominio
     */
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
