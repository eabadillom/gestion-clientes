package com.ferbo.clientes.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Inventario {
	
	private String codigo;
	private String producto;
	private Date caducidad;
	private String sap;
	private String po;
	private String pedimento;
	private String lote;
	private String tarima;
	private Integer existencia;
	private String unidad;
	private Integer cantidad;
	private BigDecimal pesoAprox;
	private BigDecimal peso;
	private Integer partidaClave;
	private String folioCliente;
	private Integer folio;
	private boolean habilitado;
	private String plantaNombre;
	private String plantaAbrev;
	
	public Inventario() {
	}
	
	public Inventario(String codigo, String producto, Date caducidad, String sap, String po, String lote, String tarima,
			Integer existencia, String unidad, Integer cantidad, BigDecimal peso, BigDecimal pesoAprox, Integer partidaClave, String folioCliente, Integer folio,boolean habilitado, String plantaNombre, String plantaAbrev) {
		this.codigo = codigo;
		this.producto = producto;
		this.caducidad = caducidad;
		this.sap = sap;
		this.po = po;
		this.lote = lote;
		this.tarima = tarima;
		this.existencia = existencia;
		this.unidad = unidad;
		this.cantidad = cantidad;
		this.peso = peso;
		this.pesoAprox = pesoAprox;
		this.partidaClave = partidaClave;
		this.folioCliente = folioCliente;
		this.folio = folio;
		this.habilitado = habilitado;
		this.plantaNombre = plantaNombre;
		this.plantaAbrev = plantaAbrev;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public Date getCaducidad() {
		return caducidad;
	}

	public void setCaducidad(Date caducidad) {
		this.caducidad = caducidad;
	}

	public String getSap() {
		return sap;
	}

	public void setSap(String sap) {
		this.sap = sap;
	}

	public String getPo() {
		return po;
	}

	public void setPo(String po) {
		this.po = po;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getTarima() {
		return tarima;
	}

	public void setTarima(String tarima) {
		this.tarima = tarima;
	}

	public Integer getExistencia() {
		return existencia;
	}

	public void setExistencia(Integer existencia) {
		this.existencia = existencia;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getPesoAprox() {
		return pesoAprox;
	}

	public void setPesoAprox(BigDecimal pesoAprox) {
		this.pesoAprox = pesoAprox;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public Integer getPartidaClave() {
		return partidaClave;
	}

	public void setPartidaClave(Integer partidaClave) {
		this.partidaClave = partidaClave;
	}

	public String getFolioCliente() {
		return folioCliente;
	}

	public void setFolioCliente(String folioCliente) {
		this.folioCliente = folioCliente;
	}

	public Integer getFolio() {
		return folio;
	}

	public void setFolio(Integer folio) {
		this.folio = folio;
	}

	public boolean isHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}

	public String getPlantaNombre() {
		return plantaNombre;
	}

	public void setPlantaNombre(String plantaNombre) {
		this.plantaNombre = plantaNombre;
	}

	public String getPlantaAbrev() {
		return plantaAbrev;
	}

	public void setPlantaAbrev(String plantaAbrev) {
		this.plantaAbrev = plantaAbrev;
	}

	public String getPedimento() {
		return pedimento;
	}

	public void setPedimento(String pedimento) {
		this.pedimento = pedimento;
	}

	@Override
	public String toString() {
		return "Inventario [codigo=" + codigo + ", producto=" + producto + ", caducidad=" + caducidad + ", sap=" + sap
				+ ", po=" + po + ", pedimento=" + pedimento + ", lote=" + lote + ", tarima=" + tarima + ", existencia="
				+ existencia + ", unidad=" + unidad + ", cantidad=" + cantidad + ", pesoAprox=" + pesoAprox + ", peso="
				+ peso + ", partidaClave=" + partidaClave + ", folioCliente=" + folioCliente + ", folio=" + folio
				+ ", habilitado=" + habilitado + ", plantaNombre=" + plantaNombre + ", plantaAbrev=" + plantaAbrev
				+ "]";
	}
}
