package com.ferbo.clientes.business;

import java.math.BigDecimal;

public class AdeudoVencidoException extends Exception {

	private static final long serialVersionUID = 9103979712206495680L;
	
	private BigDecimal importe;
	private Integer salidasSolicitadas;
	private Integer salidasPermitidas;
	
	public AdeudoVencidoException() {
		super();
	}

	public AdeudoVencidoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AdeudoVencidoException(String message, Throwable cause) {
		super(message, cause);
	}

	public AdeudoVencidoException(String message) {
		super(message);
	}

	public AdeudoVencidoException(Throwable cause) {
		super(cause);
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public Integer getSalidasSolicitadas() {
		return salidasSolicitadas;
	}

	public void setSalidasSolicitadas(Integer salidasSolicitadas) {
		this.salidasSolicitadas = salidasSolicitadas;
	}

	public Integer getSalidasPermitidas() {
		return salidasPermitidas;
	}

	public void setSalidasPermitidas(Integer salidasPermitidas) {
		this.salidasPermitidas = salidasPermitidas;
	}
}
