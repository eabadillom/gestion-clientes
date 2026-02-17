package com.ferbo.clientes.model;

import java.sql.Timestamp;
import java.util.Date;

public class Ingreso{
	
	private Integer idIngreso = null;
	private String folio = null;
	private Date fechaHora = null;
	private Integer idCliente=-1;
	private String transportista;
	private String placas;
	private String observaciones;
	private Integer idContacto = -1;
	private Integer status = -1;
	
	public Integer getIdIngreso() {
		return idIngreso;
	}
	
	public void setIdIngreso(Integer idIngreso) {
		this.idIngreso = idIngreso;
	}
	
	public String getFolio() {
		return folio;
	}
	
	public void setFolio(String folio) {
		this.folio = folio;
	}
	
	public Date getFechaHora() {
		return fechaHora;
	}
	
	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}
	
	public Integer getIdCliente() {
		return idCliente;
	}
	
	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}
	
	public String getTransportista() {
		return transportista;
	}
	
	public void setTransportista(String transportista) {
		this.transportista = transportista;
	}
	
	public String getPlacas() {
		return placas;
	}
	
	public void setPlacas(String placas) {
		this.placas = placas;
	}
	
	public String getObservaciones() {
		return observaciones;
	}
	
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIdContacto() {
		return idContacto;
	}

	public void setIdContacto(Integer idContacto) {
		this.idContacto = idContacto;
	}
	
	

}
