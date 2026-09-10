package co.com.politecnico.gestorcontratos.registrousuario.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import co.com.politecnico.gestorcontratos.registrousuario.application.casosdeuso.RegistrarUsuarioCasoDeUso;
import co.com.politecnico.gestorcontratos.registrousuario.application.casosdeuso.impl.RegistrarUsuarioCasoDeUsoImpl;
import co.com.politecnico.gestorcontratos.registrousuario.application.puertosalida.puertosDeSalidaUsuarioRegistro;

@Configuration
public class CasoUsoConfig {

	@Bean
    public RegistrarUsuarioCasoDeUso registrarUsuarioCasoDeUso(puertosDeSalidaUsuarioRegistro repositorioUsuario) {
        return new RegistrarUsuarioCasoDeUsoImpl(repositorioUsuario); 
    }
	
}
