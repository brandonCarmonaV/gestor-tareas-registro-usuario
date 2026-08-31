package com.gestortareas.paneles.infrastructure.adapter.in.rest;

import com.gestortareas.paneles.application.service.PanelService;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.PanelRequestDTO;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.PanelResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<PanelResponseDTO> crear(@Valid @RequestBody PanelRequestDTO request) {
        throw new UnsupportedOperationException("TODO: conectar DTO con caso de uso");
    }

    @GetMapping("/propietario/{propietarioId}")
    public List<PanelResponseDTO> listar(@PathVariable Long propietarioId) {
        throw new UnsupportedOperationException("TODO: conectar DTO con caso de uso");
    }
}
