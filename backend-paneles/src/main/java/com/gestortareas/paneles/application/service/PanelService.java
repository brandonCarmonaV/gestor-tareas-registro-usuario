package com.gestortareas.paneles.application.service;

import com.gestortareas.paneles.application.exception.UnauthorizedException;
import com.gestortareas.paneles.application.exception.ValidationException;
import com.gestortareas.paneles.domain.model.EstadoPanel;
import com.gestortareas.paneles.domain.model.Panel;
import com.gestortareas.paneles.domain.port.in.ActualizarEstadoPanelUseCase;
import com.gestortareas.paneles.domain.port.in.CrearPanelUseCase;
import com.gestortareas.paneles.domain.port.in.ListarPanelesUseCase;
import com.gestortareas.paneles.domain.port.out.AuthServicePort;
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
 * - Validar permisos de usuario usando AuthServicePort
 * - Asegurar consistencia de datos
 * - Delegar a PanelRepositoryPort para persistencia
 * 
 * Nota: No modifica reglas de negocio del dominio (Panel.crear, cambiarEstado)
 * sino que las utiliza correctamente.
 */
@Service
public class PanelService implements CrearPanelUseCase, ListarPanelesUseCase,
        ActualizarEstadoPanelUseCase {

    private static final Logger logger = Logger.getLogger(PanelService.class.getName());
    
    private final PanelRepositoryPort panelRepository;
    private final AuthServicePort authService;

    public PanelService(PanelRepositoryPort panelRepository, AuthServicePort authService) {
        this.panelRepository = panelRepository;
        this.authService = authService;
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
        
        // Validar que propietarioId sea un usuario válido y autenticado
        // En este contexto, propietarioId ya viene del token validado por el controlador
        // pero revalidamos como medida defensiva
        try {
            validarPropietarioId(propietarioId);
        } catch (RuntimeException ex) {
            logger.severe("Intento de crear panel con propietarioId inválido: " + propietarioId);
            throw new UnauthorizedException("Propietario inválido o no autenticado: " + propietarioId, ex);
        }

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
        
        // Validar que propietarioId sea un usuario válido
        try {
            validarPropietarioId(propietarioId);
        } catch (RuntimeException ex) {
            logger.severe("Intento de listar paneles con propietarioId inválido: " + propietarioId);
            throw new UnauthorizedException("Propietario inválido o no autenticado: " + propietarioId, ex);
        }

        List<Panel> paneles = panelRepository.listarPorPropietario(propietarioId);
        
        logger.info("Listados " + paneles.size() + " paneles del propietario: " + propietarioId);
        return paneles;
    }

    /**
     * Actualiza el estado de un panel.
     * 
     * Flujo:
     * 1. Busca el panel por id
     * 2. Valida que propietarioId sea el propietario (autorización)
     * 3. Usa panel.cambiarEstado() para cambio idempotente
     * 4. Persiste cambio
     * 5. Retorna panel actualizado
     * 
     * @param panelId id del panel a actualizar
     * @param nuevoEstado nuevo estado deseado
     * @param propietarioId id del usuario que solicita la actualización (debe ser el propietario)
     * @return panel actualizado
     * @throws IllegalArgumentException si panel no existe
     * @throws UnauthorizedException si propietarioId no es el propietario del panel
     */
    public Panel actualizarEstado(String panelId, EstadoPanel nuevoEstado, String propietarioId) {
        
        // Buscar panel existente
        Panel panel = panelRepository.buscarPorId(panelId)
                .orElseThrow(() -> {
                    logger.warning("Intento de actualizar panel inexistente: " + panelId);
                    return new IllegalArgumentException("Panel no encontrado: " + panelId);
                });

        // Validar que el usuario sea el propietario del panel
        if (!panel.getPropietarioId().equals(propietarioId)) {
            logger.severe("Intento de actualizar panel de otro usuario. Panel: " + panelId + 
                         ", Propietario: " + panel.getPropietarioId() + ", Usuario: " + propietarioId);
            throw new UnauthorizedException("No tienes permisos para actualizar este panel");
        }

        // Cambiar estado usando método del dominio (implementa idempotencia)
        panel.cambiarEstado(nuevoEstado);
        
        // Persistir cambio
        Panel panelActualizado = panelRepository.actualizar(panel);
        
        logger.info("Panel actualizado: " + panelId + " a estado: " + nuevoEstado);
        return panelActualizado;
    }

    /**
     * Implementación requerida por interfaz ActualizarEstadoPanelUseCase.
     * Esta firma NO recibe propietarioId (será pasado por el controlador).
     * El controlador REST debe extraer propietarioId del token y llamar a
     * actualizarEstado(String, EstadoPanel, String) en su lugar.
     * 
     * @deprecated Usar actualizarEstado(String panelId, EstadoPanel nuevoEstado, String propietarioId)
     */
    @Deprecated
    @Override
    public Panel actualizarEstado(String panelId, EstadoPanel nuevoEstado) {
        throw new UnsupportedOperationException(
            "Use actualizarEstado(String panelId, EstadoPanel nuevoEstado, String propietarioId) " +
            "passando el propietarioId extraído del token de autenticación");
    }

    /**
     * Valida que un propietarioId sea un usuario autenticado válido.
     * 
     * En FASE 7 (implementación del adapter de Auth), este método verificará
     * el token llamando al servicio de autenticación remoto.
     * 
     * Por ahora, simplemente verifica que propietarioId no sea null o vacío.
     * 
     * @param propietarioId id a validar
     * @throws RuntimeException si no es válido (será capturada y convertida a UnauthorizedException)
     */
    private void validarPropietarioId(String propietarioId) {
        if (propietarioId == null || propietarioId.trim().isEmpty()) {
            throw new IllegalArgumentException("propietarioId no puede ser null o vacío");
        }
        
        // TODO FASE 7: Implementar validación real usando authService
        // propietarioId debe corresponder a un usuario autenticado válido
        // String usuarioValidado = authService.validarUsuario(token);
        // if (!usuarioValidado.equals(propietarioId)) {
        //     throw new IllegalArgumentException("propietarioId no coincide con usuario autenticado");
        // }
    }
}
