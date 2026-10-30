package com.gestortareas.paneles.application.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MiembroRequestDTO {

    @NotBlank(message = "El correo del miembro es obligatorio")
    @Email(message = "El correo del miembro no es válido")
    private String email;

}
