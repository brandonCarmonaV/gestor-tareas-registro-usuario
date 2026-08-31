package com.gestortareas.paneles.infrastructure.adapter.in.rest;

import com.gestortareas.paneles.application.service.PanelService;
import com.gestortareas.paneles.domain.model.EstadoPanel;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.PanelRequestDTO;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.PanelResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/paneles")
public class PanelController {

    private final PanelService panelService;

    public PanelController(PanelService panelService) {
        this.panelService = panelService;
    }

    @PostMapping
    public ResponseEntity<PanelResponseDTO> crearPanel(@Valid @RequestBody PanelRequestDTO request) {
        // TODO: Mapear PanelRequestDTO a Panel y llamar a panelService.crearPanel()
        // TODO: Mapear resultado a PanelResponseDTO
        throw new UnsupportedOperationException("TODO: conectar DTO con caso de uso");
    }

    @GetMapping
    public ResponseEntity<List<PanelResponseDTO>> listarPaneles(@RequestParam String propietarioId) {
        // TODO: Llamar a panelService.listarPaneles(propietarioId)
        // TODO: Mapear lista de Panel a lista de PanelResponseDTO
        throw new UnsupportedOperationException("TODO: conectar DTO con caso de uso");
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<PanelResponseDTO> actualizarEstado(@PathVariable String id, @RequestBody EstadoPanel nuevoEstado) {
        // TODO: Llamar a panelService.actualizarEstado(id, nuevoEstado)
        // TODO: Mapear resultado a PanelResponseDTO
        throw new UnsupportedOperationException("TODO: conectar DTO con caso de uso");
    }
}
