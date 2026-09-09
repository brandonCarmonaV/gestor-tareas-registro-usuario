package co.com.politecnico.gestorcontratos.registrousuario.application.casosdeuso.impl;

import co.com.politecnico.gestorcontratos.registrousuario.application.casosdeuso.RegistrarUsuarioCasoDeUso;
import co.com.politecnico.gestorcontratos.registrousuario.application.dto.ResultadoConsultaUsuarioDTO;
import co.com.politecnico.gestorcontratos.registrousuario.application.dto.SolicitudEdicionUsuarioDTO;
import co.com.politecnico.gestorcontratos.registrousuario.application.dto.SolicitudRegistroUsuarioDTO;
import co.com.politecnico.gestorcontratos.registrousuario.application.puertosalida.puertosDeSalidaUsuarioRegistro;
import co.com.politecnico.gestorcontratos.registrousuario.domain.excepcion.ExcepcionGeneralesReglaNegocio;
import co.com.politecnico.gestorcontratos.registrousuario.domain.modelo.Usuario;
import co.com.politecnico.gestorcontratos.registrousuario.domain.objectvalue.CorreoElectronico;
import co.com.politecnico.gestorcontratos.registrousuario.domain.objectvalue.Password;

public final class RegistrarUsuarioCasoDeUsoImpl implements RegistrarUsuarioCasoDeUso {

    private final puertosDeSalidaUsuarioRegistro repositorioUsuario;

    public RegistrarUsuarioCasoDeUsoImpl(puertosDeSalidaUsuarioRegistro repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public void registrarUsuario(SolicitudRegistroUsuarioDTO solicitud) {

        CorreoElectronico correo = new CorreoElectronico(solicitud.correoElectronico());
        Password password = new Password(solicitud.password());

        if (repositorioUsuario.validarUsuarioExistente(correo.correoElectronico())) {
            throw new ExcepcionGeneralesReglaNegocio(
                    "Ya existe un usuario registrado con el correo " + correo + ".");
        }

        Usuario usuario = new Usuario(null, solicitud.nombreCompleto(), correo, password);
        repositorioUsuario.guardarUsuario(usuario);
    }

    @Override
    public ResultadoConsultaUsuarioDTO editarUsuario(Long id, SolicitudEdicionUsuarioDTO edicion) {

        Usuario usuarioActual = repositorioUsuario.buscarUsuarioPorId(id)
                .orElseThrow(() -> new ExcepcionGeneralesReglaNegocio(
                        "No existe un usuario registrado con el id " + id + "."));

        CorreoElectronico nuevoCorreo = new CorreoElectronico(edicion.correoElectronico());

        if (!nuevoCorreo.correoElectronico().equalsIgnoreCase(usuarioActual.getCorreo().correoElectronico())
                && repositorioUsuario.existeUsuarioConCorreoDistintoA(nuevoCorreo.correoElectronico(), id)) {
            throw new ExcepcionGeneralesReglaNegocio(
                    "El correo " + nuevoCorreo + " ya esta registrado.");
        }

        Usuario usuarioEditado = usuarioActual.actualizarUsuario(edicion.nombreCompleto(), nuevoCorreo);
        Usuario usuarioPersistido = repositorioUsuario.actualizarUsuario(usuarioEditado);

        return new ResultadoConsultaUsuarioDTO(
                usuarioPersistido.getId(),
                usuarioPersistido.getNombreCompleto(),
                usuarioPersistido.getCorreo().correoElectronico());
    }
}
