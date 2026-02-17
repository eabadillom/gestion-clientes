package com.ferbo.clientes.mail.beans;

public class Mail {
	
	private Integer idMail = null;
	private String mail = null;
	private boolean principal = false;
	private Integer tipoMail = null;
	
	public Integer getIdMail() {
		return idMail;
	}
	public void setIdMail(Integer idMail) {
		this.idMail = idMail;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public boolean isPrincipal() {
		return principal;
	}
	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}
	public Integer getTipoMail() {
		return tipoMail;
	}
	public void setTipoMail(Integer tipoMail) {
		this.tipoMail = tipoMail;
	}
}
