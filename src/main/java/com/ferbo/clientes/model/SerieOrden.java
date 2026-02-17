package com.ferbo.clientes.model;

import java.util.Objects;

public class SerieOrden {
	private Integer idCliente = null;
	private String tipoSerie = null;
	private Integer idPlanta = null;
	private Integer numero = null;
	
	public Integer getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}
	public String getTipoSerie() {
		return tipoSerie;
	}
	public void setTipoSerie(String tipoSerie) {
		this.tipoSerie = tipoSerie;
	}
	public Integer getIdPlanta() {
		return idPlanta;
	}
	public void setIdPlanta(Integer idPlanta) {
		this.idPlanta = idPlanta;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	@Override
	public int hashCode() {
		return Objects.hash(idCliente, idPlanta, tipoSerie);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SerieOrden other = (SerieOrden) obj;
		return Objects.equals(idCliente, other.idCliente) && Objects.equals(idPlanta, other.idPlanta)
				&& Objects.equals(tipoSerie, other.tipoSerie);
	}
	@Override
	public String toString() {
		return "SerieOrden [idCliente=" + idCliente + ", tipoSerie=" + tipoSerie + ", idPlanta=" + idPlanta
				+ ", numero=" + numero + "]";
	}
}
