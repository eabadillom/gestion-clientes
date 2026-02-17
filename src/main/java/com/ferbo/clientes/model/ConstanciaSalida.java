/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ferbo.clientes.model;

import java.util.Date;

/**
 *
 * @author Ernesto Ramirez eralrago@gmail.com
 */
public class ConstanciaSalida {
    private int id = -1;
    private Date fecha = null;
    private String numero = null;
    private int cliente_cve = -1;
    private String nombre_cve = null;
    private int status = -1;
    private String observaciones = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getCliente_cve() {
        return cliente_cve;
    }

    public void setCliente_cve(int cliente_cve) {
        this.cliente_cve = cliente_cve;
    }

    public String getNombre_cve() {
        return nombre_cve;
    }

    public void setNombre_cve(String nombre_cve) {
        this.nombre_cve = nombre_cve;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "ConstanciaSalida[" + "id=" + id + ", fecha=" + fecha + 
                ", numero=" + numero + ", cliente_cve=" + cliente_cve + 
                ", nombre_cve=" + nombre_cve + ", status=" + status + 
                ", observaciones=" + observaciones + ']';
    }
    
    
}
