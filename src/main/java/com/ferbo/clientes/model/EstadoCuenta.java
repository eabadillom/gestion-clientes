package com.ferbo.clientes.model;

import java.math.BigDecimal;
import java.util.Date;

public class EstadoCuenta {
	
	private Integer cteCve;
	private String numeroCte;
	private String nombreCliente;
	private String nomSerie;
	private String Folio;
	private Date fecha;
	private String Movimiento;
	private String Concepto;
	private BigDecimal saldoInicial;
	private BigDecimal debe;
	private BigDecimal haber;
	private BigDecimal parcial;
	
	
	public EstadoCuenta() {
	}


	public Integer getCteCve() {
		return cteCve;
	}


	public void setCteCve(Integer cteCve) {
		this.cteCve = cteCve;
	}


	public String getNumeroCte() {
		return numeroCte;
	}


	public void setNumeroCte(String numeroCte) {
		this.numeroCte = numeroCte;
	}


	public String getNombreCliente() {
		return nombreCliente;
	}


	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}


	public String getNomSerie() {
		return nomSerie;
	}


	public void setNomSerie(String nomSerie) {
		this.nomSerie = nomSerie;
	}


	public String getFolio() {
		return Folio;
	}


	public void setFolio(String folio) {
		Folio = folio;
	}


	public Date getFecha() {
		return fecha;
	}


	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	public String getMovimiento() {
		return Movimiento;
	}


	public void setMovimiento(String movimiento) {
		Movimiento = movimiento;
	}


	public String getConcepto() {
		return Concepto;
	}


	public void setConcepto(String concepto) {
		Concepto = concepto;
	}


	public BigDecimal getSaldoInicial() {
		return saldoInicial;
	}


	public void setSaldoInicial(BigDecimal saldoInicial) {
		this.saldoInicial = saldoInicial;
	}


	public BigDecimal getDebe() {
		return debe;
	}


	public void setDebe(BigDecimal debe) {
		this.debe = debe;
	}


	public BigDecimal getHaber() {
		return haber;
	}


	public void setHaber(BigDecimal haber) {
		this.haber = haber;
	}


	public BigDecimal getParcial() {
		return parcial;
	}


	public void setParcial(BigDecimal parcial) {
		this.parcial = parcial;
	}


	@Override
	public String toString() {
		return "EstadoCuenta [cteCve=" + cteCve + ", numeroCte=" + numeroCte + ", nombreCliente=" + nombreCliente
				+ ", nomSerie=" + nomSerie + ", Folio=" + Folio + ", fecha=" + fecha + ", Movimiento=" + Movimiento
				+ ", Concepto=" + Concepto + ", saldoInicial=" + saldoInicial + ", debe=" + debe + ", haber=" + haber
				+ ", parcial=" + parcial + "]";
	}	
	
}
