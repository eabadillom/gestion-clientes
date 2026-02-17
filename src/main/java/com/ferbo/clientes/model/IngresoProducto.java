package com.ferbo.clientes.model;

import java.math.BigDecimal;
import java.util.Date;




public class IngresoProducto implements Cloneable{

	private Integer idIngresoProducto;
	private Integer cantidad;
	private Integer idUnidadMedida;
	private BigDecimal peso;
	private Integer idPlanta;
	private BigDecimal noTarimas;
	private String lote;
	private String pedimento;
	private String contenedor;
	private Date fechaCaducidad;
	private String otro;
	private Integer idIngreso;
	private Integer idProducto;
	private Producto producto;
	private UnidadDeManejo unidadDeManejo;
	
	public IngresoProducto clone() throws CloneNotSupportedException{  
    	return (IngresoProducto) super.clone();  
	} 
	
	public Integer getIdIngresoProducto() {
		return idIngresoProducto;
	}
	
	public void setIdIngresoProducto(Integer idIngresoProducto) {
		this.idIngresoProducto = idIngresoProducto;
	}
	
	public Integer getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	public Integer getIdUnidadMedida() {
		return idUnidadMedida;
	}
	
	public void setIdUnidadMedida(Integer idUnidadMedida) {
		this.idUnidadMedida = idUnidadMedida;
	}
	
	public BigDecimal getPeso() {
		return peso;
	}
	
	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}
	
	public Integer getIdPlanta() {
		return idPlanta;
	}
	
	public void setIdPlanta(Integer idPlanta) {
		this.idPlanta = idPlanta;
	}
	
	public BigDecimal getNoTarimas() {
		return noTarimas;
	}
	
	public void setNoTarimas(BigDecimal noTarimas) {
		this.noTarimas = noTarimas;
	}
	
	public String getLote() {
		return lote;
	}
	
	public void setLote(String lote) {
		this.lote = lote;
	}
	
	public String getPedimento() {
		return pedimento;
	}
	
	public void setPedimento(String pedimento) {
		this.pedimento = pedimento;
	}
	
	public String getContenedor() {
		return contenedor;
	}
	
	public void setContenedor(String contenedor) {
		this.contenedor = contenedor;
	}
	
	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}
	
	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}
	
	public String getOtro() {
		return otro;
	}
	
	public void setOtro(String otro) {
		this.otro = otro;
	}
	
	public Integer getIdIngreso() {
		return idIngreso;
	}
	
	public void setIdIngreso(Integer idIngreso) {
		this.idIngreso = idIngreso;
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

	public UnidadDeManejo getUnidadDeManejo() {
		return unidadDeManejo;
	}

	public void setUnidadDeManejo(UnidadDeManejo unidadDeManejo) {
		this.unidadDeManejo = unidadDeManejo;
	}
	
	
}
