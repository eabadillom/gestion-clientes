package com.ferbo.clientes.mail.beans;

public class UnidadProducto {
	
	private int            idUnidadProducto = -1;
	private int            idProducto       = -1;
	private int            idUnidadManejo   = -1;
	private Producto       producto         = null;
	private UnidadDeManejo unidadManejo     = null;
	
	public void setIdUnidadProducto(int idUnidad){
		this.idUnidadProducto = idUnidad;
	}
	
	public int getIdUnidadProducto(){
		return this.idUnidadProducto;
	}
	
	public void setIdProducto(int idProducto){
		this.idProducto = idProducto;
	}
	
	public int getIdProducto(){
		return this.idProducto;
	}
	
	public void setIdUnidadManejo(int idUnidadManejo){
		this.idUnidadManejo = idUnidadManejo;
	}
	
	public int getIdUnidadManejo(){
		return this.idUnidadManejo;
	}
	
	public void setProducto(Producto p){
		this.producto = p;
	}
	
	public Producto getProducto(){
		return this.producto;
	}
	
	public void setUnidadDeManejo(UnidadDeManejo um){
		this.unidadManejo = um;
	}
	
	public UnidadDeManejo getUnidadDeManejo(){
		return this.unidadManejo;
	}
}