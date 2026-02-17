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
public class Producto {
    
    private int id = -1;
    private String producto_ds = null;
    private String numero_prod = null;
    private int categoria = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProducto_ds() {
        return producto_ds;
    }

    public void setProducto_ds(String producto_ds) {
        this.producto_ds = producto_ds;
    }

    public String getNumero_prod() {
        return numero_prod;
    }

    public void setNumero_prod(String numero_prod) {
        this.numero_prod = numero_prod;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Producto{" + "id=" + id + ", producto_ds=" + producto_ds + 
                ", numero_prod=" + numero_prod + ", categoria=" + categoria + '}';
    }
    
    
    
}
