package com.ferbo.clientes.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class AntiguedadSaldosDTO {
	
	private Integer id = null;
	private String numeroCliente = null;
	private Integer idCliente = null;
	private String nombreCliente = null;
	private String emisorRFC = null;
	private String emisorNombre = null;
	
	private String serie = null;
	private String folio = null;
	private LocalDate fecha = null;
	private BigDecimal subtotal = null;
	private BigDecimal iva = null;
	private BigDecimal total = null;
	private BigDecimal saldo = null;
	private Integer idStatus = null;
	private String status = null;
	private Integer plazoPago = null;
	private Integer diasAtraso = null;
	private BigDecimal enPlazo = null;
	private BigDecimal atraso8dias = null;
	private BigDecimal atraso15dias = null;
	private BigDecimal atraso30dias = null; 
	private BigDecimal atraso60dias = null;
	private BigDecimal atrasoMayor60dias = null;
	
	@Override
	public int hashCode() {
		if(this.id == null)
			return System.identityHashCode(this);
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AntiguedadSaldosDTO other = (AntiguedadSaldosDTO) obj;
		return Objects.equals(id, other.id);
	}
	
	@Override
	public String toString() {
		return "{\"" + (id != null ? "id\":\"" + id + "\", \"" : "")
				+ (numeroCliente != null ? "numeroCliente\":\"" + numeroCliente + "\", \"" : "")
				+ (idCliente != null ? "idCliente\":\"" + idCliente + "\", \"" : "")
				+ (nombreCliente != null ? "nombreCliente\":\"" + nombreCliente + "\", \"" : "")
				+ (emisorRFC != null ? "emisorRFC\":\"" + emisorRFC + "\", \"" : "")
				+ (emisorNombre != null ? "emisorNombre\":\"" + emisorNombre + "\", \"" : "")
				+ (serie != null ? "serie\":\"" + serie + "\", \"" : "")
				+ (folio != null ? "folio\":\"" + folio + "\", \"" : "")
				+ (fecha != null ? "fecha\":\"" + fecha + "\", \"" : "")
				+ (subtotal != null ? "subtotal\":\"" + subtotal + "\", \"" : "")
				+ (iva != null ? "iva\":\"" + iva + "\", \"" : "")
				+ (total != null ? "total\":\"" + total + "\", \"" : "")
				+ (saldo != null ? "saldo\":\"" + saldo + "\", \"" : "")
				+ (idStatus != null ? "idStatus\":\"" + idStatus + "\", \"" : "")
				+ (status != null ? "status\":\"" + status + "\", \"" : "")
				+ (plazoPago != null ? "plazoPago\":\"" + plazoPago + "\", \"" : "")
				+ (diasAtraso != null ? "diasAtraso\":\"" + diasAtraso + "\", \"" : "")
				+ (enPlazo != null ? "enPlazo\":\"" + enPlazo + "\", \"" : "")
				+ (atraso8dias != null ? "atraso8dias\":\"" + atraso8dias + "\", \"" : "")
				+ (atraso15dias != null ? "atraso15dias\":\"" + atraso15dias + "\", \"" : "")
				+ (atraso30dias != null ? "atraso30dias\":\"" + atraso30dias + "\", \"" : "")
				+ (atraso60dias != null ? "atraso60dias\":\"" + atraso60dias + "\", \"" : "")
				+ (atrasoMayor60dias != null ? "atrasoMayor60dias\":\"" + atrasoMayor60dias : "") + "\"}";
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNumeroCliente() {
		return numeroCliente;
	}
	public void setNumeroCliente(String numeroCliente) {
		this.numeroCliente = numeroCliente;
	}
	public Integer getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public String getEmisorRFC() {
		return emisorRFC;
	}
	public void setEmisorRFC(String emisorRFC) {
		this.emisorRFC = emisorRFC;
	}
	public String getEmisorNombre() {
		return emisorNombre;
	}
	public void setEmisorNombre(String emisorNombre) {
		this.emisorNombre = emisorNombre;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getFolio() {
		return folio;
	}
	public void setFolio(String folio) {
		this.folio = folio;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
	public BigDecimal getIva() {
		return iva;
	}
	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	public Integer getIdStatus() {
		return idStatus;
	}
	public void setIdStatus(Integer idStatus) {
		this.idStatus = idStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getPlazoPago() {
		return plazoPago;
	}
	public void setPlazoPago(Integer plazoPago) {
		this.plazoPago = plazoPago;
	}
	public Integer getDiasAtraso() {
		return diasAtraso;
	}
	public void setDiasAtraso(Integer diasAtraso) {
		this.diasAtraso = diasAtraso;
	}
	public BigDecimal getEnPlazo() {
		return enPlazo;
	}
	public void setEnPlazo(BigDecimal enPlazo) {
		this.enPlazo = enPlazo;
	}
	public BigDecimal getAtraso8dias() {
		return atraso8dias;
	}
	public void setAtraso8dias(BigDecimal atraso8dias) {
		this.atraso8dias = atraso8dias;
	}
	public BigDecimal getAtraso15dias() {
		return atraso15dias;
	}
	public void setAtraso15dias(BigDecimal atraso15dias) {
		this.atraso15dias = atraso15dias;
	}
	public BigDecimal getAtraso30dias() {
		return atraso30dias;
	}
	public void setAtraso30dias(BigDecimal atraso30dias) {
		this.atraso30dias = atraso30dias;
	}
	public BigDecimal getAtraso60dias() {
		return atraso60dias;
	}
	public void setAtraso60dias(BigDecimal atraso60dias) {
		this.atraso60dias = atraso60dias;
	}
	public BigDecimal getAtrasoMayor60dias() {
		return atrasoMayor60dias;
	}
	public void setAtrasoMayor60dias(BigDecimal atrasoMayor60dias) {
		this.atrasoMayor60dias = atrasoMayor60dias;
	}
}
