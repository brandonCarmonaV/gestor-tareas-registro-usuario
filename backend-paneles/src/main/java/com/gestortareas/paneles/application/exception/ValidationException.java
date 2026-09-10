package com.gestortareas.paneles.application.exception;

public class ValidationException extends RuntimeException {
    
    public ValidationException(String mensaje) {
        super(mensaje);
    }

    public ValidationException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
