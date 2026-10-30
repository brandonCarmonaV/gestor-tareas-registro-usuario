package com.gestortareas.paneles.application.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.gestortareas.paneles.domain.exception.ValidationException;
import com.gestortareas.paneles.domain.model.Usuario;
import com.gestortareas.paneles.domain.model.entity.Panel;
import com.gestortareas.paneles.domain.model.entity.Tablero;
import com.gestortareas.paneles.domain.port.PanelRepository;
import com.gestortareas.paneles.domain.port.TableroRepository;
import com.gestortareas.paneles.domain.port.out.UsuarioServicePort;
import com.gestortareas.paneles.infrastructure.adapter.out.webhook.ReporteWebhookClientAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TableroService {

	private static final String NOMBRE_TABLERO_POR_DEFECTO = "Mi tablero";

	private final TableroRepository tableroRepository;
	private final PanelRepository panelRepository;
	private final ReporteWebhookClientAdapter webhookClient;
	private final UsuarioServicePort usuarioService;

	public Tablero crearTablero(String nombre, String propietarioId) {

		try {
			Tablero tableroNuevo = new Tablero(null, nombre, propietarioId, LocalDateTime.now());

			return tableroRepository.crear(tableroNuevo);

		} catch (IllegalArgumentException ex) {
			log.warn("Validación fallida al crear tablero: " + ex.getMessage());
			throw new ValidationException("Error de validación al crear tablero: " + ex.getMessage(), ex);
		}
	}

	public List<Tablero> listarTableros(String usuarioId) {
		List<Tablero> tableros = new ArrayList<>(tableroRepository.listarPorPropietario(usuarioId));

		if (tableros.isEmpty()) {
			log.info("El usuario no tiene tableros, se crea el tablero por defecto");
			Tablero tableroPorDefecto = crearTablero(NOMBRE_TABLERO_POR_DEFECTO, usuarioId);
			moverPanelesSinTablero(tableroPorDefecto);
			tableros.add(tableroPorDefecto);
		}

		tableros.addAll(tableroRepository.listarPorMiembro(usuarioId));

		return tableros;
	}

	public Tablero obtenerTableroConAcceso(String tableroId, String usuarioId) {
		if (tableroId == null || tableroId.trim().isEmpty()) {
			throw new IllegalArgumentException("El tablero es obligatorio");
		}

		Tablero tablero = tableroRepository.buscarPorId(tableroId)
				.orElseThrow(() -> new IllegalArgumentException("Tablero no encontrado: " + tableroId));

		tablero.validarAcceso(usuarioId);

		return tablero;
	}

	public Usuario agregarMiembro(String tableroId, String email, String usuarioId, String token) {
		Tablero tablero = tableroRepository.buscarPorId(tableroId)
				.orElseThrow(() -> new IllegalArgumentException("Tablero no encontrado: " + tableroId));

		tablero.validarPropietario(usuarioId);

		Usuario nuevoMiembro = usuarioService.buscarPorEmail(email.trim(), token)
				.orElseThrow(() -> new NoSuchElementException("No existe un usuario con el correo: " + email));

		tablero.agregarMiembro(nuevoMiembro.id());
		tableroRepository.actualizar(tablero);

		return nuevoMiembro;
	}

	public void quitarMiembro(String tableroId, String miembroId, String usuarioId) {
		Tablero tablero = tableroRepository.buscarPorId(tableroId)
				.orElseThrow(() -> new IllegalArgumentException("Tablero no encontrado: " + tableroId));

		tablero.validarPropietario(usuarioId);

		tablero.quitarMiembro(miembroId);
		tableroRepository.actualizar(tablero);
	}

	public List<Usuario> listarParticipantes(Tablero tablero, String token) {
		List<String> participantesIds = new ArrayList<>();
		participantesIds.add(tablero.getPropietarioId());
		participantesIds.addAll(tablero.getMiembrosIds());

		List<Usuario> participantes = new ArrayList<>();
		for (String participanteId : participantesIds) {
			Usuario participante = usuarioService.buscarPorId(participanteId, token)
					.orElse(new Usuario(participanteId, "Usuario no disponible", null));
			participantes.add(participante);
		}

		return participantes;
	}

	public void eliminarTablero(String tableroId, String usuarioId) {
		Tablero tablero = tableroRepository.buscarPorId(tableroId)
				.orElseThrow(() -> new IllegalArgumentException("Tablero no encontrado: " + tableroId));

		tablero.validarPropietario(usuarioId);

		List<Panel> paneles = panelRepository.listarPorTablero(tableroId);
		for (Panel panel : paneles) {
			panelRepository.eliminar(panel.getId());
			webhookClient.notificarEliminacionPanel(panel);
		}

		tableroRepository.eliminar(tableroId);
	}

	private void moverPanelesSinTablero(Tablero tablero) {
		List<Panel> panelesSinTablero = panelRepository.listarSinTableroPorPropietario(tablero.getPropietarioId());

		for (Panel panel : panelesSinTablero) {
			panel.setTableroId(tablero.getId());
			panelRepository.actualizar(panel);
		}

		log.info("Se movieron " + panelesSinTablero.size() + " paneles al tablero por defecto");
	}
}
