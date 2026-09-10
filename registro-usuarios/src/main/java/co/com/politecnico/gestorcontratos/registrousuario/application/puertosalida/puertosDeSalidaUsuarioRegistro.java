package co.com.politecnico.gestorcontratos.registrousuario.application.puertosalida;

import java.util.Optional;

import co.com.politecnico.gestorcontratos.registrousuario.domain.modelo.Usuario;

public interface puertosDeSalidaUsuarioRegistro {

    void guardarUsuario(Usuario usuario);

    boolean validarUsuarioExistente(String correo);
    
	Optional<Usuario> buscarUsuarioPorId(String id);

    Usuario actualizarUsuario(Usuario usuario);

    boolean existeUsuarioConCorreoDistintoA(String correo, String idExcluir);
}
