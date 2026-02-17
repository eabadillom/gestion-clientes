package com.ferbo.clientes.mail.beans;

public class MedioContacto {
	private Integer idMedio = null;
	private String tipoMedio = null;
	private boolean habilitado = false;
	private Integer idContacto = null;
	private Integer idMail = null;
	private Integer idTelefono = null;
	
	public static final String TP_MEDIO_MAIL = "M";
	public static final String TP_MEDIO_TELEFONO = "T";
	
	public Integer getIdMedio() {
		return idMedio;
	}
	public void setIdMedio(Integer idMedio) {
		this.idMedio = idMedio;
	}
	public String getTipoMedio() {
		return tipoMedio;
	}
	public void setTipoMedio(String tipoMedio) {
		this.tipoMedio = tipoMedio;
	}
	public boolean isHabilitado() {
		return habilitado;
	}
	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}
	public Integer getIdContacto() {
		return idContacto;
	}
	public void setIdContacto(Integer idContacto) {
		this.idContacto = idContacto;
	}
	public Integer getIdMail() {
		return idMail;
	}
	public void setIdMail(Integer idMail) {
		this.idMail = idMail;
	}
	public Integer getIdTelefono() {
		return idTelefono;
	}
	public void setIdTelefono(Integer idTelefono) {
		this.idTelefono = idTelefono;
	}
	@Override
	public String toString() {
		return "{\"idMedio\":\"" + idMedio + "\", \"tipoMedio\":\"" + tipoMedio + "\", \"habilitado\":\"" + habilitado
				+ "\", \"idContacto\":\"" + idContacto + "\", \"idMail\":\"" + idMail + "\", \"idTelefono\":\""
				+ idTelefono + "\"}";
	}
}
