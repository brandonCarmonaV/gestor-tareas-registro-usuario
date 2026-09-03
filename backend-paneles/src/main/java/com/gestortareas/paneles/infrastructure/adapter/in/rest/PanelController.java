package com.gestortareas.paneles.infrastructure.adapter.in.rest;

import com.gestortareas.paneles.application.exception.UnauthorizedException;
import com.gestortareas.paneles.application.exception.ValidationException;
import com.gestortareas.paneles.application.service.PanelService;
import com.gestortareas.paneles.domain.model.EstadoPanel;
import com.gestortareas.paneles.domain.model.Panel;
import com.gestortareas.paneles.domain.port.out.AuthServicePort;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.PanelRequestDTO;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.PanelResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
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
    private final AuthServicePort authService;
    private final boolean allowTestUserHeader;

    public PanelController(PanelService panelService, AuthServicePort authService,
                           @Value("${app.auth.allow-test-user-header:false}") boolean allowTestUserHeader) {
        this.panelService = panelService;
        this.authService = authService;
        this.allowTestUserHeader = allowTestUserHeader;
    }

    /**
     * Crea un nuevo panel.
     * 
     * Flujo:
     * 1. Extrae token JWT del header Authorization
     * 2. Valida token contra backend de Auth usando AuthServicePort
     * 3. Extrae propietarioId del resultado
     * 4. Extrae parámetros del DTO
     * 5. Llama a panelService.crearPanel()
     * 6. Convierte resultado a PanelResponseDTO
     * 7. Retorna 201 Created con el panel creado
     * 
     * Códigos HTTP:
     * - 201 Created: Panel creado exitosamente
     * - 400 Bad Request: Validación fallida (nombre vacío, fechas inválidas, etc.)
     * - 401 Unauthorized: Token inválido o usuario no autenticado
     * 
     * @param request DTO con datos del panel a crear
     * @param authHeader header Authorization con token JWT (ej: "Bearer <token>")
     * @param userIdHeader header X-User-Id como fallback para testing (ignorado si authHeader presente)
     * @return Panel creado con código 201
     */
    @PostMapping
    public ResponseEntity<PanelResponseDTO> crearPanel(
            @Valid @RequestBody PanelRequestDTO request,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        
        try {
            String propietarioId = extraerPropietarioId(authHeader, userIdHeader);
            
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
     * 1. Extrae token JWT del header Authorization o X-User-Id para fallback
     * 2. Valida token y obtiene propietarioId
     * 3. Llama a panelService.listarPaneles()
     * 4. Convierte lista de Panel a lista de PanelResponseDTO
     * 5. Retorna 200 OK con lista
     * 
     * Códigos HTTP:
     * - 200 OK: Lista de paneles (puede estar vacía)
     * - 401 Unauthorized: Usuario no autenticado o inválido
     * 
     * @param authHeader token JWT en Authorization header
     * @param userIdHeader propietarioId del usuario autenticado (header X-User-Id) como fallback
     * @return Lista de paneles del usuario
     */
    @GetMapping
    public ResponseEntity<List<PanelResponseDTO>> listarPaneles(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        
        try {
            String propietarioId = extraerPropietarioId(authHeader, userIdHeader);
            
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
     * 1. Extrae token JWT del header Authorization o X-User-Id para fallback
     * 2. Valida token y obtiene propietarioId
     * 3. Valida que usuario sea propietario del panel
     * 4. Llama a panelService.actualizarEstado() con propietarioId
     * 5. Convierte resultado a PanelResponseDTO
     * 6. Retorna 200 OK con panel actualizado
     * 
     * Códigos HTTP:
     * - 200 OK: Estado actualizado exitosamente
     * - 400 Bad Request: Panel no existe o estado inválido
     * - 401 Unauthorized: Usuario no es propietario del panel
     * - 404 Not Found: Panel no encontrado
     * 
     * @param panelId id del panel a actualizar
     * @param nuevoEstado nuevo estado del panel
     * @param authHeader token JWT en Authorization header
     * @param userIdHeader propietarioId del usuario autenticado (header X-User-Id) como fallback
     * @return Panel actualizado
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<PanelResponseDTO> actualizarEstado(
            @PathVariable("id") String panelId,
            @RequestBody EstadoPanel nuevoEstado,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        
        try {
            String propietarioId = extraerPropietarioId(authHeader, userIdHeader);
            
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
     * Estrategia FASE 7 implementada:
     * 1. Preferir token JWT en Authorization header
     * 2. Fallback: usar X-User-Id directamente (testing/desarrollo)
     * 3. Si ninguno disponible: lanzar UnauthorizedException
     * 
     * Con token JWT:
     * - Extrae token del header "Authorization: Bearer <token>"
     * - Valida token usando AuthServicePort.validarUsuario()
     * - Retorna el propietarioId/userId validado por el backend de Auth remoto
     * 
     * Con X-User-Id:
     * - Retorna el valor del header sin validación (solo para testing)
     * 
     * @param authHeader valor del header Authorization (puede ser null)
     * @param userIdHeader valor del header X-User-Id como fallback (puede ser null)
     * @return propietarioId del usuario autenticado
     * @throws UnauthorizedException si no se puede extraer propietarioId válido
     */
    private String extraerPropietarioId(String authHeader, String userIdHeader) {
        // FASE 7: Preferir token JWT en Authorization header
        if (authHeader != null && !authHeader.trim().isEmpty()) {
            try {
                // Extraer token de "Bearer <token>"
                String token = extraerTokenDelHeader(authHeader);
                
                logger.info("Validando token JWT contra backend de Auth remoto...");
                
                // Usar AuthServicePort para validar token y obtener userId
                String propietarioId = authService.validarUsuario(token);
                
                logger.info("Token validado exitosamente. propietarioId=" + propietarioId);
                return propietarioId;
                
            } catch (RuntimeException ex) {
                logger.warning("Error validando token JWT: " + ex.getMessage());
                throw new UnauthorizedException("Token de autenticación inválido o expirado", ex);
            }
        }
        
        // Fallback disponible solo cuando el servidor se ejecuta con el perfil de pruebas.
        if (allowTestUserHeader && userIdHeader != null && !userIdHeader.trim().isEmpty()) {
            logger.info("Usando fallback X-User-Id (development mode)");
            return userIdHeader.trim();
        }
        
        throw new UnauthorizedException(
            "Usuario no autenticado. Proporcionar token JWT en header Authorization " +
            "o X-User-Id para testing (development mode)"
        );
    }

    /**
     * Extrae el token del header Authorization.
     * 
     * Espera formato: "Bearer <token>"
     * 
     * @param authHeader valor del header Authorization
     * @return token sin el prefijo "Bearer "
     * @throws IllegalArgumentException si el formato es inválido
     */
    private String extraerTokenDelHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header debe tener formato 'Bearer <token>'");
        }
        
        String token = authHeader.substring("Bearer ".length()).trim();
        
        if (token.isEmpty()) {
            throw new IllegalArgumentException("Token vacío en Authorization header");
        }
        
        return token;
    }
}
