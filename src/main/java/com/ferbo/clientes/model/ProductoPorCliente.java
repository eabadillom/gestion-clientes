package com.ferbo.clientes.model;

public class ProductoPorCliente {
	
	private Integer idProductoPorCte = -1; 
	private Integer idCliente = -1;
	private Integer idProducto = -1;
	private Producto producto;
	
	
	public Integer getIdProductoPorCte() {
		return idProductoPorCte;
	}
	
	public void setIdProductoPorCte(Integer idProductoPorCte) {
		this.idProductoPorCte = idProductoPorCte;
	}
	
	public Integer getIdCliente() {
		return idCliente;
	}
	
	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}
	
	public Integer getIdProducto() {
		return idProducto;
	}
	
	public void setIdProducto(Integer idProducto) {
		this.idProducto = idProducto;
	}
	
	public Producto getProducto() {
		return producto;
	}
	
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	
	

}
