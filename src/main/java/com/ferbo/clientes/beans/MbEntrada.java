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
import com.ferbo.gestion.reports.jasper.ReporteEntradasJR;

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
        private ReporteEntradasJR reporteEntradasJR;
        private StreamedContent file;
	
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
                log.info("El cliente {} ha ingresado a reportes de entradas.", this.cliente.getNombre());
	}
	
	public void makeReport() {
		String reportNameJASPER = "/jasper/entradas.jrxml";
		File reportFile = new File(getClass().getResource(reportNameJASPER).getFile());
		log.info("Entrando a generacion de reporte de entradas...");
		log.info(reportFile.getPath());
		
		String sLogoPath = "/images/logo.png";
		File logoFile = new File(getClass().getResource(sLogoPath).getFile());
		log.info("Imagen: " + logoFile.getPath());
		
		Connection connection = null;
		
		try {
			connection = Conexion.getConnection();
                        reporteEntradasJR = new ReporteEntradasJR(connection, sLogoPath);
                        byte[] bytes = reporteEntradasJR.getPDF(fechaInicio, fechaFin, cliente.getIdCliente(), null, null);
                        InputStream input = new ByteArrayInputStream(bytes);
                        this.file = DefaultStreamedContent.builder().contentType("application/pdf").name(getNameFilePdf()).stream(() -> input).build();
			log.info("Terminando generacion de reporte de entradas...");
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
		String strFechaInicio = dateFormatPeriodo.format(getFechaInicio());
                String strFechaFin = dateFormatPeriodo.format(getFechaFin());
                String nombreArchivo = String.format("Entradas_%s-%s.pdf", strFechaInicio, strFechaFin);
		return nombreArchivo;
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

        public StreamedContent getFile() {
            return file;
        }

        public void setFile(StreamedContent file) {
            this.file = file;
        }
        
}
