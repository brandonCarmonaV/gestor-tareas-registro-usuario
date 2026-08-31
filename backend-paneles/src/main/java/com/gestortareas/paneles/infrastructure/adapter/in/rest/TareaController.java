package com.gestortareas.paneles.infrastructure.adapter.in.rest;

import com.gestortareas.paneles.application.service.PanelService;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.TareaRequestDTO;
import com.gestortareas.paneles.infrastructure.adapter.in.rest.dto.TareaResponseDTO;
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
@RequestMapping("/api/tareas")
public class TareaController {

    private final PanelService panelService;

    public TareaController(PanelService panelService) {
        this.panelService = panelService;
    }

    @PostMapping
    public ResponseEntity<TareaResponseDTO> crear(@Valid @RequestBody TareaRequestDTO request) {
        throw new UnsupportedOperationException("TODO: conectar DTO con caso de uso");
    }

    @GetMapping("/panel/{panelId}")
    public List<TareaResponseDTO> listar(@PathVariable Long panelId) {
        throw new UnsupportedOperationException("TODO: conectar DTO con caso de uso");
    }
}
