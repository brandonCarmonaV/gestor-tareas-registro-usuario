package com.gestortareas.paneles.infrastructure.adapter.out.auth;

import com.gestortareas.paneles.domain.port.out.AuthServicePort;
import com.gestortareas.paneles.infrastructure.config.RmiConfig;
import org.springframework.stereotype.Component;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

/**
 * Adapter RMI que implementa AuthServicePort.
 * 
 * Responsabilidades:
 * - Conectar con el backend de autenticación vía RMI
 * - Validar tokens JWT contra el servicio remoto
 * - Extraer identidad del usuario autenticado
 * - Manejar errores de comunicación RMI
 * 
 * Flujo:
 * 1. En cada validación, hace Naming.lookup() al registry remoto de Auth
 * 2. Llama a validarToken() en AuthRemoteService
 * 3. Retorna el userId o lanza excepción
 * 
 * Nota: Se asume que el backend de Auth está disponible en authHost:authPort
 * y expone un servicio con nombre "AuthService" en su RMI registry.
 */
@Component
public class AuthRmiClientAdapter implements AuthServicePort {

    private static final Logger logger = Logger.getLogger(AuthRmiClientAdapter.class.getName());
    
    private final RmiConfig rmiConfig;
    private AuthRemoteService authRemoteService;
    private boolean initialized = false;

    public AuthRmiClientAdapter(RmiConfig rmiConfig) {
        this.rmiConfig = rmiConfig;
    }

    /**
     * Valida un token de autenticación usando el backend remoto de Auth.
     * 
     * Flujo:
     * 1. Obtiene conexión al servicio remoto de Auth (lazy initialization)
     * 2. Llama a validarToken(token) en el servicio remoto
     * 3. Retorna el ID del usuario si es válido
     * 4. Lanza RuntimeException si hay error
     * 
     * @param token token de autenticación a validar
     * @return ID del usuario autenticado
     * @throws RuntimeException si el token es inválido o hay error de comunicación
     */
    @Override
    public String validarUsuario(String token) {
        try {
            // Validar que el token no sea null o vacío
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("Token de autenticación no puede ser null o vacío");
            }

            logger.info("Validando token contra backend de Auth remoto...");

            // Obtener conexión al servicio remoto (lazy initialization)
            AuthRemoteService authService = obtenerAuthRemoteService();

            // Llamar al servicio remoto para validar token
            String userId = authService.validarToken(token);

            if (userId == null || userId.trim().isEmpty()) {
                logger.warning("Backend de Auth retornó userId inválido");
                throw new RuntimeException("Backend de Auth no retornó userId válido");
            }

            logger.info("Token validado exitosamente. userId=" + userId);
            return userId;

        } catch (IllegalArgumentException ex) {
            logger.warning("Validación fallida: " + ex.getMessage());
            throw new RuntimeException("Error de validación: " + ex.getMessage(), ex);
        } catch (RemoteException ex) {
            logger.severe("Error en comunicación RMI con backend de Auth: " + ex.getMessage());
            throw new RuntimeException(
                "No se pudo comunicar con el backend de autenticación remoto. " +
                "Verifica que el servicio esté disponible en " +
                rmiConfig.authHost() + ":" + rmiConfig.authPort(),
                ex
            );
        }
    }

    /**
     * Obtiene una conexión al servicio remoto de Auth.
     * 
     * Implementa lazy initialization para evitar fallar al startup si el servicio
     * de Auth no está disponible. Si la conexión falla, retorna una excepción
     * que será convertida a RuntimeException en validarUsuario().
     * 
     * Flujo:
     * 1. Si ya hay conexión establecida y validada, la retorna
     * 2. Si no, hace Naming.lookup() al registry del servicio de Auth
     * 3. Cachea la conexión para llamadas posteriores
     * 4. Lanza RemoteException si hay error
     * 
     * @return servicio remoto de Auth
     * @throws RemoteException si no se puede conectar al registry remoto
     */
    private AuthRemoteService obtenerAuthRemoteService() throws RemoteException {
        // Si ya está inicializado y la conexión es válida, retornarla
        if (initialized && authRemoteService != null) {
            try {
                // Intentar un ping simple para verificar que la conexión sigue viva
                // (opcional: podría saltarse si no queremos overhead)
                return authRemoteService;
            } catch (Exception ex) {
                logger.warning("Conexión al servicio remoto de Auth se perdió, reconectando...");
                initialized = false;
                authRemoteService = null;
                // Continuar con reconexión
            }
        }

        try {
            // Construir URL RMI
            String rmiUrl = "rmi://" + rmiConfig.authHost() + ":" + rmiConfig.authPort() + 
                           "/" + AuthRemoteService.SERVICE_NAME;
            
            logger.info("Conectando al backend de Auth remoto: " + rmiUrl);

            // Obtener registry y hacer lookup del servicio
            Registry registry = LocateRegistry.getRegistry(rmiConfig.authHost(), rmiConfig.authPort());
            authRemoteService = (AuthRemoteService) registry.lookup(AuthRemoteService.SERVICE_NAME);

            initialized = true;
            
            logger.info("✓ Conectado exitosamente al backend de Auth remoto");
            return authRemoteService;

        } catch (NotBoundException ex) {
            logger.severe("Servicio '" + AuthRemoteService.SERVICE_NAME + 
                         "' no está registrado en el backend de Auth. " +
                         "Verifica que AuthServiceImpl esté corriendo en " +
                         rmiConfig.authHost() + ":" + rmiConfig.authPort());
            throw new RemoteException(
                "Servicio de Auth no encontrado en registry remoto: " + ex.getMessage(),
                ex
            );
        } catch (RemoteException ex) {
            logger.severe("Error al conectar con registry remoto de Auth en " +
                         rmiConfig.authHost() + ":" + rmiConfig.authPort() + 
                         " - " + ex.getMessage());
            throw ex;
        }
    }
}
