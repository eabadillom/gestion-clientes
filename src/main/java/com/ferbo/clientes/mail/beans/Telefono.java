package com.ferbo.clientes.mail.beans;

public class Telefono {
	
	private Integer idTelefono = null;
	private String telefono = null;
	boolean principal = false;
	private Integer tipoTelefono = null;
	
	public Integer getIdTelefono() {
		return idTelefono;
	}
	public void setIdTelefono(Integer idTelefono) {
		this.idTelefono = idTelefono;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public boolean isPrincipal() {
		return principal;
	}
	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}
	public Integer getTipoTelefono() {
		return tipoTelefono;
	}
	public void setTipoTelefono(Integer tipoTelefono) {
		this.tipoTelefono = tipoTelefono;
	}
}
