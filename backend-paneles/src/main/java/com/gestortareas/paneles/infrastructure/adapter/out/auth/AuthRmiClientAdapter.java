package com.gestortareas.paneles.infrastructure.adapter.out.auth;

import com.gestortareas.paneles.domain.port.out.AuthServicePort;
import com.gestortareas.paneles.infrastructure.config.RmiConfig;
import rmi.shared.AuthRmiPort;
import org.springframework.stereotype.Component;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class AuthRmiClientAdapter implements AuthServicePort {

    private static final Logger logger = Logger.getLogger(AuthRmiClientAdapter.class.getName());
    private static final String SERVICE_NAME = "AuthService";
    
    private final RmiConfig rmiConfig;
    private AuthRmiPort authRemoteService;
    private boolean initialized = false;

    public AuthRmiClientAdapter(RmiConfig rmiConfig) {
        this.rmiConfig = rmiConfig;
    }

    @Override
    public String validarUsuario(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("Token de autenticación no puede ser null o vacío");
            }

            logger.info("Validando token contra backend de Auth remoto...");

            AuthRmiPort authService = obtenerAuthRemoteService();

            Map<String, String> subjectData = authService.extractSubject(token);
            String userId = extraerUserId(subjectData);

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

    private AuthRmiPort obtenerAuthRemoteService() throws RemoteException {
        if (initialized && authRemoteService != null) {
            try {
                return authRemoteService;
            } catch (Exception ex) {
                logger.warning("Conexión al servicio remoto de Auth se perdió, reconectando...");
                initialized = false;
                authRemoteService = null;
            }
        }

        try {
            String rmiUrl = "rmi://" + rmiConfig.authHost() + ":" + rmiConfig.authPort() + 
                           "/" + SERVICE_NAME;
            
            logger.info("Conectando al backend de Auth remoto: " + rmiUrl);

            Registry registry = LocateRegistry.getRegistry(rmiConfig.authHost(), rmiConfig.authPort());
            authRemoteService = (AuthRmiPort) registry.lookup(SERVICE_NAME);

            initialized = true;
            
            logger.info("✓ Conectado exitosamente al backend de Auth remoto");
            return authRemoteService;

        } catch (NotBoundException ex) {
            logger.severe("Servicio '" + SERVICE_NAME + 
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

    private String extraerUserId(Map<String, String> subjectData) {
        if (subjectData == null || subjectData.isEmpty()) {
            return null;
        }

        for (String key : new String[] {"userId", "user_id", "subject", "sub", "id"}) {
            String value = subjectData.get(key);
            if (value != null && !value.trim().isEmpty()) {
                return value.trim();
            }
        }

        return null;
    }
}
