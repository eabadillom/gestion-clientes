package com.ferbo.clientes.beans;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.util.Conexion;
import com.ferbo.gestion.reports.jasper.ReporteInventarioJR;

@Named(value = "mbInventario")
@ViewScoped
public class MbInventario implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger log = LogManager.getLogger(MbInventario.class);

	private Date fechaCorte;
	private Date hoy;

	private FacesContext faceContext;
	private HttpServletRequest request;
	private HttpSession session;
	private Cliente cliente;
	private ReporteInventarioJR reporteInventarioJR;
	private StreamedContent file;
	
	@PostConstruct
	public void init() {
		this.fechaCorte = new Date();
		this.hoy = new Date();
		this.faceContext = FacesContext.getCurrentInstance();
		this.request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		this.session = request.getSession(false);
		this.cliente = (Cliente) session.getAttribute("cliente");
		log.info("El cliente {} ha ingresado a reportes de inventario.", this.cliente.getNombre());
	}
	
	public void generateReport() {
		String reportNameJASPER = "/jasper/inventario.jrxml";
		File reportFile = new File(getClass().getResource(reportNameJASPER).getFile());
		log.info(reportFile.getPath());
		
		String sLogoPath = "/images/logo.png";
		File logoFile = new File(getClass().getResource(sLogoPath).getFile());
		log.info("Imagen: " + logoFile.getPath());
		
		Connection connection = null;
		try {
			connection = Conexion.getConnection();
			this.reporteInventarioJR = new ReporteInventarioJR(connection, sLogoPath);
			byte[] bytes = this.reporteInventarioJR.getPDFReporteInventario(this.cliente.getIdCliente(), null);
			InputStream input = new ByteArrayInputStream(bytes);
			this.file = DefaultStreamedContent.builder().contentType("application/pdf").name(getNameFilePdf()).stream(() -> input).build();
			log.info("Terminando generacion de reporte de inventarios...");
		} catch (SQLException ex) {
			log.error("Problema en base de datos...", ex);
		} catch(Exception ex) {
			log.error("Problema general...", ex);
		} finally {
			Conexion.close(connection);
		}
	}
	
	public String getNameFilePdf() {
		DateFormat dateFormatPeriodo = new SimpleDateFormat("yyyy-mm-dd"); 
		String strDatePeriodo = dateFormatPeriodo.format(getFechaCorte());
		String nombreArchivo = String.format("Inventario_%s.pdf", strDatePeriodo);
		return nombreArchivo;
	}
	
	public Date getFechaCorte() {
		return fechaCorte;
	}

	public void setFechaCorte(Date periodo) {
		this.fechaCorte = periodo;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}
	
	public Date getHoy() {
		return hoy;
	}

	public void setHoy(Date hoy) {
		this.hoy = hoy;
	}
}
