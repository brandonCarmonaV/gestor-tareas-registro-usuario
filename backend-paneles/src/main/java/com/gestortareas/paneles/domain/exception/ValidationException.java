package com.gestortareas.paneles.domain.exception;

public class ValidationException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;

	public ValidationException(String mensaje) {
        super(mensaje);
    }

    public ValidationException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
