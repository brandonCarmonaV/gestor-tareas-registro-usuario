package com.gestortareas.paneles.infrastructure.adapter.out.rmi;

import com.gestortareas.paneles.domain.port.out.AuthServicePort;
import org.springframework.stereotype.Component;

import java.rmi.Naming;

@Component
public class AuthRmiClientAdapter implements AuthServicePort {

    @Override
    public String validarUsuario(String token) {
        // TODO: Usar Naming.lookup() para resolver el servicio remoto de Auth
        // TODO: Llamar al método remoto pasando el token
        // TODO: Retornar el ID del usuario autenticado o lanzar excepción si el token es inválido
        throw new UnsupportedOperationException("TODO: resolver servicio Auth mediante Naming.lookup y validar token");
    }
}
