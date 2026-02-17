package com.ferbo.clientes.mail.beans;

import java.math.BigDecimal;

public class Partida{
	private int              idPartida        = -1;//partida_cve
	private int              idCamara         = -1; //camara_cve
	private int              folio            = -1; //id constancia deposito
	private BigDecimal       pesoTotal        = null;
	private int              cantidadTotal    = -1;
	private int              idUnidadProducto = -1; //unidad_de_producto_cve
	private BigDecimal       cantidadCobro    = null;
	private int              unidadCobro      = -1;
	private int              partidaSeq       = -1;
	private BigDecimal       valorMercancia   = null;
	private BigDecimal       rendimiento      = null;
	private UnidadProducto   unidad           = null;
	private BigDecimal       no_tarimas       = null;
	private DetallePartida   detallePartida       = null;
	
	public void setIdPartida(int idPartida){
		this.idPartida = idPartida;
	}
	
	public int getIdPartida(){
		return this.idPartida;
	}
	
	public void setIdCamara(int idCamara){
		this.idCamara = idCamara;
	}
	
	public int getIdCamara(){
		return this.idCamara;
	}
	
	public void setFolio(int folio){
		this.folio = folio;
	}
	
	public int getFolio(){
		return this.folio;
	}
	
	public void setPesoTotal(BigDecimal pesoTotal){
		this.pesoTotal = pesoTotal;
	}
	
	public BigDecimal getPesoTotal(){
		return this.pesoTotal;
	}
	
	public void setCantidadTotal(int cantidadTotal){
		this.cantidadTotal = cantidadTotal;
	}
	
	public int getCantidadTotal(){
		return this.cantidadTotal;
	}
	
	public void setIdUnidadProducto(int idUnidadProducto){
		this.idUnidadProducto = idUnidadProducto;
	}
	
	public int getIdUnidadProducto(){
		return this.idUnidadProducto;
	}
	
	public void setCantidadCobro(BigDecimal cantidadCobro){
		this.cantidadCobro = cantidadCobro;
	}
	
	public BigDecimal getCantidadCobro(){
		return this.cantidadCobro;
	}
	
	public void setUnidadCobro(int unidadCobro){
		this.unidadCobro = unidadCobro;
	}
	
	public int getUnidadCobro(){
		return this.unidadCobro;
	}
	
	public void setPartidaSeq(int partidaSeq){
		this.partidaSeq = partidaSeq;
	}
	
	public int getPartidaSeq(){
		return this.partidaSeq;
	}
	
	public void setValorMercancia(BigDecimal valorMercancia){
		this.valorMercancia = valorMercancia;
	}
	
	public BigDecimal getValorMercancia(){
		return this.valorMercancia;
	}
	
	public void setRendimiento(BigDecimal rendimiento){
		this.rendimiento = rendimiento;
	}
	
	public BigDecimal getRendimiento(){
		return this.rendimiento;
	}
	
	public void setUnidadProducto(UnidadProducto unidad){
		this.unidad = unidad;
	}
	
	public UnidadProducto getUnidadProducto(){
		return unidad;
	}
	
	public BigDecimal getNoTarimas() {
		return no_tarimas;
	}

	public void setNoTarimas(BigDecimal no_tarimas) {
		this.no_tarimas = no_tarimas;
	}

	public DetallePartida getDetallePartida() {
		return detallePartida;
	}

	public void setDetallePartida(DetallePartida detallePartida) {
		this.detallePartida = detallePartida;
	}
}
