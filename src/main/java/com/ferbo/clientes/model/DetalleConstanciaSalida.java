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
public class DetalleConstanciaSalida {
    
    private int id = -1;
    private int constancia_cve = -1;
    private int partida_cve = -1;
    private int camara_cve = -1;
    private int cantidad = -1;
    private float peso = -1;
    private String unidad = null;
    private String producto = null;
    private String folio_entrada = null;
    private String camara_cadena = null;
    private int det_part_cve = -1;
    private String temperatura = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConstancia_cve() {
        return constancia_cve;
    }

    public void setConstancia_cve(int constancia_cve) {
        this.constancia_cve = constancia_cve;
    }

    public int getPartida_cve() {
        return partida_cve;
    }

    public void setPartida_cve(int partida_cve) {
        this.partida_cve = partida_cve;
    }

    public int getCamara_cve() {
        return camara_cve;
    }

    public void setCamara_cve(int camara_cve) {
        this.camara_cve = camara_cve;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getFolio_entrada() {
        return folio_entrada;
    }

    public void setFolio_entrada(String folio_entrada) {
        this.folio_entrada = folio_entrada;
    }

    public String getCamara_cadena() {
        return camara_cadena;
    }

    public void setCamara_cadena(String camara_cadena) {
        this.camara_cadena = camara_cadena;
    }

    public int getDet_part_cve() {
        return det_part_cve;
    }

    public void setDet_part_cve(int det_part_cve) {
        this.det_part_cve = det_part_cve;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    @Override
    public String toString() {
        return "Detalle_Constancia_Salida{" + "id=" + id + ", constancia_cve=" + 
                constancia_cve + ", partida_cve=" + partida_cve + 
                ", camara_cve=" + camara_cve + ", cantidad=" + cantidad + 
                ", peso=" + peso + ", unidad=" + unidad + ", producto=" + 
                producto + ", folio_entrada=" + folio_entrada + 
                ", camara_cadena=" + camara_cadena + ", det_part_cve=" + 
                det_part_cve + ", temperatura=" + temperatura + '}';
    }
    
    
}
