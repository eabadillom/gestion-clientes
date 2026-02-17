package com.ferbo.clientes.mail.beans;

public class Correo {
	String mail = null;
	String nombreBuzon = null;
	
	public Correo(String mail, String nombreBuzon) {
		this.mail = mail;
		this.nombreBuzon = nombreBuzon;
	}
	
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getNombreBuzon() {
		return nombreBuzon;
	}
	public void setNombreBuzon(String nombreBuzon) {
		this.nombreBuzon = nombreBuzon;
	}
}
