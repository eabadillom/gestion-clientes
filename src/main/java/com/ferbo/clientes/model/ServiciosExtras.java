package com.ferbo.clientes.model;

public class ServiciosExtras {
	
	private String folioSalida;
	private int idServicioExtra;
	private String servicioExtra;
	private int cantidad;
	private String observacion;
	private boolean habilitado;
	private Integer idUnidadManejo;
	private String unidadManejo;
	private String tipoCobro;
	
	public int getIdServicioExtra() {
		return idServicioExtra;
	}
	
	public void setIdServicioExtra(int idServicioExtra) {
		this.idServicioExtra = idServicioExtra;
	}
	
	public String getServicioExtra() {
		return servicioExtra;
	}
	
	public void setServicioExtra(String servicioExtra) {
		this.servicioExtra = servicioExtra;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	public String getObservacion() {
		return observacion;
	}
	
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	public boolean isHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}

	public String getUnidadManejo() {
		return unidadManejo;
	}

	public void setUnidadManejo(String unidadManejo) {
		this.unidadManejo = unidadManejo;
	}

	public String getTipoCobro() {
		return tipoCobro;
	}

	public void setTipoCobro(String tipoCobro) {
		this.tipoCobro = tipoCobro;
	}

	public Integer getIdUnidadManejo() {
		return idUnidadManejo;
	}

	public void setIdUnidadManejo(Integer idUnidadManejo) {
		this.idUnidadManejo = idUnidadManejo;
	}

	public String getFolioSalida() {
		return folioSalida;
	}

	public void setFolioSalida(String folioSalida) {
		this.folioSalida = folioSalida;
	}

	@Override
	public String toString() {
		return "{\"folioSalida\":\"" + folioSalida + "\", \"idServicioExtra\":\"" + idServicioExtra
				+ "\", \"servicioExtra\":\"" + servicioExtra + "\", \"cantidad\":\"" + cantidad
				+ "\", \"observacion\":\"" + observacion + "\", \"habilitado\":\"" + habilitado
				+ "\", \"idUnidadManejo\":\"" + idUnidadManejo + "\", \"unidadManejo\":\"" + unidadManejo
				+ "\", \"tipoCobro\":\"" + tipoCobro + "\"}";
	}
	
	
	
}
