package com.gestortareas.paneles.infrastructure.adapter.in.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestortareas.paneles.application.models.dto.PanelRequestDTO;
import com.gestortareas.paneles.application.service.PanelService;
import com.gestortareas.paneles.domain.exception.UnauthorizedException;
import com.gestortareas.paneles.domain.exception.ValidationException;
import com.gestortareas.paneles.domain.model.entity.Panel;
import com.gestortareas.paneles.domain.port.out.AuthServicePort;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.PanelResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/paneles")
public class PanelController {

    private final PanelService panelService;
    private final AuthServicePort authService;
    private final boolean allowTestUserHeader;

    @PostMapping
    public ResponseEntity<PanelResponseDTO> crearPanel(
            @Valid @RequestBody PanelRequestDTO request,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @CookieValue(value = "access_token", required = false) String cookieToken) {
        
        try {
            String propietarioId = extraerPropietarioId(userIdHeader, cookieToken);
            Panel panelCreado = panelService.crearPanel(request, propietarioId);
            PanelResponseDTO response = PanelMapper.toPanelResponseDTO(panelCreado);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (ValidationException | IllegalArgumentException ex) {
            log.warn("Error de validación al crear panel: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (UnauthorizedException ex) {
        	log.warn("Usuario no autorizado para crear panel: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PanelResponseDTO>> listarPaneles(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @CookieValue(value = "access_token", required = false) String cookieToken) {
        
        try {
            String propietarioId = extraerPropietarioId(userIdHeader, cookieToken);
            
            List<Panel> paneles = panelService.listarPaneles(propietarioId);
            
            List<PanelResponseDTO> response = paneles.stream()
                    .map(PanelMapper::toPanelResponseDTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
            
        } catch (UnauthorizedException ex) {
        	log.warn("Usuario no autorizado para listar paneles: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IllegalArgumentException ex) {
        	log.warn("Error al listar paneles: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PanelResponseDTO> actualizarPanel(
            @PathVariable("id") String panelId,
            @Valid @RequestBody PanelRequestDTO request,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @CookieValue(value = "access_token", required = false) String cookieToken) {
        
        try {
            String propietarioId = extraerPropietarioId(userIdHeader, cookieToken);

            Panel panelActualizado = panelService.actualizarPanel(request, propietarioId);
            
            PanelResponseDTO response = PanelMapper.toPanelResponseDTO(panelActualizado);
            return ResponseEntity.ok(response);
            
        } catch (UnauthorizedException ex) {
        	log.warn("Usuario no autorizado para actualizar panel: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IllegalArgumentException ex) {
        	log.warn("Error al actualizar panel: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPanel(
            @PathVariable("id") String panelId,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @CookieValue(value = "access_token", required = false) String cookieToken) {
        try {
            String propietarioId = extraerPropietarioId(userIdHeader, cookieToken);
            panelService.eliminarPanel(panelId, propietarioId);
            return ResponseEntity.noContent().build();
        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Extracción limpia basada exclusivamente en Cookies y Fallback de desarrollo
    private String extraerPropietarioId(String userIdHeader, String cookieToken) {
        // 1. Validar la cookie HttpOnly enviada automáticamente por el navegador
        if (cookieToken != null && !cookieToken.trim().isEmpty()) {
            try {
            	log.info("Validando token JWT obtenido desde la Cookie 'access_token'...");
                return authService.validarUsuario(cookieToken);
            } catch (RuntimeException ex) {
                throw new UnauthorizedException("Token en cookie inválido o expirado", ex);
            }
        }
        
        // 2. Fallback de desarrollo (X-User-Id) para pruebas manuales rápidas
        if (allowTestUserHeader && userIdHeader != null && !userIdHeader.trim().isEmpty()) {
        	log.info("Usando fallback X-User-Id (development mode)");
            return userIdHeader.trim();
        }
        
        throw new UnauthorizedException("Usuario no autenticado (falta cookie 'access_token' o header de prueba)");
    }
}