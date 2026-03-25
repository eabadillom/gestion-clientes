package com.ferbo.clientes.model;

import java.util.Objects;

/**
 *
 * @author alberto
 */
public class ServicioSalida 
{
    private Integer idSrvSalida;
    private Integer idSalida;
    private Integer idServicio;
    private Integer idUnidadDeManejo;
    private Integer cantidad;

    public ServicioSalida() {
    }

    public Integer getIdSrvSalida() {
        return idSrvSalida;
    }

    public void setIdSrvSalida(Integer idSrvSalida) {
        this.idSrvSalida = idSrvSalida;
    }

    public Integer getIdSalida() {
        return idSalida;
    }

    public void setIdSalida(Integer idSalida) {
        this.idSalida = idSalida;
    }

    public Integer getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Integer idServicio) {
        this.idServicio = idServicio;
    }

    public Integer getIdUnidadDeManejo() {
        return idUnidadDeManejo;
    }

    public void setIdUnidadDeManejo(Integer idUnidadDeManejo) {
        this.idUnidadDeManejo = idUnidadDeManejo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public int hashCode() {
        if(this.idSrvSalida == null)
            return System.identityHashCode(this);
        return Objects.hash(this.idSrvSalida);
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
        final ServicioSalida other = (ServicioSalida) obj;
        if(this.idSrvSalida == null || other.idSrvSalida == null)
            return Objects.equals(System.identityHashCode(this), System.identityHashCode(other));
        
        return Objects.equals(this.idSrvSalida, other.idSrvSalida);
    }

    @Override
    public String toString() {
        return "ServicioSalida[" + "idSalida=" + idSalida + ", idServicio=" + idServicio 
            + ", idUnidadDeManejo=" + idUnidadDeManejo + ", cantidad=" + cantidad + ']';
    }
    
}
