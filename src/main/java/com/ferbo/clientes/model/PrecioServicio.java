package com.ferbo.clientes.model;

import java.math.BigDecimal;

public class PrecioServicio {
	
	private Integer id = -1;
	private Integer idCliente = null;
	private Integer idServicio = null;
	private Integer idUnidad = null; 
	private BigDecimal precio = null;
	private Integer avisoCve = null;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getIdCliente() {
		return idCliente;
	}
	
	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}
	
	public Integer getIdServicio() {
		return idServicio;
	}
	
	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}
	
	public Integer getIdUnidad() {
		return idUnidad;
	}
	
	public void setIdUnidad(Integer idUnidad) {
		this.idUnidad = idUnidad;
	}
	
	public BigDecimal getPrecio() {
		return precio;
	}
	
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
	
	public Integer getAvisoCve() {
		return avisoCve;
	}
	
	public void setAvisoCve(Integer avisoCve) {
		this.avisoCve = avisoCve;
	}
	
	

}
