package com.gestortareas.paneles.infrastructure.adapter.out.webhook;

import com.gestortareas.paneles.domain.model.Panel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class ReporteWebhookClientAdapter {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.reportes.url}")
    private String urlReportes;

    public void notificarCambioPanel(Panel panel) {
        try {
            WebhookComandoPayload payload = new WebhookComandoPayload(
                    panel.getId(),
                    panel.getPropietarioId(),
                    panel.getEstado() != null ? panel.getEstado().name() : null,
                    panel.getFechaInicio(),
                    panel.getFechaFin(),
                    panel.getFechaCompletado()
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<WebhookComandoPayload> request = new HttpEntity<>(payload, headers);
            
            restTemplate.postForEntity(urlReportes, request, Void.class);
            
        } catch (Exception e) {
            System.err.println("No se pudo enviar el webhook al microservicio de reportes: " + e.getMessage());
        }
    }

    public void notificarEliminacionPanel(Panel panel) {
        try {
            WebhookComandoPayloadEliminar payload = new WebhookComandoPayloadEliminar(
                    panel.getId()
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<WebhookComandoPayloadEliminar> request = new HttpEntity<>(payload, headers);
            
            restTemplate.postForEntity(urlReportes + "/eliminacion", request, Void.class);
            
        } catch (Exception e) {
            System.err.println("No se pudo enviar el webhook de eliminación al microservicio de reportes: " + e.getMessage());
        }
    }

    private record WebhookComandoPayload(
            String panelIdOriginal,
            String propietarioId,
            String estado,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            LocalDateTime fechaCompletado
    ) {}

    private record WebhookComandoPayloadEliminar(
        String panelIdOriginal
    ){}
}