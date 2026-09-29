package com.gestortareas.paneles.application.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gestortareas.paneles.application.exception.UnauthorizedException;
import com.gestortareas.paneles.application.exception.ValidationException;
import com.gestortareas.paneles.application.models.dto.PanelRequestDTO;
import com.gestortareas.paneles.domain.model.EstadoPanelEnum;
import com.gestortareas.paneles.domain.model.entity.Panel;
import com.gestortareas.paneles.domain.port.PanelRepository;
import com.gestortareas.paneles.domain.port.out.PanelRepositoryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PanelService {

	private final PanelRepositoryPort panelRepositoryPort;
	private final PanelRepository panelRepository;

	public Panel crearPanel(PanelRequestDTO panelRequest, String propietarioId) {

		try {
			Panel panelNuevo = new Panel(null, panelRequest.getNombre(), panelRequest.getColor(),
					EstadoPanelEnum.PENDIENTE, panelRequest.getFechaInicio(), panelRequest.getFechaFin(),
					panelRequest.getPrioridad(), propietarioId, LocalDateTime.now(), panelRequest.getDescripcion());

			return panelRepository.crearPanel(panelNuevo);

		} catch (IllegalArgumentException ex) {
			log.warn("Validación fallida al crear panel: " + ex.getMessage());
			throw new ValidationException("Error de validación al crear panel: " + ex.getMessage(), ex);
		}
	}

	public List<Panel> listarPaneles(String propietarioId) {
		List<Panel> paneles = panelRepositoryPort.listarPorPropietario(propietarioId);

		log.info("Listados " + paneles.size() + " paneles del propietario: " + propietarioId);
		return paneles;
	}

	public Panel actualizarPanel(String panelId, String nombre, String color, LocalDate fechaInicio, LocalDate fechaFin,
			Integer prioridad, EstadoPanelEnum estado, String propietarioId, String descripcion) {
		Panel panel = panelRepositoryPort.buscarPorId(panelId).orElseThrow(() -> {
			log.warn("Intento de actualizar panel inexistente: " + panelId);
			return new IllegalArgumentException("Panel no encontrado: " + panelId);
		});

		panel.actualizarDatos(nombre, color, fechaInicio, fechaFin, prioridad, estado, propietarioId, descripcion);
		return panelRepositoryPort.actualizar(panel);

	}

	public void eliminarPanel(String panelId, String propietarioId) {
		Panel panel = panelRepositoryPort.buscarPorId(panelId)
				.orElseThrow(() -> new IllegalArgumentException("Panel no encontrado: " + panelId));

		if (!panel.getPropietarioId().equals(propietarioId)) {
			throw new UnauthorizedException("No tienes permisos para eliminar este panel");
		}

		panelRepositoryPort.eliminar(panelId);
	}

}
