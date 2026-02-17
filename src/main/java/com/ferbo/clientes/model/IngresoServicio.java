package com.ferbo.clientes.model;

import java.math.BigDecimal;

public class IngresoServicio {
	
	private Integer idIngresoServicio;
	private Integer idServicio;
	private BigDecimal cantidad;
	private Integer idUnidad;
	private Integer idIngreso;
	
	//DEBE TENER UN SERVICIO Y UNA UNIDAD DE MANEJO PARA MOSTRAR EN TABLA ????	
	private Servicio servicio;
	private UnidadDeManejo unidadDeManejo;
	
	public Integer getIdIngresoServicio() {
		return idIngresoServicio;
	}
	
	public void setIdIngresoServicio(Integer idIngresoServicio) {
		this.idIngresoServicio = idIngresoServicio;
	}
	
	public Integer getIdServicio() {
		return idServicio;
	}
	
	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}
	
	public BigDecimal getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}
	
	public Integer getIdUnidad() {
		return idUnidad;
	}
	
	public void setIdUnidad(Integer idUnidad) {
		this.idUnidad = idUnidad;
	}
	
	public Integer getIdIngreso() {
		return idIngreso;
	}
	
	public void setIdIngreso(Integer idIngreso) {
		this.idIngreso = idIngreso;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	public UnidadDeManejo getUnidadDeManejo() {
		return unidadDeManejo;
	}

	public void setUnidadDeManejo(UnidadDeManejo unidadDeManejo) {
		this.unidadDeManejo = unidadDeManejo;
	}

	
	
	

}
