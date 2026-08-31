package com.gestortareas.paneles.application.exception;

/**
 * Excepción lanzada cuando la validación de datos de negocio falla.
 * Se diferencia de IllegalArgumentException porque incluye contexto específico
 * del dominio de paneles.
 */
public class ValidationException extends RuntimeException {
    
    public ValidationException(String mensaje) {
        super(mensaje);
    }

    public ValidationException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
