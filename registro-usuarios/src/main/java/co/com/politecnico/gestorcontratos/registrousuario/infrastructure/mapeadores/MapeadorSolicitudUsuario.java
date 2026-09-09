package co.com.politecnico.gestorcontratos.registrousuario.infrastructure.mapeadores;

import co.com.politecnico.gestorcontratos.registrousuario.domain.modelo.Usuario;
import co.com.politecnico.gestorcontratos.registrousuario.domain.objectvalue.CorreoElectronico;
import co.com.politecnico.gestorcontratos.registrousuario.domain.objectvalue.Password;
import co.com.politecnico.gestorcontratos.registrousuario.infrastructure.adaptadoressalida.persistencia.jpa.entity.UsuarioEntity;

public final class MapeadorSolicitudUsuario {

	private MapeadorSolicitudUsuario() {
	}

	public static UsuarioEntity mapperUsuarioEntity(Usuario usuario) {
		return UsuarioEntity.builder()
				.id(usuario.getId() == null ? null : usuario.getId().toString())
				.nombreCompleto(usuario.getNombreCompleto())
				.correoElectronico(usuario.getCorreo().correoElectronico())
				.password(usuario.getPassword().password())
				.build();
	}

	public static Usuario mapperUsuarioDominio(UsuarioEntity entity) {
		return new Usuario(
				Long.valueOf(entity.getId()),
				entity.getNombreCompleto(),
				new CorreoElectronico(entity.getCorreoElectronico()),
				new Password(entity.getPassword()));
	}
}
