package com.ferbo.clientes.beans;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.business.AntiguedadSaldosBL;
import com.ferbo.clientes.business.ClienteBL;
import com.ferbo.clientes.dto.AntiguedadSaldosDTO;
import com.ferbo.clientes.util.ClientesException;
import com.ferbo.gestion.core.model.cliente.Cliente;

@Named
@ViewScoped
public class AdeudoVencidoBean implements Serializable {

	private static final long serialVersionUID = 8261113888715880779L;
	private static Logger log = LogManager.getLogger(AdeudoVencidoBean.class);
	private String contextPath = null;
	private String mensaje = null;
	
	@Inject
	private AntiguedadSaldosBL saldoBO;
	private List<AntiguedadSaldosDTO> saldos;
	private AntiguedadSaldosDTO saldo;
	
	@Inject
	private ClienteBL clienteBO;
	private Cliente cliente;
	private Integer idCliente;
	private BigDecimal saldoTotal;
	
	public AdeudoVencidoBean() {
		final com.ferbo.clientes.model.Cliente cliente;
		this.contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession(false);
		cliente = (com.ferbo.clientes.model.Cliente) session.getAttribute("cliente");
		this.idCliente = cliente.getIdCliente();
	}
	
	@PostConstruct
	public void init() {
		log.info("Entrando al post-construct...");
	}
	
	public void cargarAdeudo() {
		try {
			log.info("Entrando a cargar-adeudo...");
			this.cliente = clienteBO.buscar(this.idCliente);
			this.saldos = saldoBO.desglose(LocalDate.now(), cliente, null);
		} catch (ClientesException ex) {
			log.error("Problema para obtener la información del cliente...", ex);
		}
	}

	public void consultaFacturacion() {
		String path = null;
		
        try {
            log.info("Redirigiendo al reporte de facturacion...");
            path = this.contextPath + "/reportes/facturacion.xhtml";
            FacesContext.getCurrentInstance().getExternalContext().redirect(path);
        } catch (IOException ex) {
            log.error("Problema para redirigir al catalogo de dias no laborales...", ex);
        }
	}
	
	public String getMensaje() {
		return mensaje;
	}
	
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public AntiguedadSaldosDTO getSaldo() {
		return saldo;
	}

	public void setSaldo(AntiguedadSaldosDTO saldo) {
		this.saldo = saldo;
	}

	public BigDecimal getSaldoTotal() {
		return saldoTotal;
	}

	public void setSaldoTotal(BigDecimal saldoTotal) {
		this.saldoTotal = saldoTotal;
	}

	public List<AntiguedadSaldosDTO> getSaldos() {
		return saldos;
	}

	public void setSaldos(List<AntiguedadSaldosDTO> saldos) {
		this.saldos = saldos;
	}
}
