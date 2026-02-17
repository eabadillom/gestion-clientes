package com.ferbo.clientes.model;

public class PreSalidaObservaciones {
	
	private String folioSalida = null;
	private String observaciones = null;
	
	public PreSalidaObservaciones() {
		super();
	}
	
	public PreSalidaObservaciones(String folioSalida, String observaciones) {
		super();
		this.folioSalida = folioSalida;
		this.observaciones = observaciones;
	}
	public String getFolioSalida() {
		return folioSalida;
	}
	public void setFolioSalida(String folioSalida) {
		this.folioSalida = folioSalida;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	@Override
	public String toString() {
		return "PreSalidaObservaciones [folioSalida=" + folioSalida + ", observaciones=" + observaciones + "]";
	}
}
