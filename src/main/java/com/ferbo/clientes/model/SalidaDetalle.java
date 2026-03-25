package com.ferbo.clientes.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author alberto
 */
public class SalidaDetalle 
{
    private Integer idSalidaDetalle;
    private Integer idSalida;
    private Integer idPartida;
    private Integer cantidad;
    private BigDecimal pesoAprox;

    public SalidaDetalle() {
    }

    public Integer getIdSalidaDetalle() {
        return idSalidaDetalle;
    }

    public void setIdSalidaDetalle(Integer idSalidaDetalle) {
        this.idSalidaDetalle = idSalidaDetalle;
    }

    public Integer getIdSalida() {
        return idSalida;
    }

    public void setIdSalida(Integer idSalida) {
        this.idSalida = idSalida;
    }

    public Integer getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(Integer idPartida) {
        this.idPartida = idPartida;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPesoAprox() {
        return pesoAprox;
    }

    public void setPesoAprox(BigDecimal pesoAprox) {
        this.pesoAprox = pesoAprox;
    }

    @Override
    public int hashCode() {
        if(this.idSalidaDetalle == null)
            return System.identityHashCode(this);
        return Objects.hash(this.idSalidaDetalle);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SalidaDetalle other = (SalidaDetalle) obj;
        if(this.idSalidaDetalle == null || other.idSalidaDetalle == null)
            return Objects.equals(System.identityHashCode(this), System.identityHashCode(other));
        
        return Objects.equals(this.idSalidaDetalle, other.idSalidaDetalle);
    }

    @Override
    public String toString() {
        return "SalidaDetalle[" + "idSalida=" + idSalida + ", idPartida=" + idPartida 
                + ", cantidad=" + cantidad + ", pesoAprox=" + pesoAprox + ']';
    }
    
}
