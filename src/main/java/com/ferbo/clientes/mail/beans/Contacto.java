package com.ferbo.clientes.mail.beans;

public class Contacto {
	private Integer idContacto = null;
	private String nombre = null;
	private String apellido1 = null;
	private String apellido2 = null;
	
	private MedioContacto[] mediosContacto = null;
	
	public Integer getIdContacto() {
		return idContacto;
	}
	public void setIdContacto(Integer idContacto) {
		this.idContacto = idContacto;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido1() {
		return apellido1;
	}
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	public String getApellido2() {
		return apellido2;
	}
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	public MedioContacto[] getMediosContacto() {
		return mediosContacto;
	}
	public void setMediosContacto(MedioContacto[] mediosContacto) {
		this.mediosContacto = mediosContacto;
	}
	@Override
	public String toString() {
		return "Contacto [idContacto=" + idContacto + ", nombre=" + nombre + ", apellido1=" + apellido1 + ", apellido2="
				+ apellido2 + "]";
	}
	
}
