package com.ferbo.clientes.beans;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.ferbo.clientes.util.JasperReportUtil;

@Named(value = "mbInventario")
@ViewScoped
public class MbInventario implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger log = LogManager.getLogger(MbInventario.class);

	private Date fechaCorte;
	
	private FacesContext faceContext;
	private HttpServletRequest request;
	private HttpSession session;
	private Cliente cliente;
	
	@PostConstruct
	public void init() {
		this.fechaCorte = new Date();
		
		faceContext = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        session = request.getSession(false);
		this.cliente = (Cliente) session.getAttribute("cliente");
	}
	
	public void generateReport() {
		String reportNameJASPER = "/jasper/inventario.jrxml";
		File reportFile = new File(getClass().getResource(reportNameJASPER).getFile());
		log.info(reportFile.getPath());
		
		String sLogoPath = "/images/logo.png";
		File logoFile = new File(getClass().getResource(sLogoPath).getFile());
		log.info("Imagen: " + logoFile.getPath());
		
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		parameters = new HashMap<String, Object>();
		try {
			connection = Conexion.getConnection();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("idCliente", this.cliente.getIdCliente());
			parameters.put("fecha", getFechaCorte());
			parameters.put("imagen", logoFile.getPath());
			log.info("Parametros: " + parameters.toString());
			jasperReportUtil.createPdf(getNameFilePdf(), parameters, reportFile.getPath());
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
		return "inventario"+"_"+strDatePeriodo+".pdf";
	}
	
	public Date getFechaCorte() {
		return fechaCorte;
	}

	public void setFechaCorte(Date periodo) {
		this.fechaCorte = periodo;
	}

}
