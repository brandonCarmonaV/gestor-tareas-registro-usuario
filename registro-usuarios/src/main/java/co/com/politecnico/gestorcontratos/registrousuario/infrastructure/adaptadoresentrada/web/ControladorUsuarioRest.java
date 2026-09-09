package co.com.politecnico.gestorcontratos.registrousuario.infrastructure.adaptadoresentrada.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.politecnico.gestorcontratos.registrousuario.application.casosdeuso.RegistrarUsuarioCasoDeUso;
import co.com.politecnico.gestorcontratos.registrousuario.application.dto.ResultadoConsultaUsuarioDTO;
import co.com.politecnico.gestorcontratos.registrousuario.application.dto.SolicitudEdicionUsuarioDTO;
import co.com.politecnico.gestorcontratos.registrousuario.application.dto.SolicitudRegistroUsuarioDTO;
import co.com.politecnico.gestorcontratos.registrousuario.application.puertosalida.puertosDeSalidaUsuarioRegistro;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/usuarios")
public class ControladorUsuarioRest {

	private final RegistrarUsuarioCasoDeUso casoDeUso;

	public ControladorUsuarioRest(RegistrarUsuarioCasoDeUso casoDeUso, puertosDeSalidaUsuarioRegistro repositorio) {
		this.casoDeUso = casoDeUso;
	}

	@PostMapping
	public ResponseEntity<Void> registrarUsuario(@RequestBody SolicitudRegistroUsuarioDTO solicitud) {
		log.info("REST: solicitud de registro de usuario recibida: {}", solicitud.correoElectronico());
		casoDeUso.registrarUsuario(solicitud);
		return ResponseEntity.created(URI.create("/api/usuarios")).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<ResultadoConsultaUsuarioDTO> editarUsuario(@PathVariable Long id,
			@RequestBody SolicitudEdicionUsuarioDTO edicion) {
		log.info("REST: solicitud de edicion de usuario id={}", id);
		ResultadoConsultaUsuarioDTO respuesta = casoDeUso.editarUsuario(id, edicion);
		return ResponseEntity.ok(respuesta);
	}
}
