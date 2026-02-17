package com.ferbo.clientes.mail.beans;

import java.math.BigDecimal;
import java.util.Date;

public class DetallePartida {
	private int        idDetPartida         = -1;   //En BD es el campo det_part_cve
	private int        idPartida            = -1;   //En BD es el campo partida_cve
	private int        idTipoMovimiento     = -1;   //En BD es el campo tipo_mov_cve
	private int        idEstadoInventario   = -1;   //En BD es el campo edo_inv_cve 
	private int        idDetPartidaAnterior = -1;   //En BD es el campo det_anterior
	private int        idPartidaAnterior    = -1;   //En BD es el campo det_part_anterior
	private int        idDetPartidaPadre    = -1;   //En BD es el campo det_padre
	private int        idPartidaPadre       = -1;   //En BD es el campo det_part_padre
	private int        cantidadManejo       = -1;   //En BD es el campo cantidad_u_manejo
	private int        idUnidadMedida       = -1;   //En BD es el campo u_medida_cve
	private BigDecimal cantidadMedida       = null; //En BD es el campo cantidad_u_medida
	private String     codigo               = null; //En BD es el campo dtp_codigo
    private String     lote                 = null; //En BD es el campo dtp_lote
    private Date       caducidad            = null; //En BD es el campo dtp_caducidad
    private String     po                   = null; //En BD es el campo dtp_PO
    private String     mp                   = null; //En BD es el campo dtp_MP
    private String     pedimento            = null; //En BD es el campo dtp_pedimento
    private String     sap                  = null; //En BD es el campo dtp_SAP
    private String     tarimas              = null; //En BD es el campo dtp_tarimas
    
    /**Id Detalle Partida (En BD: det_part_cve)
     * @param idDetPartida
     */
    public void setIdDetPartida(int idDetPartida) {
    	this.idDetPartida = idDetPartida;
    }
    
    /**Id Detalle Partida (En BD: det_part_cve)
     * @param idDetPartida
     */
    public int getIdDetPartida() {
    	return this.idDetPartida;
    }
    
    /**Id Partida (En BD: partida_cve)
     * @param idPartida
     */
    public void setIdPartida(int idPartida) {
    	this.idPartida = idPartida;
    }
    
    /**Id Partida (En BD: partida_cve)
     * @param idPartida
     */
    public int getIdPartida() {
    	return this.idPartida;
    }
    
    /**Id Tipo Movimiento (En BD: tipo_mov_cve)
     * @param idTipoMovimiento
     */
    public void setIdTipoMovimiento(int idTipoMovimiento) {
    	this.idTipoMovimiento = idTipoMovimiento;
    }
    
    /**Id Tipo Movimiento (En BD: tipo_mov_cve)
     * @param idTipoMovimiento
     */
    public int getIdTipoMovimiento() {
    	return this.idTipoMovimiento;
    }
    
    /**Id Estado Inventario (En BD: edo_inv_cve)
     * @param idEstadoInventario
     */
    public void setIdEstadoInventario(int idEstadoInventario) {
    	this.idEstadoInventario = idEstadoInventario;
    }
    
    /**Id Estado Inventario (En BD: edo_inv_cve)
     * @param idEstadoInventario
     */
    public int getIdEstadoInventario() {
    	return this.idEstadoInventario;
    }
    
    /**Id Detalle Partida - Anterior (En BD: det_anterior)
     * @param idDetPartidaAnterior
     */
    public void setIdDetPartidaAnterior(int idDetPartidaAnterior) {
    	this.idDetPartidaAnterior = idDetPartidaAnterior;
    }
    
    /**Id Detalle Partida - Anterior (En BD: det_anterior)
     * @param idDetPartidaAnterior
     */
    public int getIdDetPartidaAnterior() {
    	return this.idDetPartidaAnterior;
    }
    
    /**Id Partida - Anterior (En BD: det_part_anterior)
     * @param idPartidaAnterior
     */
    public void setIdPartidaAnterior(int idPartidaAnterior) {
    	this.idPartidaAnterior = idPartidaAnterior;
    }
    
    /**Id Partida - Anterior (En BD: det_part_anterior)
     * @param idPartidaAnterior
     */
    public int getIdPartidaAnterior(){
    	return this.idPartidaAnterior;
    }
    
    /**Id Detalle Partida - Padre (En BD: det_padre)
     * @param idDetPartidaPadre
     */
    public void setIdDetPartidaPadre(int idDetPartidaPadre) {
    	this.idDetPartidaPadre = idDetPartidaPadre;
    }
    
    /**Id Detalle Partida - Padre (En BD: det_padre)
     * @param idDetPartidaPadre
     */
    public int getIdDetPartidaPadre() {
    	return this.idDetPartidaPadre;
    }
    
    /**Id Partida - Padre (En BD: det_part_padre)
     * @param idPartidaPadre
     */
    public void setIdPartidaPadre(int idPartidaPadre) {
    	this.idPartidaPadre = idPartidaPadre;
    }
    
    /**Id Partida - Padre (En BD: det_part_padre)
     * @param idPartidaPadre
     */
    public int getIdPartidaPadre() {
    	return this.idPartidaPadre;
    }
    
    /**Cantidad de Manejo - Cajas, Tarimas, etc (En BD: cantidad_u_manejo)
     * @param cantidadManejo
     */
    public void setCantidadManejo(int cantidadManejo) {
    	this.cantidadManejo = cantidadManejo;
    }
    
    /**Cantidad de Manejo - Cajas, Tarimas, etc (En BD: cantidad_u_manejo)
     * @param cantidadManejo
     */
    public int getCantidadManejo() {
    	return this.cantidadManejo;
    }
    
    /**Id Unidad de Medida (En BD: u_medida_cve)
     * @param idUnidadMedida
     */
    public void setIdUnidadMedida(int idUnidadMedida) {
    	this.idUnidadMedida = idUnidadMedida;
    }
    
    /**Id Unidad de Medida (En BD: u_medida_cve)
     * @param idUnidadMedida
     */
    public int getIdUnidadMedida() {
    	return this.idUnidadMedida;
    }
    
    /**Cantidad de Medida - Kilogramos (En BD: cantidad_u_medida)
     * @param cantidadMedida
     */
    public void setCantidadMedida(BigDecimal cantidadMedida) {
    	this.cantidadMedida = cantidadMedida;
    }
    
    /**Cantidad de Medida - Kilogramos (En BD: cantidad_u_medida)
     * @param cantidadMedida
     */
    public BigDecimal getCantidadMedida() {
    	return this.cantidadMedida;
    }
    
    /**Codigo (En BD: dtp_codigo)
     * @param codigo
     */
    public void setCodigo(String codigo) {
    	this.codigo = codigo;
    }
    
    /**Codigo (En BD: dtp_codigo)
     * @param codigo
     */
    public String getCodigo() {
    	return this.codigo;
    }
    
    /**Lote (En BD: dtp_lote)
     * @param lote
     */
    public void setLote(String lote) {
    	this.lote = lote;
    }
    
    /**Lote (En BD: dtp_lote)
     * @param lote
     */
    public String getLote() {
    	return this.lote;
    }
    
    /**Caducidad (En BD: dtp_caducidad)
     * @param caducidad
     */
    public void setCaducidad(Date caducidad) {
    	this.caducidad = caducidad;
    }
    
    /**Caducidad (En BD: dtp_caducidad)
     * @param caducidad
     */
    public Date getCaducidad() {
    	return this.caducidad;
    }
    
    /**PO (En BD: dtp_PO)
     * @param po
     */
    public void setPO(String po) {
    	this.po = po;
    }
    
    /**PO (En BD: dtp_PO)
     * @param po
     */
    public String getPO() {
    	return this.po;
    }
    
    /**MP (En BD: dtp_MP)
     * @param mp
     */
    public void setMP(String mp) {
    	this.mp = mp;
    }
    
    /**MP (En BD: dtp_MP)
     * @param mp
     */
    public String getMP(){
    	return this.mp;
    }
    
    /**Pedimento (En BD: dtp_pedimento)
     * @param pedimento
     */
    public void setPedimento(String pedimento) {
    	this.pedimento = pedimento;
    }
    
    /**Pedimento (En BD: dtp_pedimento)
     * @param pedimento
     */
    public String getPedimento() {
    	return this.pedimento;
    }
    
    /**SAP (En BD: dtp_sap)
     * @param sap
     */
    public void setSAP(String sap) {
    	this.sap = sap;
    }
    
    /**SAP (En BD: dtp_sap)
     * @param sap
     */
    public String getSAP() {
    	return this.sap;
    }
    
    /**Tarimas (En BD: dtp_tarimas)
     * @param tarimas
     */
    public void setTarimas(String tarimas) {
    	this.tarimas = tarimas;
    }
    
    /**Tarimas (En BD: dtp_tarimas)
     * @param tarimas
     */
    public String getTarimas(){
    	return this.tarimas;
    }
}