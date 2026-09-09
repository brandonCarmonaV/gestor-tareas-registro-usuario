package co.com.politecnico.gestorcontratos.registrousuario.domain.modelo;

import co.com.politecnico.gestorcontratos.registrousuario.domain.excepcion.ExcepcionGeneralesReglaNegocio;
import co.com.politecnico.gestorcontratos.registrousuario.domain.objectvalue.CorreoElectronico;
import co.com.politecnico.gestorcontratos.registrousuario.domain.objectvalue.Password;

public final class Usuario {

	private final Long id;
	private final String nombreCompleto;
	private final CorreoElectronico correoElectronico;
	private final Password password;

	public Usuario(Long id, String nombreCompleto, CorreoElectronico correoElectronico, Password pass) {

		validarAtributosObligatorios(nombreCompleto, correoElectronico, pass);

		this.id = id;
		this.nombreCompleto = nombreCompleto;
		this.correoElectronico = correoElectronico;
		this.password = pass;
	}

	public Long getId() {
		return id;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public CorreoElectronico getCorreo() {
		return correoElectronico;
	}

	public Password getPassword() {
		return password;
	}

	public Usuario actualizarUsuario(String nuevoNombreCompleto, CorreoElectronico nuevoCorreo) {
		return new Usuario(this.id, nuevoNombreCompleto, nuevoCorreo, this.password);
	}

	private void validarAtributosObligatorios(String nombreCompleto, CorreoElectronico correoElectronico, Password pass) {

		if (nombreCompleto == null) {
            throw new ExcepcionGeneralesReglaNegocio("El nombre completo es obligatorio.");
        }
        if (correoElectronico == null) {
            throw new ExcepcionGeneralesReglaNegocio("El correo electronico es obligatorio.");
        }
        if (pass == null) {
            throw new ExcepcionGeneralesReglaNegocio("La contrasena es obligatoria.");
        }

	}
}
