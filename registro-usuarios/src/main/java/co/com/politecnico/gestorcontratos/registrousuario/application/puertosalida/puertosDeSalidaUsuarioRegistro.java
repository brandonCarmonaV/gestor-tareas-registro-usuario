package co.com.politecnico.gestorcontratos.registrousuario.application.puertosalida;

import java.util.Optional;

import co.com.politecnico.gestorcontratos.registrousuario.domain.modelo.Usuario;

public interface puertosDeSalidaUsuarioRegistro {

    void guardarUsuario(Usuario usuario);

    boolean validarUsuarioExistente(String correo);

    Optional<Usuario> buscarUsuarioPorId(Long id);

    Usuario actualizarUsuario(Usuario usuario);

    /**
     * Verifica si el correo ya esta siendo usado por un usuario distinto al id
     * proporcionado. Util al editar para preservar la unicidad sin chocar con
     * el propio registro.
     */
    boolean existeUsuarioConCorreoDistintoA(String correo, Long idExcluir);
}
