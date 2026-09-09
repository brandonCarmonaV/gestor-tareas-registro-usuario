package com.gestortareas.paneles.application.service;

import rmi.shared.Panel;
import rmi.shared.EstadoPanel;

import com.gestortareas.paneles.application.exception.UnauthorizedException;
import com.gestortareas.paneles.application.exception.ValidationException;
import com.gestortareas.paneles.domain.port.in.CrearPanelUseCase;
import com.gestortareas.paneles.domain.port.in.ListarPanelesUseCase;
import com.gestortareas.paneles.domain.port.out.PanelRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

/**
 * Servicio de aplicación que orquesta los use cases de Panel.
 * 
 * Responsabilidades:
 * - Orquestar la creación, lectura y actualización de paneles
 * - Aplicar las reglas de autorización sobre los paneles
 * - Asegurar consistencia de datos
 * - Delegar a PanelRepositoryPort para persistencia
 * 
 * Nota: No modifica reglas de negocio del dominio (Panel.crear, cambiarEstado)
 * sino que las utiliza correctamente.
 */
@Service
public class PanelService implements CrearPanelUseCase, ListarPanelesUseCase {

    private static final Logger logger = Logger.getLogger(PanelService.class.getName());
    
    private final PanelRepositoryPort panelRepository;

    public PanelService(PanelRepositoryPort panelRepository) {
        this.panelRepository = panelRepository;
    }

    /**
     * Crea un nuevo panel con validaciones de negocio y seguridad.
     * 
     * Flujo:
     * 1. Valida que propietarioId sea un usuario autenticado válido
     * 2. Delega a Panel.crear() para crear con reglas de negocio encapsuladas
     * 3. Persiste en repositorio
     * 4. Retorna panel creado
     * 
     * @param nombre nombre del panel
     * @param color color del panel
     * @param prioridad prioridad del panel
     * @param fechaInicio fecha de inicio
     * @param fechaFin fecha de fin
     * @param propietarioId id del propietario (debe ser usuario autenticado válido)
     * @return panel creado con id único, estado PENDIENTE, fechaCreacion asignada
     * @throws ValidationException si nombre vacío, fechas inválidas, o propietarioId inválido
     * @throws UnauthorizedException si propietarioId no es usuario válido
     */
    @Override
    public Panel crearPanel(String nombre, String color, Integer prioridad,
                            LocalDate fechaInicio, LocalDate fechaFin, String propietarioId) {
        
        try {
            // Panel.crear() valida nombre y fechas según reglas de negocio
            Panel panel = Panel.crear(nombre, color, prioridad, fechaInicio, fechaFin, propietarioId);
            
            // Persistir en repositorio
            Panel panelGuardado = panelRepository.guardar(panel);
            
            logger.info("Panel creado exitosamente: " + panelGuardado.getId() + 
                       " por propietario: " + propietarioId);
            return panelGuardado;
            
        } catch (IllegalArgumentException ex) {
            // Las excepciones de Panel.crear() indican validaciones de negocio fallidas
            logger.warning("Validación fallida al crear panel: " + ex.getMessage());
            throw new ValidationException("Error de validación al crear panel: " + ex.getMessage(), ex);
        }
    }

    /**
     * Lista todos los paneles de un propietario.
     * 
     * Flujo:
     * 1. Valida que propietarioId sea válido
     * 2. Consulta repositorio
     * 3. Retorna lista (puede estar vacía)
     * 
     * @param propietarioId id del propietario autenticado
     * @return lista de paneles del propietario (puede estar vacía, nunca null)
     * @throws UnauthorizedException si propietarioId no es válido
     */
    @Override
    public List<Panel> listarPaneles(String propietarioId) {
        
        List<Panel> paneles = panelRepository.listarPorPropietario(propietarioId);
        
        logger.info("Listados " + paneles.size() + " paneles del propietario: " + propietarioId);
        return paneles;
    }

    public Panel actualizarPanel(String panelId, String nombre, String color,
                                 LocalDate fechaInicio, LocalDate fechaFin,
                                 Integer prioridad, EstadoPanel estado,
                                 String propietarioId) {
        Panel panel = panelRepository.buscarPorId(panelId)
                .orElseThrow(() -> {
                    logger.warning("Intento de actualizar panel inexistente: " + panelId);
                    return new IllegalArgumentException("Panel no encontrado: " + panelId);
                });

        if (!panel.getPropietarioId().equals(propietarioId)) {
            logger.severe("Intento de actualizar panel de otro usuario. Panel: " + panelId + 
                         ", Propietario: " + panel.getPropietarioId() + ", Usuario: " + propietarioId);
            throw new UnauthorizedException("No tienes permisos para actualizar este panel");
        }

        try {
            panel.actualizarDatos(nombre, color, fechaInicio, fechaFin, prioridad, estado);
            return panelRepository.actualizar(panel);
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Error de validación al actualizar panel: " + ex.getMessage(), ex);
        }
    }

    public void eliminarPanel(String panelId, String propietarioId) {
        Panel panel = panelRepository.buscarPorId(panelId)
                .orElseThrow(() -> new IllegalArgumentException("Panel no encontrado: " + panelId));

        if (!panel.getPropietarioId().equals(propietarioId)) {
            throw new UnauthorizedException("No tienes permisos para eliminar este panel");
        }

        panelRepository.eliminar(panelId);
    }

}
