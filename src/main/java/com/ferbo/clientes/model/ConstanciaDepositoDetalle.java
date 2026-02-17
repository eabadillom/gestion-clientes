/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ferbo.clientes.model;

/**
 *
 * @author Ernesto Ramirez eralrago@gmail.com
 */
public class ConstanciaDepositoDetalle {
    
    private int idConstanciaDepositoDetalle = -1;
    private int servicio = -1;
    private int folio = -1;
    private int servicioCantidad = -1;

    public int getIdConstanciaDepositoDetalle() {
        return idConstanciaDepositoDetalle;
    }

    public void setIdConstanciaDepositoDetalle(int idConstanciaDepositoDetalle) {
        this.idConstanciaDepositoDetalle = idConstanciaDepositoDetalle;
    }

    public int getServicio() {
        return servicio;
    }

    public void setServicio(int servicio) {
        this.servicio = servicio;
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public int getServicioCantidad() {
        return servicioCantidad;
    }

    public void setServicioCantidad(int servicioCantidad) {
        this.servicioCantidad = servicioCantidad;
    }

    @Override
    public String toString() {
        return "ConstanciaDepositoDetalle [idConstanciaDepositoDetalle=" + 
                idConstanciaDepositoDetalle + ", servicio=" + servicio + ", " + 
                "folio=" + folio + ", servicioCantidad=" + servicioCantidad + 
                ']';
    }
    
    
}
