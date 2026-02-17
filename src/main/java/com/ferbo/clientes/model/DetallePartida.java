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
public class DetallePartida {
    
    private int id = -1;
    private int partida_cve = -1;
    private int tipo_mov_cve = -1;
    private int edo_inv_cve = -1;
    private int det_anterior = -1;
    private int det_part_anterior = -1;
    private int det_padre = -1;
    private int det_part_padre = -1;
    private int cantidad_u_manejo = -1;
    private int u_medida_cve = -1;
    private int cantidad_u_medida = -1;
    private String dtp_codigo = null;
    private String dtp_lote = null;
    private Date dtp_caducidad = null;
    private String dtp_PO = null;
    private String dtp_MP = null;
    private String dtp_pedimento = null;
    private String dtp_SAT = null;
    private String dtp_tarima = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPartida_cve() {
        return partida_cve;
    }

    public void setPartida_cve(int partida_cve) {
        this.partida_cve = partida_cve;
    }

    public int getTipo_mov_cve() {
        return tipo_mov_cve;
    }

    public void setTipo_mov_cve(int tipo_mov_cve) {
        this.tipo_mov_cve = tipo_mov_cve;
    }

    public int getEdo_inv_cve() {
        return edo_inv_cve;
    }

    public void setEdo_inv_cve(int edo_inv_cve) {
        this.edo_inv_cve = edo_inv_cve;
    }

    public int getDet_anterior() {
        return det_anterior;
    }

    public void setDet_anterior(int det_anterior) {
        this.det_anterior = det_anterior;
    }

    public int getDet_part_anterior() {
        return det_part_anterior;
    }

    public void setDet_part_anterior(int det_part_anterior) {
        this.det_part_anterior = det_part_anterior;
    }

    public int getDet_padre() {
        return det_padre;
    }

    public void setDet_padre(int det_padre) {
        this.det_padre = det_padre;
    }

    public int getDet_part_padre() {
        return det_part_padre;
    }

    public void setDet_part_padre(int det_part_padre) {
        this.det_part_padre = det_part_padre;
    }

    public int getCantidad_u_manejo() {
        return cantidad_u_manejo;
    }

    public void setCantidad_u_manejo(int cantidad_u_manejo) {
        this.cantidad_u_manejo = cantidad_u_manejo;
    }

    public int getU_medida_cve() {
        return u_medida_cve;
    }

    public void setU_medida_cve(int u_medida_cve) {
        this.u_medida_cve = u_medida_cve;
    }

    public int getCantidad_u_medida() {
        return cantidad_u_medida;
    }

    public void setCantidad_u_medida(int cantidad_u_medida) {
        this.cantidad_u_medida = cantidad_u_medida;
    }

    public String getDtp_codigo() {
        return dtp_codigo;
    }

    public void setDtp_codigo(String dtp_codigo) {
        this.dtp_codigo = dtp_codigo;
    }

    public String getDtp_lote() {
        return dtp_lote;
    }

    public void setDtp_lote(String dtp_lote) {
        this.dtp_lote = dtp_lote;
    }

    public Date getDtp_caducidad() {
        return dtp_caducidad;
    }

    public void setDtp_caducidad(Date dtp_caducidad) {
        this.dtp_caducidad = dtp_caducidad;
    }

    public String getDtp_PO() {
        return dtp_PO;
    }

    public void setDtp_PO(String dtp_PO) {
        this.dtp_PO = dtp_PO;
    }

    public String getDtp_MP() {
        return dtp_MP;
    }

    public void setDtp_MP(String dtp_MP) {
        this.dtp_MP = dtp_MP;
    }

    public String getDtp_pedimento() {
        return dtp_pedimento;
    }

    public void setDtp_pedimento(String dtp_pedimento) {
        this.dtp_pedimento = dtp_pedimento;
    }

    public String getDtp_SAT() {
        return dtp_SAT;
    }

    public void setDtp_SAT(String dtp_SAT) {
        this.dtp_SAT = dtp_SAT;
    }

    public String getDtp_tarima() {
        return dtp_tarima;
    }

    public void setDtp_tarima(String dtp_tarima) {
        this.dtp_tarima = dtp_tarima;
    }

    @Override
    public String toString() {
        return "DetallePartida{" + "id=" + id + ", partida_cve=" + partida_cve + 
                ", tipo_mov_cve=" + tipo_mov_cve + ", edo_inv_cve=" + 
                edo_inv_cve + ", det_anterior=" + det_anterior + 
                ", det_part_anterior=" + det_part_anterior + ", det_padre=" + 
                det_padre + ", det_part_padre=" + det_part_padre + 
                ", cantidad_u_manejo=" + cantidad_u_manejo + ", u_medida_cve=" + 
                u_medida_cve + ", cantidad_u_medida=" + cantidad_u_medida + 
                ", dtp_codigo=" + dtp_codigo + ", dtp_lote=" + dtp_lote + 
                ", dtp_caducidad=" + dtp_caducidad + ", dtp_PO=" + dtp_PO + 
                ", dtp_MP=" + dtp_MP + ", dtp_pedimento=" + dtp_pedimento + 
                ", dtp_SAT=" + dtp_SAT + ", dtp_tarima=" + dtp_tarima + '}';
    }

    
}
