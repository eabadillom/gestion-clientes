package com.ferbo.clientes.model;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author alberto
 */
public class Salida 
{
    private Integer idSalida;
    private String folioSalida;
    private Integer idCliente;
    private Integer idContacto;
    private Integer idStatus;
    private String placasTransporte;
    private String nombreTransportista;
    private String observaciones;
    private Date fechaSalida;
    private Date horaSalida;
    private Date fechaRegistro;
    private Date fechaModificacion;

    public Salida() {
    }

    public Integer getIdSalida() {
        return idSalida;
    }

    public void setIdSalida(Integer idSalida) {
        this.idSalida = idSalida;
    }

    public String getFolioSalida() {
        return folioSalida;
    }

    public void setFolioSalida(String folioSalida) {
        this.folioSalida = folioSalida;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Integer idContacto) {
        this.idContacto = idContacto;
    }

    public Integer getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Integer idStatus) {
        this.idStatus = idStatus;
    }

    public String getPlacasTransporte() {
        return placasTransporte;
    }

    public void setPlacasTransporte(String placasTransporte) {
        this.placasTransporte = placasTransporte;
    }

    public String getNombreTransportista() {
        return nombreTransportista;
    }

    public void setNombreTransportista(String nombreTransportista) {
        this.nombreTransportista = nombreTransportista;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Date getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Date horaSalida) {
        this.horaSalida = horaSalida;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    @Override
    public int hashCode() {
        if(this.idSalida == null)
            return System.identityHashCode(this);
        return Objects.hash(this.idSalida);
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
        final Salida other = (Salida) obj;
        if(this.idSalida == null || other.idSalida == null)
            return Objects.equals(System.identityHashCode(this), System.identityHashCode(other));
        
        return Objects.equals(this.idSalida, other.idSalida);
    }

    @Override
    public String toString() {
        return "Salida[" + "folioSalida=" + folioSalida + ", placasTransporte=" + placasTransporte 
                + ", nombreTransportista=" + nombreTransportista + ", observaciones=" + observaciones + ", fechaSalida=" + fechaSalida 
                + ", horaSalida=" + horaSalida + ", fechaRegistro=" + fechaRegistro + ", fechaModificacion=" + fechaModificacion + ']';
    }
    
}
