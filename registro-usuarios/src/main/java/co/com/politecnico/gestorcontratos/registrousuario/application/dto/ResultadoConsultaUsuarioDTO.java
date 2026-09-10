package co.com.politecnico.gestorcontratos.registrousuario.application.dto;

/**
 * DTO de salida para operaciones de consulta y edicion de usuarios.
 * Nunca expone el password al exterior del backend.
 */
public record ResultadoConsultaUsuarioDTO(
        Long id,
        String nombreCompleto,
        String correoElectronico
) {}
