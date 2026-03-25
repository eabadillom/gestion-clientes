package com.ferbo.clientes.model;

import java.util.Objects;

/**
 *
 * @author alberto
 */
public class StatusSalida 
{
    private Integer idEstatus;
    private String descripcion;
    private String clave;

    public StatusSalida() {
    }

    public Integer getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(Integer idEstatus) {
        this.idEstatus = idEstatus;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Override
    public int hashCode() {
        if(this.idEstatus == null)
            return System.identityHashCode(this);
        return Objects.hash(this.idEstatus);
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
        final StatusSalida other = (StatusSalida) obj;
        if(this.idEstatus == null || other.idEstatus == null)
            return Objects.equals(System.identityHashCode(this), System.identityHashCode(other));
        
        return Objects.equals(this.idEstatus, other.idEstatus);
    }

    @Override
    public String toString() {
        return "StatusSalida[" + "idEstatus: " + idEstatus + ", descripcion: " + descripcion + ", clave: " + clave + ']';
    }
    
}
