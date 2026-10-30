package com.gestortareas.paneles.infrastructure.adapter.in.rest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestortareas.paneles.application.models.dto.MiembroRequestDTO;
import com.gestortareas.paneles.application.models.dto.TableroRequestDTO;
import com.gestortareas.paneles.application.service.TableroService;
import com.gestortareas.paneles.domain.exception.UnauthorizedException;
import com.gestortareas.paneles.domain.exception.ValidationException;
import com.gestortareas.paneles.domain.model.Usuario;
import com.gestortareas.paneles.domain.model.entity.Tablero;
import com.gestortareas.paneles.domain.port.out.AuthServicePort;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.ParticipanteResponseDTO;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.TableroResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/tableros")
public class TableroController {

    private final TableroService tableroService;
    private final AuthServicePort authService;

    @PostMapping
    public ResponseEntity<TableroResponseDTO> crearTablero(
            @Valid @RequestBody TableroRequestDTO request,
            @CookieValue(value = "access_token", required = false) String cookieToken) {

        try {
            String usuarioId = extraerUsuarioId(cookieToken);
            Tablero tableroCreado = tableroService.crearTablero(request.getNombre(), usuarioId);
            TableroResponseDTO response = TableroMapper.toTableroResponseDTO(tableroCreado, usuarioId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (ValidationException | IllegalArgumentException ex) {
            log.warn("Error de validación al crear tablero: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (UnauthorizedException ex) {
            log.warn("Usuario no autorizado para crear tablero: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<TableroResponseDTO>> listarTableros(
            @CookieValue(value = "access_token", required = false) String cookieToken) {

        try {
            String usuarioId = extraerUsuarioId(cookieToken);

            List<Tablero> tableros = tableroService.listarTableros(usuarioId);

            List<TableroResponseDTO> response = tableros.stream()
                    .map(tablero -> TableroMapper.toTableroResponseDTO(tablero, usuarioId))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (UnauthorizedException ex) {
            log.warn("Usuario no autorizado para listar tableros: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTablero(
            @PathVariable("id") String tableroId,
            @CookieValue(value = "access_token", required = false) String cookieToken) {
        try {
            String usuarioId = extraerUsuarioId(cookieToken);
            tableroService.eliminarTablero(tableroId, usuarioId);
            return ResponseEntity.noContent().build();
        } catch (UnauthorizedException ex) {
            log.warn("Usuario no autorizado para eliminar tablero: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/miembros")
    public ResponseEntity<List<ParticipanteResponseDTO>> listarParticipantes(
            @PathVariable("id") String tableroId,
            @CookieValue(value = "access_token", required = false) String cookieToken) {
        try {
            String usuarioId = extraerUsuarioId(cookieToken);
            Tablero tablero = tableroService.obtenerTableroConAcceso(tableroId, usuarioId);

            List<ParticipanteResponseDTO> response = tableroService.listarParticipantes(tablero, cookieToken).stream()
                    .map(participante -> TableroMapper.toParticipanteResponseDTO(participante, tablero))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException ex) {
            log.warn("Usuario no autorizado para ver participantes: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/miembros")
    public ResponseEntity<ParticipanteResponseDTO> agregarMiembro(
            @PathVariable("id") String tableroId,
            @Valid @RequestBody MiembroRequestDTO request,
            @CookieValue(value = "access_token", required = false) String cookieToken) {
        try {
            String usuarioId = extraerUsuarioId(cookieToken);
            Usuario miembro = tableroService.agregarMiembro(tableroId, request.getEmail(), usuarioId, cookieToken);

            ParticipanteResponseDTO response = new ParticipanteResponseDTO(miembro.id(), miembro.nombre(),
                    miembro.email(), false);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UnauthorizedException ex) {
            log.warn("Usuario no autorizado para agregar miembros: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (NoSuchElementException ex) {
            log.warn("Error al agregar miembro: " + ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            log.warn("Error de validación al agregar miembro: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}/miembros/{usuarioId}")
    public ResponseEntity<Void> quitarMiembro(
            @PathVariable("id") String tableroId,
            @PathVariable("usuarioId") String miembroId,
            @CookieValue(value = "access_token", required = false) String cookieToken) {
        try {
            String usuarioId = extraerUsuarioId(cookieToken);
            tableroService.quitarMiembro(tableroId, miembroId, usuarioId);
            return ResponseEntity.noContent().build();
        } catch (UnauthorizedException ex) {
            log.warn("Usuario no autorizado para quitar miembros: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IllegalArgumentException ex) {
            log.warn("Error al quitar miembro: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private String extraerUsuarioId(String cookieToken) {
        if (cookieToken != null && !cookieToken.trim().isEmpty()) {
            try {
                return authService.validarUsuario(cookieToken);
            } catch (RuntimeException ex) {
                throw new UnauthorizedException("Token en cookie inválido o expirado", ex);
            }
        }

        throw new UnauthorizedException("Usuario no autenticado (falta cookie 'access_token')");
    }
}
