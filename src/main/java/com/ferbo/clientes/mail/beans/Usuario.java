package com.ferbo.clientes.mail.beans;

public class Usuario {
	private int id = -1;
	private int perfil = -1;
	private String usuario = "";
	private String apellido1 = "";
	private String apellido2 = "";
	private String password = "";
	private String nombre = "";
	private String descripcion = "";
	private String mail = "";
	private Integer idPlanta = null;

	public int getId() {
		return (this.id);
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPerfil() {
		return (this.perfil);
	}

	public void setPerfil(int perfil) {
		this.perfil = perfil;
	}

	public String getUsuario() {
		return (this.usuario);
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return (this.password);
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNombre() {
		return (this.nombre);
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return (this.descripcion);
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Integer getIdPlanta() {
		return idPlanta;
	}

	public void setIdPlanta(Integer idPlanta) {
		this.idPlanta = idPlanta;
	}
}
