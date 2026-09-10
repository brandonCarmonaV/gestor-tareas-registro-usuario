package co.com.politecnico.gestorcontratos.registrousuario.application.dto;

/**
 * DTO de entrada para la operacion de editar un usuario.
 * Solo expone los campos editables; la contrasena se gestiona
 * en otro endpoint dedicado.
 */
public record SolicitudEdicionUsuarioDTO(
        String nombreCompleto,
        String correoElectronico
) {}
