package com.gestortareas.paneles.domain.port.in;

import com.gestortareas.paneles.domain.model.Panel;

import java.util.List;

/**
 * Use Case para listar paneles de un propietario.
 * 
 * El propietarioId es extraído del token autenticado, no del request.
 */
public interface ListarPanelesUseCase {
    /**
     * Lista todos los paneles de un propietario.
     * 
     * @param propietarioId id del propietario (extraído del token autenticado)
     * @return lista de paneles del propietario (puede estar vacía)
     */
    List<Panel> listarPaneles(String propietarioId);
}
