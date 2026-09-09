package co.com.politecnico.gestorcontratos.registrousuario.domain.excepcion;

public class ExcepcionGeneralesReglaNegocio extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public ExcepcionGeneralesReglaNegocio(String mensaje) {
        super(mensaje);
    }
}
