package com.ferbo.clientes.mail.beans;

public class Producto {

	public Producto() {
		id = -1;
		descripcion = "";
		numeroProducto = "";
		categoria = -1;
	}

	public void setId(int i) {
		id = i;
	}

	public void setDescripcion(String s) {
		descripcion = s;
	}

	public void setNumeroProducto(String s) {
		numeroProducto = s;
	}

	public void setCategoria(int i) {
		categoria = i;
	}

	public int getId() {
		return id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getNumeroProducto() {
		return numeroProducto;
	}

	public int getCategoria() {
		return categoria;
	}

	private int id;
	private String descripcion;
	private String numeroProducto;
	private int categoria;
}
