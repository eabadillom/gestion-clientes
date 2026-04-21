package com.ferbo.clientes.beans;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.sql.Connection;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.ferbo.clientes.business.ConstanciaDepositoBL;
import com.ferbo.clientes.business.KardexBL;
import com.ferbo.clientes.mail.beans.ConstanciaDeposito;
import com.ferbo.clientes.mail.mngr.ConstanciaDepositoDAO;
import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.util.ClientesException;
import com.ferbo.clientes.util.Conexion;

@Named
@ViewScoped
public class KardexBean implements Serializable {

	private static final long serialVersionUID = -6614349579953025334L;
	private static final Logger log = LogManager.getLogger(KardexBean.class);
	
	private Integer idCliente; 
	private Cliente cliente;
	private String  folioCliente = null;
	private ConstanciaDeposito constancia = null;
	
	private StreamedContent    kardexFile = null;
	private FacesContext       context;
	private HttpServletRequest request;
	private HttpSession        session;
	
	public KardexBean() {
		this.context = FacesContext.getCurrentInstance();
		this.request = (HttpServletRequest) this.context.getExternalContext().getRequest();
		this.session = this.request.getSession(false);
		this.cliente = (Cliente) this.session.getAttribute("cliente");
	}
	
	@PostConstruct
	public void init() {
        this.idCliente = this.cliente.getIdCliente();
        this.constancia = null;
	}
	
	public void buscar() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Folio";
		
		try {
			this.constancia = new ConstanciaDepositoBL(this.cliente.getIdCliente())
					.buscar(this.folioCliente)
					.orElseThrow(() -> new ClientesException("Entrada no encontrada."))
					;
			titulo = this.folioCliente;
			mensaje = "Entrada encontrada";
			severity = FacesMessage.SEVERITY_INFO;		
		} catch(ClientesException ex) {
			log.warn(ex.getMessage());
			this.constancia = null;
			titulo = "Verifique su información";
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			log.error("Problema para obtener el kardex...", ex);
			this.constancia = null;
			titulo = "Error";
			mensaje = "Ocurrió un problema con la descarga del kardex";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
		}
		
	}
	
	public void buscarPDF() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Folio";
		String fileName = null;
		
		try {
			log.info("El cliente {} intenta descargar el kardex {}...", this.cliente.getNumero(), this.folioCliente);
			
			fileName = String.format("kardex-%s.pdf", this.folioCliente);
			final byte[] bytes = new KardexBL().buscarPDF(this.idCliente, this.folioCliente);
			kardexFile = DefaultStreamedContent
					.builder()
					.contentType("application/pdf")
					.name(fileName)
					.stream(() -> new ByteArrayInputStream(bytes))
					.build();
			
			titulo = "Descarga completa";
			mensaje = "Verifique su kardex";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(ClientesException ex ) {
			log.warn(ex.getMessage());
			titulo = "Verifique su información";
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			log.error("Problema para obtener el kardex...", ex);
			titulo = "Error";
			mensaje = "Ocurrió un problema con la descarga del kardex";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
		}
	}
	
	public void buscarXLSX() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Folio";
		
		String fileName = null;
		try {
			fileName = String.format("kardex-%s.xlsx", this.folioCliente);
			final byte[] bytes = new KardexBL().buscarXLSX(this.idCliente, this.folioCliente);
			kardexFile = DefaultStreamedContent
					.builder()
					.contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
					.name(fileName)
					.stream(() -> new ByteArrayInputStream(bytes))
					.build();
			
			titulo = "Descarga completa";
			mensaje = "Verifique su kardex";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(ClientesException ex ) {
			log.warn(ex.getMessage());
			titulo = "Verifique su información";
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			log.error("Problema para obtener el kardex...", ex);
			titulo = "Error";
			mensaje = "Ocurrió un problema con la descarga del kardex";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
		}
	}
	
	public String getFolioCliente() {
		return folioCliente;
	}

	public void setFolioCliente(String folioCliente) {
		this.folioCliente = folioCliente;
	}

	public StreamedContent getKardexFile() {
		return kardexFile;
	}

	public void setKardexFile(StreamedContent kardexFile) {
		this.kardexFile = kardexFile;
	}

	public ConstanciaDeposito getConstancia() {
		return constancia;
	}

	public void setConstancia(ConstanciaDeposito constancia) {
		this.constancia = constancia;
	}
}
