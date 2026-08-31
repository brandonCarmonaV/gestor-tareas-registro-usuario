package com.gestortareas.paneles.infrastructure.adapter.in.rest;

import com.gestortareas.paneles.application.exception.UnauthorizedException;
import com.gestortareas.paneles.application.exception.ValidationException;
import com.gestortareas.paneles.application.service.PanelService;
import com.gestortareas.paneles.domain.model.EstadoPanel;
import com.gestortareas.paneles.domain.model.Panel;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.PanelRequestDTO;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.PanelResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controlador REST (Adapter de entrada) para operaciones sobre paneles.
 * 
 * Responsabilidades:
 * - Recibir solicitudes HTTP
 * - Extraer y validar propietarioId del usuario autenticado
 * - Convertir entre DTOs y objetos del dominio
 * - Delegar lógica a PanelService
 * - Retornar respuestas HTTP apropiadas
 * - Manejar excepciones y retornar códigos HTTP correctos
 * 
 * Nota sobre autenticación:
 * - Por ahora recibe X-User-Id en header (para testing)
 * - En FASE 7: se integrará con Spring Security / JWT
 * 
 * Endpoints:
 * - POST   /api/paneles              → crear panel
 * - GET    /api/paneles              → listar paneles del usuario autenticado
 * - PUT    /api/paneles/{id}/estado  → cambiar estado de panel
 */
@RestController
@RequestMapping("/api/paneles")
public class PanelController {

    private static final Logger logger = Logger.getLogger(PanelController.class.getName());
    
    private final PanelService panelService;

    public PanelController(PanelService panelService) {
        this.panelService = panelService;
    }

    /**
     * Crea un nuevo panel.
     * 
     * Flujo:
     * 1. Extrae propietarioId del usuario autenticado (header X-User-Id)
     * 2. Extrae parámetros del DTO
     * 3. Llama a panelService.crearPanel()
     * 4. Convierte resultado a PanelResponseDTO
     * 5. Retorna 201 Created con el panel creado
     * 
     * Códigos HTTP:
     * - 201 Created: Panel creado exitosamente
     * - 400 Bad Request: Validación fallida (nombre vacío, fechas inválidas, etc.)
     * - 401 Unauthorized: Usuario no autenticado o inválido
     * 
     * @param request DTO con datos del panel a crear
     * @param userIdHeader propietarioId del usuario autenticado (header X-User-Id)
     * @return Panel creado con código 201
     */
    @PostMapping
    public ResponseEntity<PanelResponseDTO> crearPanel(
            @Valid @RequestBody PanelRequestDTO request,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        
        try {
            String propietarioId = extraerPropietarioId(userIdHeader);
            
            // Llamar a panelService con parámetros individuales
            Panel panelCreado = panelService.crearPanel(
                    request.getNombre(),
                    request.getColor(),
                    request.getPrioridad(),
                    request.getFechaInicio(),
                    request.getFechaFin(),
                    propietarioId
            );
            
            PanelResponseDTO response = PanelMapper.toPanelResponseDTO(panelCreado);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (ValidationException ex) {
            logger.warning("Validación fallida al crear panel: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            
        } catch (UnauthorizedException ex) {
            logger.warning("Usuario no autorizado para crear panel: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            
        } catch (IllegalArgumentException ex) {
            logger.warning("Error de validación: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Lista todos los paneles del usuario autenticado.
     * 
     * Flujo:
     * 1. Extrae propietarioId del usuario autenticado (header X-User-Id)
     * 2. Llama a panelService.listarPaneles()
     * 3. Convierte lista de Panel a lista de PanelResponseDTO
     * 4. Retorna 200 OK con lista
     * 
     * Códigos HTTP:
     * - 200 OK: Lista de paneles (puede estar vacía)
     * - 401 Unauthorized: Usuario no autenticado o inválido
     * 
     * @param userIdHeader propietarioId del usuario autenticado (header X-User-Id)
     * @return Lista de paneles del usuario
     */
    @GetMapping
    public ResponseEntity<List<PanelResponseDTO>> listarPaneles(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        
        try {
            String propietarioId = extraerPropietarioId(userIdHeader);
            
            List<Panel> paneles = panelService.listarPaneles(propietarioId);
            
            List<PanelResponseDTO> response = paneles.stream()
                    .map(PanelMapper::toPanelResponseDTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
            
        } catch (UnauthorizedException ex) {
            logger.warning("Usuario no autorizado para listar paneles: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            
        } catch (IllegalArgumentException ex) {
            logger.warning("Error al listar paneles: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Cambia el estado de un panel existente.
     * 
     * Flujo:
     * 1. Extrae propietarioId del usuario autenticado (header X-User-Id)
     * 2. Valida que usuario sea propietario del panel
     * 3. Llama a panelService.actualizarEstado() con propietarioId
     * 4. Convierte resultado a PanelResponseDTO
     * 5. Retorna 200 OK con panel actualizado
     * 
     * Códigos HTTP:
     * - 200 OK: Estado actualizado exitosamente
     * - 400 Bad Request: Panel no existe o estado inválido
     * - 401 Unauthorized: Usuario no es propietario del panel
     * - 404 Not Found: Panel no encontrado
     * 
     * @param panelId id del panel a actualizar
     * @param nuevoEstado nuevo estado del panel
     * @param userIdHeader propietarioId del usuario autenticado (header X-User-Id)
     * @return Panel actualizado
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<PanelResponseDTO> actualizarEstado(
            @PathVariable("id") String panelId,
            @RequestBody EstadoPanel nuevoEstado,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        
        try {
            String propietarioId = extraerPropietarioId(userIdHeader);
            
            // Llamar a versión de actualizarEstado que recibe propietarioId
            Panel panelActualizado = panelService.actualizarEstado(panelId, nuevoEstado, propietarioId);
            
            PanelResponseDTO response = PanelMapper.toPanelResponseDTO(panelActualizado);
            return ResponseEntity.ok(response);
            
        } catch (UnauthorizedException ex) {
            logger.warning("Usuario no autorizado para actualizar panel: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            
        } catch (IllegalArgumentException ex) {
            logger.warning("Panel no encontrado: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Extrae el propietarioId del usuario autenticado.
     * 
     * Estrategia:
     * 1. Intenta obtener del header X-User-Id (para testing/desarrollo)
     * 2. En FASE 7: se integrará con Spring Security para extraer de JWT/token
     * 
     * @param userIdHeader valor del header X-User-Id (puede ser null)
     * @return propietarioId del usuario autenticado
     * @throws UnauthorizedException si no se puede extraer propietarioId válido
     */
    private String extraerPropietarioId(String userIdHeader) {
        // Por ahora, extrae del header X-User-Id
        // En FASE 7: reemplazar con extracción de SecurityContext
        if (userIdHeader != null && !userIdHeader.trim().isEmpty()) {
            return userIdHeader.trim();
        }
        
        // TODO FASE 7: Integrar con Spring Security
        // String usuarioAutenticado = SecurityContextHolder.getContext()
        //         .getAuthentication()
        //         .getPrincipal()
        //         .toString();
        // return usuarioAutenticado;
        
        throw new UnauthorizedException("Usuario no autenticado. Proporcionar header X-User-Id o usar Spring Security");
    }
}
