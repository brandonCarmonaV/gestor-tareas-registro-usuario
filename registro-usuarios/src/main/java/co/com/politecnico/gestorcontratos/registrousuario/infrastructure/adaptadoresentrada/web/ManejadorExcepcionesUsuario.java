package co.com.politecnico.gestorcontratos.registrousuario.infrastructure.adaptadoresentrada.web;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.com.politecnico.gestorcontratos.registrousuario.domain.excepcion.ExcepcionGeneralesReglaNegocio;

/**
 * Traduce las excepciones de dominio a respuestas HTTP entendibles
 * por el frontend.
 */
@RestControllerAdvice(basePackages = "co.com.politecnico.gestorcontratos.registrousuario.infrastructure.adaptadoresentrada.web")
public class ManejadorExcepcionesUsuario {

    @ExceptionHandler(ExcepcionGeneralesReglaNegocio.class)
    public ResponseEntity<Map<String, String>> manejarExcepcionNegocio(ExcepcionGeneralesReglaNegocio ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensaje", ex.getMessage()));
    }
}
