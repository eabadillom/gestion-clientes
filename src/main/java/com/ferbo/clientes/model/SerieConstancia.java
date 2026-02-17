package com.ferbo.clientes.model;

import java.util.Objects;

public class SerieConstancia {
    private Integer idCliente = null;
    private String tipoSerie = null;
    private Integer numero = null;
    private Integer idPlanta = -1;

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getTipoSerie() {
        return tipoSerie;
    }

    public void setTipoSerie(String tp_serie) {
        this.tipoSerie = tp_serie;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

	public Integer getIdPlanta() {
		return idPlanta;
	}

	public void setIdPlanta(Integer idPlanta) {
		this.idPlanta = idPlanta;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idCliente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SerieConstancia other = (SerieConstancia) obj;
		return idCliente == other.idCliente;
	}

	@Override
	public String toString() {
		return "SerieConstancia [id=" + idCliente + ", tipoSerie=" + tipoSerie + ", numero=" + numero + ", idPlanta="
				+ idPlanta + "]";
	}
}
