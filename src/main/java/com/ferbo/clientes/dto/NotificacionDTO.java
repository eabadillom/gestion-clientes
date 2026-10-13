package com.ferbo.clientes.dto;

import java.time.LocalDateTime;

import com.ferbo.clientes.util.SistemaOrigen;
import com.ferbo.clientes.util.StatusNotificacion;
import com.ferbo.clientes.util.TipoNotificacion;

public class NotificacionDTO {
    
    private SistemaOrigen sistemaOrigen;
    private TipoNotificacion tipo;
    private LocalDateTime fecha;
    private StatusNotificacion status;
    
    public SistemaOrigen getSistemaOrigen() {
        return sistemaOrigen;
    }

    public void setSistemaOrigen(SistemaOrigen sistemaOrigen) {
        this.sistemaOrigen = sistemaOrigen;
    }

    public TipoNotificacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacion tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public StatusNotificacion getStatus() {
        return status;
    }

    public void setStatus(StatusNotificacion status) {
        this.status = status;
    }
}
