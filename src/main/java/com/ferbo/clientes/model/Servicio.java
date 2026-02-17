package com.ferbo.clientes.model;

import com.ferbo.clientes.mail.beans.UnidadDeManejo;

public class Servicio {
	
	private Integer servicioCve;
	private String servicioDs;
	private Integer cobro;
	private String servicioCod;
	private String cdUnidad;
	private String servicioNom;
	private String uuid;
	private Boolean st_Default;
	
	//Agregar unidad de manejo en servicio para mostrar??
	private UnidadDeManejo unidadDeManejo;
	
	public Integer getServicioCve() {
		return servicioCve;
	}
	
	public void setServicioCve(Integer servicioCve) {
		this.servicioCve = servicioCve;
	}
	
	public String getServicioDs() {
		return servicioDs;
	}
	
	public void setServicioDs(String servicioDs) {
		this.servicioDs = servicioDs;
	}
	
	public Integer getCobro() {
		return cobro;
	}
	
	public void setCobro(Integer cobro) {
		this.cobro = cobro;
	}
	
	public String getServicioCod() {
		return servicioCod;
	}
	
	public void setServicioCod(String servicioCod) {
		this.servicioCod = servicioCod;
	}
	
	public String getCdUnidad() {
		return cdUnidad;
	}
	
	public void setCdUnidad(String cdUnidad) {
		this.cdUnidad = cdUnidad;
	}
	
	public String getServicioNom() {
		return servicioNom;
	}
	
	public void setServicioNom(String servicioNom) {
		this.servicioNom = servicioNom;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public Boolean getSt_Default() {
		return st_Default;
	}
	
	public void setSt_Default(Boolean st_Default) {
		this.st_Default = st_Default;
	}

	public UnidadDeManejo getUnidadDeManejo() {
		return unidadDeManejo;
	}

	public void setUnidadDeManejo(UnidadDeManejo unidadDeManejo) {
		this.unidadDeManejo = unidadDeManejo;
	}

	
	
	
}
