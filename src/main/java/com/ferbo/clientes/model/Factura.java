package com.ferbo.clientes.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ferbo.clientes.mail.beans.Planta;

public class Factura {
	

	private Integer id;
	private Integer idCliente; //esta variable hace referencia a la columna "cliente" de la tabla "CLIENTE".
	private Cliente cliente;
	private String numero;
	private String moneda;
	private String rfc;
	private String nombreCliente;
	private Date fecha;
	private String observacion;
	private BigDecimal subtotal;
	private BigDecimal iva;
	private BigDecimal total;
	private String pais;
	private String estado;
	private String municipio;
	private String ciudad;
	private String colonia;
	private String cp;
	private String calle;
	private String numExt;
	private String numInt;
	private String telefono;
	private String fax;
	private BigDecimal porcentajeIva;
	private String numeroCliente;
	private BigDecimal valorDeclarado;
	private Date inicioServicios;
	private Date finServicios;
	private String montoLetra;
	private Integer idFacturacion;
	private TipoFacturacion tipoFacturacion;
	private Integer idPlanta;
	private Planta planta;
	private int plazo;
	private BigDecimal retencion;
	private String nomSerie;
	private Integer idStatus;
	private StatusFactura status;
	private String metodoPago;
	private String tipoPersona;
	private String cdRegimen;
	private String cdUsoCfdi;
	private String uuid;
	private String emisorNombre;
	private String emisorRFC;
	private String emisorCdRegimen;

	public Factura() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
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

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getColonia() {
		return colonia;
	}

	public void setColonia(String colonia) {
		this.colonia = colonia;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getNumExt() {
		return numExt;
	}

	public void setNumExt(String numExt) {
		this.numExt = numExt;
	}

	public String getNumInt() {
		return numInt;
	}

	public void setNumInt(String numInt) {
		this.numInt = numInt;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public BigDecimal getPorcentajeIva() {
		return porcentajeIva;
	}

	public void setPorcentajeIva(BigDecimal porcentajeIva) {
		this.porcentajeIva = porcentajeIva;
	}

	public String getNumeroCliente() {
		return numeroCliente;
	}

	public void setNumeroCliente(String numeroCliente) {
		this.numeroCliente = numeroCliente;
	}

	public BigDecimal getValorDeclarado() {
		return valorDeclarado;
	}

	public void setValorDeclarado(BigDecimal valorDeclarado) {
		this.valorDeclarado = valorDeclarado;
	}

	public Date getInicioServicios() {
		return inicioServicios;
	}

	public void setInicioServicios(Date inicioServicios) {
		this.inicioServicios = inicioServicios;
	}

	public Date getFinServicios() {
		return finServicios;
	}

	public void setFinServicios(Date finServicios) {
		this.finServicios = finServicios;
	}

	public String getMontoLetra() {
		return montoLetra;
	}

	public void setMontoLetra(String montoLetra) {
		this.montoLetra = montoLetra;
	}

	public TipoFacturacion getTipoFacturacion() {
		return tipoFacturacion;
	}

	public void setTipoFacturacion(TipoFacturacion tipoFacturacion) {
		this.tipoFacturacion = tipoFacturacion;
	}

	public Planta getPlanta() {
		return planta;
	}

	public void setPlanta(Planta planta) {
		this.planta = planta;
	}

	public int getPlazo() {
		return plazo;
	}

	public void setPlazo(int plazo) {
		this.plazo = plazo;
	}

	public BigDecimal getRetencion() {
		return retencion;
	}

	public void setRetencion(BigDecimal retencion) {
		this.retencion = retencion;
	}

	public String getNomSerie() {
		return nomSerie;
	}

	public void setNomSerie(String nomSerie) {
		this.nomSerie = nomSerie;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public StatusFactura getStatus() {
		return status;
	}

	public void setStatus(StatusFactura status) {
		this.status = status;
	}

	public String getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public String getCdRegimen() {
		return cdRegimen;
	}

	public void setCdRegimen(String cdRegimen) {
		this.cdRegimen = cdRegimen;
	}

	public String getCdUsoCfdi() {
		return cdUsoCfdi;
	}

	public void setCdUsoCfdi(String cdUsoCfdi) {
		this.cdUsoCfdi = cdUsoCfdi;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getEmisorNombre() {
		return emisorNombre;
	}

	public void setEmisorNombre(String emisorNombre) {
		this.emisorNombre = emisorNombre;
	}

	public String getEmisorRFC() {
		return emisorRFC;
	}

	public void setEmisorRFC(String emisorRFC) {
		this.emisorRFC = emisorRFC;
	}

	public String getEmisorCdRegimen() {
		return emisorCdRegimen;
	}

	public void setEmisorCdRegimen(String emisorCdRegimen) {
		this.emisorCdRegimen = emisorCdRegimen;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public Factura(Integer id, String numero, String moneda, String rfc, String nombreCliente, Date fecha,
			String observacion, BigDecimal subtotal, BigDecimal iva, BigDecimal total, String pais, String estado,
			String municipio, String ciudad, String colonia, String cp, String calle, String numExt, String numInt,
			String telefono, String fax, BigDecimal porcentajeIva, String numeroCliente, BigDecimal valorDeclarado,
			Date inicioServicios, Date finServicios, String montoLetra, TipoFacturacion tipoFacturacion, Planta planta,
			int plazo, BigDecimal retencion, String nomSerie, Cliente cliente, StatusFactura status, String metodoPago,
			String tipoPersona, String cdRegimen, String cdUsoCfdi, String uuid, String emisorNombre, String emisorRFC,
			String emisorCdRegimen) {
		super();
		this.id = id;
		this.numero = numero;
		this.moneda = moneda;
		this.rfc = rfc;
		this.nombreCliente = nombreCliente;
		this.fecha = fecha;
		this.observacion = observacion;
		this.subtotal = subtotal;
		this.iva = iva;
		this.total = total;
		this.pais = pais;
		this.estado = estado;
		this.municipio = municipio;
		this.ciudad = ciudad;
		this.colonia = colonia;
		this.cp = cp;
		this.calle = calle;
		this.numExt = numExt;
		this.numInt = numInt;
		this.telefono = telefono;
		this.fax = fax;
		this.porcentajeIva = porcentajeIva;
		this.numeroCliente = numeroCliente;
		this.valorDeclarado = valorDeclarado;
		this.inicioServicios = inicioServicios;
		this.finServicios = finServicios;
		this.montoLetra = montoLetra;
		this.tipoFacturacion = tipoFacturacion;
		this.planta = planta;
		this.plazo = plazo;
		this.retencion = retencion;
		this.nomSerie = nomSerie;
		this.cliente = cliente;
		this.status = status;
		this.metodoPago = metodoPago;
		this.tipoPersona = tipoPersona;
		this.cdRegimen = cdRegimen;
		this.cdUsoCfdi = cdUsoCfdi;
		this.uuid = uuid;
		this.emisorNombre = emisorNombre;
		this.emisorRFC = emisorRFC;
		this.emisorCdRegimen = emisorCdRegimen;
	}

	@Override
	public String toString() {
		return "Factura [id=" + id + ", numero=" + numero + ", moneda=" + moneda + ", rfc=" + rfc + ", nombreCliente="
				+ nombreCliente + ", fecha=" + fecha + ", observacion=" + observacion + ", subtotal=" + subtotal
				+ ", iva=" + iva + ", total=" + total + ", pais=" + pais + ", estado=" + estado + ", municipio="
				+ municipio + ", ciudad=" + ciudad + ", colonia=" + colonia + ", cp=" + cp + ", calle=" + calle
				+ ", numExt=" + numExt + ", numInt=" + numInt + ", telefono=" + telefono + ", fax=" + fax
				+ ", porcentajeIva=" + porcentajeIva + ", numeroCliente=" + numeroCliente + ", valorDeclarado="
				+ valorDeclarado + ", inicioServicios=" + inicioServicios + ", finServicios=" + finServicios
				+ ", montoLetra=" + montoLetra + ", tipoFacturacion=" + tipoFacturacion + ", planta=" + planta
				+ ", plazo=" + plazo + ", retencion=" + retencion + ", nomSerie=" + nomSerie + ", cliente=" + cliente
				+ ", status=" + status + ", metodoPago=" + metodoPago + ", tipoPersona=" + tipoPersona + ", cdRegimen="
				+ cdRegimen + ", cdUsoCfdi=" + cdUsoCfdi + ", uuid=" + uuid + ", emisorNombre=" + emisorNombre
				+ ", emisorRFC=" + emisorRFC + ", emisorCdRegimen=" + emisorCdRegimen + "]";
	}
	
	public Integer getIdStatus() {
		return idStatus;
	}

	public void setIdStatus(Integer idStatus) {
		this.idStatus = idStatus;
	}

	public Integer getIdFacturacion() {
		return idFacturacion;
	}

	public void setIdFacturacion(Integer idFacturacion) {
		this.idFacturacion = idFacturacion;
	}

	public Integer getIdPlanta() {
		return idPlanta;
	}

	public void setIdPlanta(Integer idPlanta) {
		this.idPlanta = idPlanta;
	}



}
