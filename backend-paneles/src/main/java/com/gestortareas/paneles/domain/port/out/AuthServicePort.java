package com.gestortareas.paneles.domain.port.out;

/**
 * Puerto de salida (output port) que define el contrato para validación de autenticación.
 * Implementación: RMI client que se conecta al servicio de autenticación remoto.
 * 
 * Responsabilidades:
 * - Validar tokens JWT o equivalentes
 * - Extraer la identidad del usuario autenticado
 * - Lanzar excepciones si el token no es válido
 */
public interface AuthServicePort {
    
    /**
     * Valida un token de autenticación y retorna el ID del usuario autenticado.
     * 
     * @param token token de autenticación (ej: JWT) a validar
     * @return id del usuario autenticado (nunca null)
     * @throws RuntimeException si el token es inválido, expirado o malformado
     */
    String validarUsuario(String token);
}
