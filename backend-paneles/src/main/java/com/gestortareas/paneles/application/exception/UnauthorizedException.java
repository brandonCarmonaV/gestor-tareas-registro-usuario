package com.gestortareas.paneles.application.exception;

/**
 * Excepción lanzada cuando un usuario intenta realizar una operación
 * para la cual no tiene permisos o autorización.
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String mensaje) {
        super(mensaje);
    }

    public UnauthorizedException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
