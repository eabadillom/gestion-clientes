package com.ferbo.clientes.beans;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.util.Conexion;
import com.ferbo.clientes.util.DateUtils;
import com.ferbo.clientes.util.JasperReportUtil;

@Named(value = "mbEntrada")
@ViewScoped
public class MbEntrada implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private static Logger log = LogManager.getLogger(MbEntrada.class);
	
	private Date fechaInicio;
	
	private Date fechaFin;
	
	private FacesContext faceContext;
	private HttpServletRequest request;
	private HttpSession session;
	private Cliente cliente;
	
	public MbEntrada() {
		this.fechaInicio = new Date();
		this.fechaFin = new Date();
	}
	
	@PostConstruct
	public void init() {
		fechaInicio = new Date();
		fechaFin = new Date();
		faceContext = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        session = request.getSession(false);
		this.cliente = (Cliente) session.getAttribute("cliente");
	}
	
	public void makeReport() {
		String reportNameJASPER = "/jasper/entradas.jrxml";
		File reportFile = new File(getClass().getResource(reportNameJASPER).getFile());
		log.info("Entrando a generacion de reporte de entradas...");
		log.info(reportFile.getPath());
		
		String sLogoPath = "/images/logo.png";
		File logoFile = new File(getClass().getResource(sLogoPath).getFile());
		log.info("Imagen: " + logoFile.getPath());
		
		String fileName = null;
		
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		
		try {
			parameters = new HashMap<String, Object>();
			fileName = "entradas_"
					+ DateUtils.getString(this.fechaInicio, DateUtils.FORMATO_YYYY_MM_DD)
					+ "-"
					+ DateUtils.getString(this.fechaFin, DateUtils.FORMATO_YYYY_MM_DD)
					+ ".pdf"
					;
			connection = Conexion.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("idCliente", this.cliente.getIdCliente());
			parameters.put("fechaInicio", this.fechaInicio);
			parameters.put("fechaFin", this.fechaFin);
			parameters.put("imagen", logoFile.getPath());
			jasperReportUtil.createPdf(fileName, parameters, reportFile.getPath());
			log.info("Terminando generacion de reporte de entradas...");
		} catch (SQLException ex) {
			log.error("Problema en base de datos...", ex);
		} catch(Exception ex) {
			log.error("Problema general...", ex);
		} finally {
			Conexion.close(connection);
		}
			
	}
	
	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
}
