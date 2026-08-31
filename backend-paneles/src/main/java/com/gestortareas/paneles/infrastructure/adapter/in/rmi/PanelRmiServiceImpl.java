package com.gestortareas.paneles.infrastructure.adapter.in.rmi;

import com.gestortareas.paneles.application.exception.UnauthorizedException;
import com.gestortareas.paneles.application.exception.ValidationException;
import com.gestortareas.paneles.application.service.PanelService;
import com.gestortareas.paneles.domain.model.EstadoPanel;
import com.gestortareas.paneles.domain.model.Panel;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Logger;

/**
 * Adapter RMI que implementa PanelRemoteService.
 * 
 * Responsabilidades:
 * - Exponer los 3 use cases de Panel vía RMI
 * - Delegar toda la lógica a PanelService
 * - Convertir excepciones de aplicación en RemoteException si es necesario
 * 
 * Nota: Los clientes RMI pasan Panel ya construido con propietarioId validado
 * en el lado del cliente (o confiamos en que está bien informado).
 */
public class PanelRmiServiceImpl extends UnicastRemoteObject implements PanelRemoteService {

    private static final Logger logger = Logger.getLogger(PanelRmiServiceImpl.class.getName());
    
    private final PanelService panelService;

    public PanelRmiServiceImpl(PanelService panelService) throws RemoteException {
        super();
        this.panelService = panelService;
    }

    /**
     * Crea un nuevo panel vía RMI.
     * 
     * Flujo:
     * 1. Valida que Panel tenga nombre y propietarioId
     * 2. Delega a panelService.crearPanel() extrayendo los datos del Panel
     * 3. Retorna panel creado
     * 
     * @param panel panel con nombre, color, prioridad, fechas y propietarioId ya informados
     * @return panel creado con id único, estado PENDIENTE, fechaCreacion asignada
     * @throws RemoteException si hay error en la comunicación RMI
     * @throws IllegalArgumentException si panel.nombre vacío o fechas inválidas
     */
    @Override
    public Panel crearPanel(Panel panel) throws RemoteException {
        try {
            if (panel == null) {
                throw new IllegalArgumentException("Panel no puede ser null");
            }
            
            logger.info("RMI: Creando panel con nombre='" + panel.getNombre() + 
                       "' propietarioId='" + panel.getPropietarioId() + "'");
            
            // Delegar a PanelService con los parámetros extraídos del Panel
            Panel panelCreado = panelService.crearPanel(
                panel.getNombre(),
                panel.getColor(),
                panel.getPrioridad(),
                panel.getFechaInicio(),
                panel.getFechaFin(),
                panel.getPropietarioId()
            );
            
            return panelCreado;
            
        } catch (ValidationException | UnauthorizedException ex) {
            logger.severe("Error en RMI crearPanel: " + ex.getMessage());
            throw new RemoteException(ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            logger.warning("Validación fallida en RMI crearPanel: " + ex.getMessage());
            throw new RemoteException(ex.getMessage(), ex);
        }
    }

    /**
     * Lista paneles de un propietario vía RMI.
     * 
     * @param propietarioId id del propietario
     * @return lista de paneles del propietario (puede estar vacía, nunca null)
     * @throws RemoteException si hay error en la comunicación RMI
     * @throws UnauthorizedException si propietarioId no es válido
     */
    @Override
    public List<Panel> listarPaneles(String propietarioId) throws RemoteException {
        try {
            if (propietarioId == null || propietarioId.trim().isEmpty()) {
                throw new IllegalArgumentException("propietarioId no puede ser null o vacío");
            }
            
            logger.info("RMI: Listando paneles del propietario='" + propietarioId + "'");
            
            List<Panel> paneles = panelService.listarPaneles(propietarioId);
            
            logger.info("RMI: Retornando " + paneles.size() + " paneles");
            return paneles;
            
        } catch (UnauthorizedException ex) {
            logger.severe("Autorización fallida en RMI listarPaneles: " + ex.getMessage());
            throw new RemoteException(ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            logger.warning("Validación fallida en RMI listarPaneles: " + ex.getMessage());
            throw new RemoteException(ex.getMessage(), ex);
        }
    }

    /**
     * Actualiza el estado de un panel vía RMI.
     * 
     * IMPORTANTE: Esta implementación NO recibe propietarioId en la firma.
     * En la FASE 7 (cuando AuthServicePort esté implementado), la llamada
     * RMI debe validar el token y pasar el propietarioId validado.
     * 
     * Por ahora, se asume que el cliente remoto ya validó el propietarioId
     * y el panel contiene el propietarioId correcto.
     * 
     * TODO FASE 7: Modificar la firma de PanelRemoteService para incluir token/propietarioId
     * y validar en AuthServicePort.
     * 
     * @param panelId id del panel a actualizar
     * @param nuevoEstado nuevo estado deseado
     * @return panel actualizado
     * @throws RemoteException si hay error en la comunicación RMI
     * @throws IllegalArgumentException si panel no existe
     */
    @Override
    public Panel actualizarEstado(String panelId, EstadoPanel nuevoEstado) throws RemoteException {
        try {
            if (panelId == null || panelId.trim().isEmpty()) {
                throw new IllegalArgumentException("panelId no puede ser null o vacío");
            }
            if (nuevoEstado == null) {
                throw new IllegalArgumentException("nuevoEstado no puede ser null");
            }
            
            logger.info("RMI: Actualizando estado del panel='" + panelId + 
                       "' a nuevoEstado='" + nuevoEstado + "'");
            
            // TODO FASE 7: Extraer propietarioId del token validado
            // Por ahora, levantamos excepción informativa
            throw new UnsupportedOperationException(
                "actualizarEstado vía RMI requiere propietarioId validado. " +
                "Esta funcionalidad se completará en FASE 7 con AuthServicePort."
            );
            
        } catch (IllegalArgumentException | UnsupportedOperationException ex) {
            logger.warning("Error en RMI actualizarEstado: " + ex.getMessage());
            throw new RemoteException(ex.getMessage(), ex);
        }
    }
}
