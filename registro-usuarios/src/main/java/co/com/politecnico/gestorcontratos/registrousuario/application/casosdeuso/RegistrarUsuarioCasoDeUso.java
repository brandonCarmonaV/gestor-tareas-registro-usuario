package co.com.politecnico.gestorcontratos.registrousuario.application.casosdeuso;

import co.com.politecnico.gestorcontratos.registrousuario.application.dto.ResultadoConsultaUsuarioDTO;
import co.com.politecnico.gestorcontratos.registrousuario.application.dto.SolicitudEdicionUsuarioDTO;
import co.com.politecnico.gestorcontratos.registrousuario.application.dto.SolicitudRegistroUsuarioDTO;

public interface RegistrarUsuarioCasoDeUso {

    void registrarUsuario(SolicitudRegistroUsuarioDTO registroUsuarioDto);

    ResultadoConsultaUsuarioDTO editarUsuario(Long id, SolicitudEdicionUsuarioDTO edicion);
}
