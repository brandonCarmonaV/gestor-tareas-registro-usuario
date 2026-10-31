package com.gestortareas.paneles.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gestortareas.paneles.application.models.dto.PanelRequestDTO;
import com.gestortareas.paneles.domain.exception.UnauthorizedException;
import com.gestortareas.paneles.domain.exception.ValidationException;
import com.gestortareas.paneles.domain.model.EstadoPanelEnum;
import com.gestortareas.paneles.domain.model.entity.Panel;
import com.gestortareas.paneles.domain.model.entity.Tablero;
import com.gestortareas.paneles.domain.port.PanelRepository;
import com.gestortareas.paneles.infrastructure.adapter.out.webhook.ReporteWebhookClientAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PanelService {

	private final PanelRepository panelRepository;
    private final ReporteWebhookClientAdapter webhookClient;
	private final TableroService tableroService;

	public Panel crearPanel(PanelRequestDTO panelRequest, String propietarioId) {

		try {
			Panel panelNuevo = new Panel(null, panelRequest.getNombre(), panelRequest.getColor(),
					EstadoPanelEnum.PENDIENTE, panelRequest.getFechaInicio(), panelRequest.getFechaFin(),
					panelRequest.getPrioridad(), propietarioId, LocalDateTime.now(), panelRequest.getDescripcion());

			Tablero tablero = tableroService.obtenerTableroConAcceso(panelRequest.getTableroId(), propietarioId);
			panelNuevo.setTableroId(panelRequest.getTableroId());
			panelNuevo.asignarResponsable(panelRequest.getResponsableId());
			tablero.validarResponsable(panelNuevo.getResponsableId());

			Panel panelGuardado = panelRepository.crearPanel(panelNuevo);
			webhookClient.notificarCambioPanel(panelGuardado);

			return panelGuardado;

		} catch (IllegalArgumentException ex) {
			log.warn("Validación fallida al crear panel: " + ex.getMessage());
			throw new ValidationException("Error de validación al crear panel: " + ex.getMessage(), ex);
		}
	}

	public List<Panel> listarPaneles(String propietarioId) {
		List<Panel> paneles = panelRepository.listarPorPropietario(propietarioId);
		return paneles;
	}

	public List<Panel> listarPanelesPorTablero(String tableroId, String usuarioId) {
		tableroService.obtenerTableroConAcceso(tableroId, usuarioId);
		return panelRepository.listarPorTablero(tableroId);
	}

	public Panel actualizarPanel(PanelRequestDTO panelRequest, String propietarioId) {
		
		Panel panel = panelRepository.buscarPorId(panelRequest.getPanelId()).orElseThrow(() -> {
			return new IllegalArgumentException("Panel no encontrado: " + panelRequest.getPanelId());
		});

		if (panel.getTableroId() != null) {
			Tablero tablero = tableroService.obtenerTableroConAcceso(panel.getTableroId(), propietarioId);
			panel.asignarResponsable(panelRequest.getResponsableId());
			tablero.validarResponsable(panel.getResponsableId());
		}

		panel.actualizarDatos(panelRequest.getNombre(), panelRequest.getColor(), panelRequest.getFechaInicio(), panelRequest.getFechaFin(),
				panelRequest.getPrioridad(), panelRequest.getEstado(), propietarioId, panelRequest.getDescripcion());
		webhookClient.notificarCambioPanel(panel);
		return panelRepository.actualizar(panel);

	}

	public void eliminarPanel(String panelId, String propietarioId) {
		Panel panel = panelRepository.buscarPorId(panelId)
				.orElseThrow(() -> new IllegalArgumentException("Panel no encontrado: " + panelId));

		if (panel.getTableroId() != null) {
			Tablero tablero = tableroService.obtenerTableroConAcceso(panel.getTableroId(), propietarioId);
			tablero.validarPropietario(propietarioId);
		} else if (!panel.getPropietarioId().equals(propietarioId)) {
			throw new UnauthorizedException("No tienes permisos para eliminar este panel");
		}

		panelRepository.eliminar(panelId);
		webhookClient.notificarEliminacionPanel(panel);
	}
}
