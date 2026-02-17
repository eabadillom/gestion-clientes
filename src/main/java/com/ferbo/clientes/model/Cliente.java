package com.ferbo.clientes.model;

public class Cliente {

    private int idCliente = -1;
    private String nombre = null;
    private String rfc = null;
    private String numero = null;
    private String email = null;
    private String codigoUnico = null;
    private boolean habilitado = false;
    private String tipoPersona = null;
    private String claveRegimen = null;
    private String claveUsoCFDI = null;
    private String claveMetodoPago = null;
    private String claveFormaPago = null;
    private String regimenCapital = null;

    public Cliente(String nombre, String rfc) {
        this.nombre = nombre;
        this.rfc = rfc;
    }

    public Cliente() {
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRFC() {
        return rfc;
    }

    public void setRFC(String rfc) {
        this.rfc = rfc;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(String codigo_unico) {
        this.codigoUnico = codigo_unico;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    @Override
    public String toString() {
        return "Cliente [idCliente=" + idCliente + ", nombre=" + nombre + 
                ", rfc=" + rfc + ", numero=" + numero + ", email=" + email + 
                ", codigoUnico=" + codigoUnico + ", habilitado=" + 
                habilitado + "]";
    }

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public String getClaveRegimen() {
		return claveRegimen;
	}

	public void setClaveRegimen(String claveRegimen) {
		this.claveRegimen = claveRegimen;
	}

	public String getClaveUsoCFDI() {
		return claveUsoCFDI;
	}

	public void setClaveUsoCFDI(String claveUsoCFDI) {
		this.claveUsoCFDI = claveUsoCFDI;
	}

	public String getClaveMetodoPago() {
		return claveMetodoPago;
	}

	public void setClaveMetodoPago(String claveMetodoPago) {
		this.claveMetodoPago = claveMetodoPago;
	}

	public String getClaveFormaPago() {
		return claveFormaPago;
	}

	public void setClaveFormaPago(String claveFormaPago) {
		this.claveFormaPago = claveFormaPago;
	}

	public String getRegimenCapital() {
		return regimenCapital;
	}

	public void setRegimenCapital(String regimenCapital) {
		this.regimenCapital = regimenCapital;
	}
}
