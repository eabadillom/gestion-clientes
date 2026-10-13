package com.ferbo.clientes.business;


import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.dto.NotificacionDTO;
import com.ferbo.clientes.util.SistemaOrigen;
import com.ferbo.clientes.util.StatusNotificacion;
import com.ferbo.clientes.util.TipoNotificacion;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Named 
@RequestScoped 
public class NotificacionBL {
    
    private static final Logger log = LogManager.getLogger("NotificacionBL");
    
    private NotificacionDTO getNotificacionBase() {
        NotificacionDTO notificacion = new NotificacionDTO();
        
        notificacion.setSistemaOrigen(SistemaOrigen.GESTION_CLIENTES);
        notificacion.setStatus(StatusNotificacion.ENVIADA);
        LocalDateTime cdmxDateTime = LocalDateTime.now(ZoneId.of("America/Mexico_City"));
        notificacion.setFecha(cdmxDateTime);
        
        return notificacion;
    }
    
    public NotificacionDTO getNuevaOrdenRetiro() {
    
        NotificacionDTO notificacion = getNotificacionBase();
        notificacion.setTipo(TipoNotificacion.NUEVA_ORDEN_RETIRO);
        
        return notificacion;
    }
 
}
