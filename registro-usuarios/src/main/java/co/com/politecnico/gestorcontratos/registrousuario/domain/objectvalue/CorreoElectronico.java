package co.com.politecnico.gestorcontratos.registrousuario.domain.objectvalue;

import java.util.regex.Pattern;

import co.com.politecnico.gestorcontratos.registrousuario.domain.excepcion.ExcepcionGeneralesReglaNegocio;

public record CorreoElectronico(String correoElectronico) {

	private static final Pattern PATRON_CORREO = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

	public CorreoElectronico {
		validarCorreoElectronicoValido(correoElectronico);
	}

	private static void validarCorreoElectronicoValido(String correoElectronico) {
        if (correoElectronico == null || correoElectronico.isBlank()) {
            throw new ExcepcionGeneralesReglaNegocio("El correo electronico es obligatorio.");
        }
        
        String correoElectronicoMinuscula = correoElectronico.trim().toLowerCase();
        
        if (!PATRON_CORREO.matcher(correoElectronicoMinuscula).matches()) {
            throw new ExcepcionGeneralesReglaNegocio("El correo electronico no tiene un formato valido.");
        }
    }

}
