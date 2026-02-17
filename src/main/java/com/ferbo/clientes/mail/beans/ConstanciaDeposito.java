package com.ferbo.clientes.mail.beans;

import java.util.Date;
import java.math.BigDecimal;

public class ConstanciaDeposito{
	private int idConstancia = -1;
	private int idCliente = -1;
	private Date fechaIngreso = null;
	private String nombreTransportista = null;
	private String placasTransporte = null;
	private String observaciones = null;
	private String folioCliente = null;
	private BigDecimal valorDeclarado = null;
	private int status = -1;
	private int avisoCve = -1;
	private String temperatura = null;
	
	public void setIdConstancia(int idConstancia){
		this.idConstancia = idConstancia;
	}
	
	public int getIdConstancia(){
		return this.idConstancia;
	}
	
	public void setIdCliente(int idCliente){
		this.idCliente = idCliente;
	}
	
	public int getIdCliente(){
		return this.idCliente;
	}
	
	public void setFechaIngreso(Date fechaIngreso){
		this.fechaIngreso = fechaIngreso;
	}
	
	public Date getFechaIngreso(){
		return this.fechaIngreso;
	}
	
	public void setNombreTransportista(String nombreTransportista){
		this.nombreTransportista = nombreTransportista;
	}
	
	public String getNombreTransportista(){
		return this.nombreTransportista;
	}
	
	public void setPlacasTransporte(String placasTransporte){
		this.placasTransporte = placasTransporte;
	}
	
	public String getPlacasTransporte(){
		return this.placasTransporte;
	}
	
	public void setObservaciones(String observaciones){
		this.observaciones = observaciones;
	}
	
	public String getObservaciones(){
		return this.observaciones;
	}
	
	public void setFolioCliente(String folioCliente){
		this.folioCliente = folioCliente;
	}
	
	public String getFolioCliente(){
		return this.folioCliente;
	}
	
	public void setValorDeclarado(BigDecimal valorDeclarado){
		this.valorDeclarado = valorDeclarado;
	}
	
	public BigDecimal getValorDecimal(){
		return this.valorDeclarado;
	}
	
	public void setStatus(int status){
		this.status = status;
	}
	
	public int getStatus(){
		return this.status;
	}
	
	public void setAvisoCve(int avisoCve){
		this.avisoCve = avisoCve;
	}
	
	public int getAvisoCve(){
		return this.avisoCve;
	}
	
	public void setTemperatura(String temperatura){
		this.temperatura = temperatura;
	}
	
	public String getTemperatura(){
		return this.temperatura;
	}
	
	//Los siguientes valores representan el estado (status) de una constancia de depósito, de acuerdo a su ciclo de vida.
	public static int NUEVA = 0;
	public static int FACTURADA = 1;
	public static int ABIERTA = 2;
	public static int CERRADA = 3;
	public static int CANCELADA = 4;
}