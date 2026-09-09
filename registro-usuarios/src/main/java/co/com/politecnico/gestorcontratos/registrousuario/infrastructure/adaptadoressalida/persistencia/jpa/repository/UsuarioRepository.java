package co.com.politecnico.gestorcontratos.registrousuario.infrastructure.adaptadoressalida.persistencia.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.politecnico.gestorcontratos.registrousuario.application.puertosalida.puertosDeSalidaUsuarioRegistro;
import co.com.politecnico.gestorcontratos.registrousuario.domain.modelo.Usuario;
import co.com.politecnico.gestorcontratos.registrousuario.infrastructure.adaptadoressalida.persistencia.jpa.entity.UsuarioEntity;
import co.com.politecnico.gestorcontratos.registrousuario.infrastructure.mapeadores.MapeadorSolicitudUsuario;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long>, puertosDeSalidaUsuarioRegistro {

	boolean existsByCorreoElectronico(String correo);


	boolean existsByCorreoElectronicoAndIdNot(String correo, Long id);

	@Override
	default void guardarUsuario(Usuario usuario) {
		save(MapeadorSolicitudUsuario.mapperUsuarioEntity(usuario));
	}

	@Override
	default boolean validarUsuarioExistente(String correo) {
		return existsByCorreoElectronico(correo);
	}

	@Override
	default Optional<Usuario> buscarUsuarioPorId(Long id) {
		return findById(id).map(MapeadorSolicitudUsuario::aDominio);
	}

	@Override
	default Usuario actualizarUsuario(Usuario usuario) {
		UsuarioEntity persistido = save(MapeadorSolicitudUsuario.mapperUsuarioEntity(usuario));
		return MapeadorSolicitudUsuario.aDominio(persistido);
	}

	@Override
	default boolean existeUsuarioConCorreoDistintoA(String correo, Long idExcluir) {
		return existsByCorreoElectronicoAndIdNot(correo, idExcluir);
	}
}
