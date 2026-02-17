package com.ferbo.clientes.mail.beans;

import java.util.Date;

public class PreSalida {
	private Integer idPreSalida = null;
	private String folioSalida = null;
	private String estado = null;
	private Date fechaSalida = null;
	private Date horaSalida = null;
	private String plataTransporte = null;
	private String nombreTransportista = null;
	private Integer idPartida = null;
	private Partida partida = null;
	private Integer folio = null;
	private Integer cantidad = null;
	private String folioCliente = null;
	
	public Integer getIdPreSalida() {
		return idPreSalida;
	}
	public void setIdPreSalida(Integer idPreSalida) {
		this.idPreSalida = idPreSalida;
	}
	public String getFolioSalida() {
		return folioSalida;
	}
	public void setFolioSalida(String folioSalida) {
		this.folioSalida = folioSalida;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Date getFechaSalida() {
		return fechaSalida;
	}
	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}
	public Date getHoraSalida() {
		return horaSalida;
	}
	public void setHoraSalida(Date horaSalida) {
		this.horaSalida = horaSalida;
	}
	public String getPlataTransporte() {
		return plataTransporte;
	}
	public void setPlacaTransporte(String plataTransporte) {
		this.plataTransporte = plataTransporte;
	}
	public String getNombreTransportista() {
		return nombreTransportista;
	}
	public void setNombreTransportista(String nombreTransportista) {
		this.nombreTransportista = nombreTransportista;
	}
	public Integer getIdPartida() {
		return idPartida;
	}
	public void setIdPartida(Integer idPartida) {
		this.idPartida = idPartida;
	}
	public Integer getFolio() {
		return folio;
	}
	public void setFolio(Integer folio) {
		this.folio = folio;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public Partida getPartida() {
		return partida;
	}
	public void setPartida(Partida partida) {
		this.partida = partida;
	}
	public String getFolioCliente() {
		return folioCliente;
	}
	public void setFolioCliente(String folioCliente) {
		this.folioCliente = folioCliente;
	}
	
}
