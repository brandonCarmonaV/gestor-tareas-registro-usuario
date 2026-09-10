package co.com.politecnico.gestorcontratos.registrousuario.domain.objectvalue;

import co.com.politecnico.gestorcontratos.registrousuario.domain.excepcion.ExcepcionGeneralesReglaNegocio;

public record Password(String password) {

    public static final int LONGITUD_MINIMA = 8;

    public Password {
       validarPasswordValida(password);
    }
    
    private void validarPasswordValida(String pass) {
        if (pass == null || pass.isBlank()) {
            throw new ExcepcionGeneralesReglaNegocio("La contrasena es obligatoria.");
        }
        if (pass.length() < LONGITUD_MINIMA) {
            throw new ExcepcionGeneralesReglaNegocio(
                    "La contraseña debe tener al menos " + LONGITUD_MINIMA + " caracteres.");
        }
    }

    
}
