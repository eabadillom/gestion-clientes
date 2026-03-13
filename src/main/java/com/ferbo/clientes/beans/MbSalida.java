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
import com.ferbo.gestion.reports.jasper.ReporteSalidasJR;

@Named(value = "mbSalidas")
@ViewScoped
public class MbSalida implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger log = LogManager.getLogger(MbSalida.class);

	private Date fechaInicio;

	private Date fechaFin;
	
	private FacesContext faceContext;
	private HttpServletRequest request;
	private HttpSession session;
	private Cliente cliente;
        
        private ReporteSalidasJR reporteSalidasJR;
        private StreamedContent file;

	public MbSalida() {
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
                log.info("El cliente {} ha ingresado a reportes de salidas.", this.cliente.getNombre());
	}

	public void generateReport() {
		String reportNameJASPER = "/jasper/salidas.jrxml";
		File reportFile = new File(getClass().getResource(reportNameJASPER).getFile());
		log.info(reportFile.getPath());

		String sLogoPath = "/images/logo.jpeg";
		File logoFile = new File(getClass().getResource(sLogoPath).getFile());
		log.info("Imagen: " + logoFile.getPath());

		Connection connection = null;
		try {
			connection = Conexion.dsConexion();
                        reporteSalidasJR = new ReporteSalidasJR(connection, sLogoPath);
                        byte[] bytes = reporteSalidasJR.getPDF(fechaInicio, fechaFin, cliente.getIdCliente(), null, null);
                        InputStream input = new ByteArrayInputStream(bytes);
                        this.file = DefaultStreamedContent.builder().contentType("application/pdf").name(getNameFilePdf()).stream(() -> input).build();
                        log.info("Terminando generacion de reporte de salidas...");
		} catch (SQLException ex) {
			log.error("Problema en base de datos...", ex);
		} catch (Exception ex) {
			log.error("Problema general...", ex);
		} finally {
			Conexion.close(connection);
		}
	}

	public String getNameFilePdf() {
		DateFormat dateFormatPeriodo = new SimpleDateFormat("yyyy-mm-dd");
		String strDatePeriodoI = dateFormatPeriodo.format(getFechaInicio());
		String strDatePeriodoF = dateFormatPeriodo.format(getFechaFin());
                String nombreArchivo = String.format("Salidas_%s-%s.pdf", strDatePeriodoI, strDatePeriodoF);
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
