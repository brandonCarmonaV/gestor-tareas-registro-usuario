package com.gestortareas.paneles.infrastructure.adapter.out.rmi;

import com.gestortareas.paneles.domain.port.out.AuthServicePort;
import org.springframework.stereotype.Component;

import java.rmi.Naming;

@Component
public class AuthRmiClientAdapter implements AuthServicePort {

    @Override
    public boolean validarUsuario(Long usuarioId) {
        throw new UnsupportedOperationException("TODO: resolver servicio Auth mediante Naming.lookup");
    }
}
