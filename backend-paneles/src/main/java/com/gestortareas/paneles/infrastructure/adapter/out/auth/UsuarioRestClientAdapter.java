package com.gestortareas.paneles.infrastructure.adapter.out.auth;

import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.gestortareas.paneles.domain.model.Usuario;
import com.gestortareas.paneles.domain.port.out.UsuarioServicePort;

@Component
public class UsuarioRestClientAdapter implements UsuarioServicePort {

    @Value("${auth.host}")
    private String authHost;

    @Value("${auth.port}")
    private String authPort;

    private static final Logger logger = Logger.getLogger(UsuarioRestClientAdapter.class.getName());

    private final RestTemplate restTemplate;

    public UsuarioRestClientAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email, String token) {
        return consultarUsuario("/api/users/email/" + email, token);
    }

    @Override
    public Optional<Usuario> buscarPorId(String usuarioId, String token) {
        return consultarUsuario("/api/users/" + usuarioId, token);
    }

    private Optional<Usuario> consultarUsuario(String ruta, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.COOKIE, "access_token=" + token);

            ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                    authHost + authPort + ruta,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<Map<String, String>>() {
                    });

            Map<String, String> body = response.getBody();

            return Optional.of(new Usuario(body.get("id"), body.get("name"), body.get("email")));

        } catch (HttpClientErrorException.NotFound ex) {
            logger.info("Usuario no encontrado en el backend de Auth: " + ruta);
            return Optional.empty();
        } catch (RestClientException ex) {
            logger.severe("Error en comunicación con backend de Auth: " + ex.getMessage());
            throw new RuntimeException("No se pudo consultar el usuario en el backend de autenticación", ex);
        }
    }
}
