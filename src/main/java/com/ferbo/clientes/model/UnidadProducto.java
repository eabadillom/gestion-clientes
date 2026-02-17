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
public class UnidadProducto {
    
    private int id = -1;
    private int producto = -1;
    private int unidad_manejo = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProducto() {
        return producto;
    }

    public void setProducto(int producto) {
        this.producto = producto;
    }

    public int getUnidad_manejo() {
        return unidad_manejo;
    }

    public void setUnidad_manejo(int unidad_manejo) {
        this.unidad_manejo = unidad_manejo;
    }

    @Override
    public String toString() {
        return "UnidadProducto{" + "id=" + id + ", producto=" + producto + 
                ", unidad_manejo=" + unidad_manejo + '}';
    }

    
}
