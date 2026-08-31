package com.gestortareas.paneles.infrastructure.adapter.out.auth;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz RMI para comunicación con el backend de autenticación remoto.
 * 
 * Define el contrato que el servidor de Auth implementa y que este backend
 * de Paneles consumirá como cliente RMI.
 * 
 * Responsabilidades:
 * - Validar tokens de autenticación
 * - Extraer identidad del usuario autenticado
 * - Lanzar excepciones si el token es inválido
 */
public interface AuthRemoteService extends Remote {
    
    /**
     * Nombre del servicio registrado en el RMI registry del backend de Auth.
     * Usado para hacer Naming.lookup("rmi://authHost:authPort/" + SERVICE_NAME)
     */
    String SERVICE_NAME = "AuthService";

    /**
     * Valida un token de autenticación contra el backend de Auth.
     * 
     * Flujo:
     * 1. El cliente (PanelService vía AuthRmiClientAdapter) envía el token
     * 2. El servidor de Auth valida el token
     * 3. Si es válido, retorna el ID del usuario autenticado
     * 4. Si no es válido, retorna null o lanza RemoteException
     * 
     * @param token token de autenticación (ej: JWT) a validar
     * @return ID del usuario autenticado (nunca null si el token es válido)
     * @throws RemoteException si hay error en la comunicación RMI o el token es inválido
     */
    String validarToken(String token) throws RemoteException;
}
