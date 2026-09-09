package com.gestortareas.paneles.infrastructure.adapter.in.rest;

import com.gestortareas.paneles.application.exception.UnauthorizedException;
import com.gestortareas.paneles.application.exception.ValidationException;
import com.gestortareas.paneles.application.service.PanelService;
import com.gestortareas.paneles.domain.model.Panel;
import com.gestortareas.paneles.domain.port.out.AuthServicePort;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.ActualizarPanelRequestDTO;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.PanelRequestDTO;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.PanelResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping
    public ResponseEntity<PanelResponseDTO> crearPanel(
            @Valid @RequestBody PanelRequestDTO request,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        
        try {
            String propietarioId = extraerPropietarioId(authHeader, userIdHeader);
            
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

    @PutMapping("/{id}")
    public ResponseEntity<PanelResponseDTO> actualizarPanel(
            @PathVariable("id") String panelId,
            @Valid @RequestBody ActualizarPanelRequestDTO request,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        
        try {
            String propietarioId = extraerPropietarioId(authHeader, userIdHeader);

            Panel panelActualizado = panelService.actualizarPanel(
                    panelId, request.getNombre(), request.getColor(),
                    request.getFechaInicio(), request.getFechaFin(),
                    request.getPrioridad(), request.getEstado(), propietarioId);
            
            PanelResponseDTO response = PanelMapper.toPanelResponseDTO(panelActualizado);
            return ResponseEntity.ok(response);
            
        } catch (UnauthorizedException ex) {
            logger.warning("Usuario no autorizado para actualizar panel: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            
        } catch (IllegalArgumentException ex) {
            logger.warning("Error al actualizar panel: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPanel(
            @PathVariable("id") String panelId,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        try {
            String propietarioId = extraerPropietarioId(authHeader, userIdHeader);
            panelService.eliminarPanel(panelId, propietarioId);
            return ResponseEntity.noContent().build();
        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    private String extraerPropietarioId(String authHeader, String userIdHeader) {
        if (authHeader != null && !authHeader.trim().isEmpty()) {
            try {
                String token = extraerTokenDelHeader(authHeader);
                
                logger.info("Validando token JWT contra backend de Auth remoto...");
                
                String propietarioId = authService.validarUsuario(token);
                
                logger.info("Token validado exitosamente. propietarioId=" + propietarioId);
                return propietarioId;
                
            } catch (RuntimeException ex) {
                logger.warning("Error validando token JWT: " + ex.getMessage());
                throw new UnauthorizedException("Token de autenticación inválido o expirado", ex);
            }
        }
        
        if (allowTestUserHeader && userIdHeader != null && !userIdHeader.trim().isEmpty()) {
            logger.info("Usando fallback X-User-Id (development mode)");
            return userIdHeader.trim();
        }
        
        throw new UnauthorizedException(
            "Usuario no autenticado. Proporcionar token JWT en header Authorization " +
            "o X-User-Id para testing (development mode)"
        );
    }

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
