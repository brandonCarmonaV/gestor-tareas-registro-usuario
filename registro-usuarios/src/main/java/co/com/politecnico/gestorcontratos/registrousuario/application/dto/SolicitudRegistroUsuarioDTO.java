package co.com.politecnico.gestorcontratos.registrousuario.application.dto;

public record SolicitudRegistroUsuarioDTO (
	String nombreCompleto,
	String correoElectronico,
	String password
	) {}