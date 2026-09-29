package com.gestortareas.paneles.infrastructure.adapter.out.auth;

import com.gestortareas.paneles.domain.port.out.AuthServicePort;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class AuthRmiClientAdapter implements AuthServicePort {

    @Value("${auth.host}")
    private String authHost;

    @Value("${auth.port}")
    private String authPort;

    private static final Logger logger = Logger.getLogger(AuthRmiClientAdapter.class.getName());

    private final RestTemplate restTemplate;

    public AuthRmiClientAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String validarUsuario(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("Token de autenticación no puede ser null o vacío");
            }

            logger.info("Validando token contra backend de Auth remoto...");

            Map<String, String> params = new HashMap<>();
            params.put("token", token);

            ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                    authHost + authPort + "/extract?token=" + token,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, String>>() {
                    });

            String userId = response.getBody().get("id");

            if (userId == null || userId.trim().isEmpty()) {
                logger.warning("Backend de Auth retornó userId inválido");
                throw new RuntimeException("Backend de Auth no retornó userId válido");
            }

            logger.info("Token validado exitosamente. userId=" + userId);
            return userId;

        } catch (IllegalArgumentException ex) {
            logger.warning("Validación fallida: " + ex.getMessage());
            throw new RuntimeException("Error de validación: " + ex.getMessage(), ex);
        } catch (RestClientException ex) {
            logger.severe("Error en comunicación con backend de Auth: " + ex.getMessage());
            throw new RuntimeException(
                    "No se pudo comunicar con el backend de autenticación remoto. " +
                            "Verifica que el servicio esté disponible en " +
                            authHost + authPort,
                    ex);
        }
    }
}
