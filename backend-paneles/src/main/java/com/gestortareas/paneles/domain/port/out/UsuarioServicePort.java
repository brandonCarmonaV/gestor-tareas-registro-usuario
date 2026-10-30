package com.gestortareas.paneles.domain.port.out;

import java.util.Optional;

import com.gestortareas.paneles.domain.model.Usuario;

public interface UsuarioServicePort {
    Optional<Usuario> buscarPorEmail(String email, String token);
    Optional<Usuario> buscarPorId(String usuarioId, String token);
}
