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
public class PreSalida {
    
    private int id = -1;
    private String cd_folio_salida = null;
    private String st_estado = null;
    private Date fh_salida;
    private Date tm_salida;
    private String nb_placa_tte = null;
    private String nb_operador_tte = null;
    private int partida_cve = -1;
    private int folio = -1;
    private int nu_cantidad = -1;
    private Integer idContacto = -1;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCd_folio_salida() {
		return cd_folio_salida;
	}
	public void setCd_folio_salida(String cd_folio_salida) {
		this.cd_folio_salida = cd_folio_salida;
	}
	public String getSt_estado() {
		return st_estado;
	}
	public void setSt_estado(String st_estado) {
		this.st_estado = st_estado;
	}

	public Date getFh_salida() {
		return fh_salida;
	}
	public void setFh_salida(Date date) {
		this.fh_salida = date;
	}
	public Date getTm_salida() {
		return tm_salida;
	}
	public void setTm_salida(Date tm_salida) {
		this.tm_salida = tm_salida;
	}
	public String getNb_placa_tte() {
		return nb_placa_tte;
	}
	public void setNb_placa_tte(String nb_placa_tte) {
		this.nb_placa_tte = nb_placa_tte;
	}
	public String getNb_operador_tte() {
		return nb_operador_tte;
	}
	public void setNb_operador_tte(String nb_operador_tte) {
		this.nb_operador_tte = nb_operador_tte;
	}
	public int getPartida_cve() {
		return partida_cve;
	}
	public void setPartida_cve(int partida_cve) {
		this.partida_cve = partida_cve;
	}
	public int getFolio() {
		return folio;
	}
	public void setFolio(int folio) {
		this.folio = folio;
	}
	public int getNu_cantidad() {
		return nu_cantidad;
	}
	public void setNu_cantidad(int nu_cantidad) {
		this.nu_cantidad = nu_cantidad;
	}
	public Integer getIdContacto() {
		return idContacto;
	}
	public void setIdContacto(Integer idContacto) {
		this.idContacto = idContacto;
	}    
	@Override
	public String toString() {
		return "PreSalida [id=" + id + ", cd_folio_salida=" + cd_folio_salida + ", st_estado=" + st_estado
				+ ", fh_estado=" + fh_salida + ", tm_salida=" + tm_salida 
				+ ", nb_placa_tte="
				+ nb_placa_tte + ", nb_operador_tte=" + nb_operador_tte + ", partida_cve=" + partida_cve + ", folio="
				+ folio + ", nu_cantidad=" + nu_cantidad + "]";
	}
}
