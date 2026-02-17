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
public class Partida {
    
    private int id = -1;
    private int camara_cve = -1;
    private int folio = -1;
    private float peso_total = -1;
    private int cantidad_total = -1;
    private int unidad_producto_cve = -1;
    private int cantidad_cobro = -1;
    private int unidad_cobro = -1;
    private int partida_seq = -1;
    private int valorMercancia = -1;
    private int rendimiento = -1;
    private float no_tarimas = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCamara_cve() {
        return camara_cve;
    }

    public void setCamara_cve(int camara_cve) {
        this.camara_cve = camara_cve;
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public float getPeso_total() {
        return peso_total;
    }

    public void setPeso_total(float peso_total) {
        this.peso_total = peso_total;
    }

    public int getCantidad_total() {
        return cantidad_total;
    }

    public void setCantidad_total(int cantidad_total) {
        this.cantidad_total = cantidad_total;
    }

    public int getUnidad_producto_cve() {
        return unidad_producto_cve;
    }

    public void setUnidad_producto_cve(int unidad_producto_cve) {
        this.unidad_producto_cve = unidad_producto_cve;
    }

    public int getCantidad_cobro() {
        return cantidad_cobro;
    }

    public void setCantidad_cobro(int cantidad_cobro) {
        this.cantidad_cobro = cantidad_cobro;
    }

    public int getUnidad_cobro() {
        return unidad_cobro;
    }

    public void setUnidad_cobro(int unidad_cobro) {
        this.unidad_cobro = unidad_cobro;
    }

    public int getPartida_seq() {
        return partida_seq;
    }

    public void setPartida_seq(int partida_seq) {
        this.partida_seq = partida_seq;
    }

    public int getValorMercancia() {
        return valorMercancia;
    }

    public void setValorMercancia(int valorMercancia) {
        this.valorMercancia = valorMercancia;
    }

    public int getRendimiento() {
        return rendimiento;
    }

    public void setRendimiento(int rendimiento) {
        this.rendimiento = rendimiento;
    }

    public float getNo_tarimas() {
        return no_tarimas;
    }

    public void setNo_tarimas(float no_tarimas) {
        this.no_tarimas = no_tarimas;
    }

    @Override
    public String toString() {
        return "Partida{" + "id=" + id + ", camara_cve=" + camara_cve + 
                ", folio=" + folio + ", peso_total=" + peso_total + 
                ", cantidad_total=" + cantidad_total + 
                ", unidad_producto_cve=" + unidad_producto_cve + 
                ", cantidad_cobro=" + cantidad_cobro + ", unidad_cobro=" + 
                unidad_cobro + ", partida_seq=" + partida_seq + 
                ", valorMercancia=" + valorMercancia + ", rendimiento=" + 
                rendimiento + ", no_tarimas=" + no_tarimas + '}';
    }
    
    
}
