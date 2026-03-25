package com.ferbo.clientes.mail.report;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.business.SalidasBL;
import com.ferbo.clientes.mail.beans.Adjunto;
import com.ferbo.clientes.mail.beans.Camara;
import com.ferbo.clientes.mail.beans.ClienteContacto;
import com.ferbo.clientes.mail.beans.ConstanciaDeposito;
import com.ferbo.clientes.mail.beans.Correo;
import com.ferbo.clientes.mail.beans.Mail;
import com.ferbo.clientes.mail.beans.MedioContacto;
import com.ferbo.clientes.mail.beans.Partida;
import com.ferbo.clientes.mail.beans.Planta;
import com.ferbo.clientes.mail.beans.Usuario;
import com.ferbo.clientes.mail.business.ContactosBL;
import com.ferbo.clientes.mail.mngr.CamaraDAO;
import com.ferbo.clientes.mail.mngr.ConstanciaDepositoDAO;
import com.ferbo.clientes.mail.mngr.MailDAO;
import com.ferbo.clientes.mail.mngr.MedioContactoDAO;
import com.ferbo.clientes.mail.mngr.PartidaDAO;
import com.ferbo.clientes.mail.mngr.PlantaDAO;
import com.ferbo.clientes.mail.mngr.UsuarioDAO;
import com.ferbo.clientes.model.Salida;
import com.ferbo.clientes.model.SalidaDetalle;
import com.ferbo.clientes.util.Conexion;
import com.ferbo.clientes.util.DateUtils;
import com.ferbo.clientes.util.MailHelper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class SendMailOrdenSalida extends Thread {
	private static Logger log = LogManager.getLogger(SendMailOrdenSalida.class);
	private Connection conn = null;
	private Integer idCliente = null;
	private String folio = null;
	private List<Adjunto> alAdjuntos = null;

	public SendMailOrdenSalida(Connection conn) {
		this.conn = conn;
		this.alAdjuntos = new ArrayList<Adjunto>();
	}
	
	public SendMailOrdenSalida() {
		this.alAdjuntos = new ArrayList<Adjunto>();
	}
	
	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}
	
	public List<Adjunto> getAlAdjuntos() {
		return alAdjuntos;
	}

	public void setAlAdjuntos(List<Adjunto> alAdjuntos) {
		this.alAdjuntos = alAdjuntos;
	}
	
	public void add(Adjunto archivo) {
		if(this.alAdjuntos == null)
			alAdjuntos = new ArrayList<Adjunto>();
		
		this.alAdjuntos.add(archivo);
	}
	
	public void add(List<Adjunto> alAdjuntos) {
		if(this.alAdjuntos == null)
			alAdjuntos = new ArrayList<Adjunto>();
		
		this.alAdjuntos.addAll(alAdjuntos);
	}
	
	@Override
	public void run() {
		try {
			conn = Conexion.dsConexion();
			this.exec(this.folio);
		} catch(Exception ex) {
			log.error("Problema para el envío de la orden de salida por correo electrónico...", ex);
		} finally {
			Conexion.close(conn);
		}
	}

	public void exec(String folio) {
		Salida salida = null;
		List<Integer> alPlantas = null;
		Boolean isHorarioNoLaboral = null;
		Date fechaSalida = null;
		
		Date horaSalida = null;
		Date horaLimite = null;
		
		Integer diaSalida = null;

		try {
                        salida = SalidasBL.consultarSalida(conn, folio);
                        alPlantas = new ArrayList<Integer>();
                        
                        if (horaSalida == null)
                            horaSalida = new Date(salida.getHoraSalida().getTime());

                        if(fechaSalida == null)
                            fechaSalida = new Date(salida.getFechaSalida().getTime());
                                
                        List<SalidaDetalle> listSalidaDetalle = SalidasBL.consultarSalidasDetalles(conn, salida);
                        
                        for(SalidaDetalle detSalida : listSalidaDetalle){
                            Partida partida = PartidaDAO.get(conn, detSalida.getIdPartida());
                            ConstanciaDeposito deposito = ConstanciaDepositoDAO.getConstanciaDeposito(conn, partida.getFolio());

                            if (idCliente == null)
                                idCliente = new Integer(deposito.getIdCliente());

                            Camara camara = CamaraDAO.getCamara(conn, partida.getIdCamara());
                            if (alPlantas.contains(camara.getPlanta())) {
                                    continue;
                            }

                            alPlantas.add(new Integer(camara.getPlanta()));
                        }
                        
			diaSalida = DateUtils.getDiaSemana(fechaSalida);
			horaLimite = new Date(horaSalida.getTime());
			
			if(diaSalida.equals(DateUtils.SABADO)) {
				DateUtils.setTime(horaLimite, 13, 0, 0);
			} else if (diaSalida.equals(DateUtils.DOMINGO)) {
				DateUtils.setTime(horaLimite, 0, 0, 0);
			} else {
				DateUtils.setTime(horaLimite, 17, 0, 0);
			}
			
			log.info("Hora salida: {}", horaSalida);
			log.info("Hora limite: {}", horaLimite);
			
			if (horaSalida.getTime() >= horaLimite.getTime())
				isHorarioNoLaboral = new Boolean(true);
			
			this.process(conn, folio, alPlantas, isHorarioNoLaboral);

		} catch (Exception ex) {
			log.error("Problema general para crear el formato de reporte de orden de salida...", ex);
		}
	}

	private void process(Connection conn, String folio, List<Integer> alPlantas, Boolean isHorarioEspecial) {
		for (Integer idPlanta : alPlantas) {
                    	this.processReport(conn, folio, idPlanta, isHorarioEspecial);
		}
	}

	private void processReport(Connection conn, String folio, Integer idPlanta, Boolean isHorarioEspecial) {

		String reportNameJASPER = null;
		String logoPath = null;
		File logoFile = null;
		File subReportDir = null;
		File reportFile = null;
		String mailInventarioHTML = null;
		File mailInventarioFile = null;
		FileReader mailInventarioReader = null;
		BufferedReader reader = null;
		Map<String, Object> parameters = null;
		StringBuilder sb = null;
		String subject = null;
		JRExporter            exporter         = null;
		ByteArrayOutputStream output           = null;

		log.info(String.format("Iniciando envío de correo para la orden de salida %s, idPlanta %d", folio, idPlanta));

		try {
			reportNameJASPER = "/jasper/SolicitudSalidaMercanciaNew.jrxml";
			reportFile = new File(getClass().getResource(reportNameJASPER).getFile());
			log.info("Ruta jasper: " + reportFile.getPath());
			if(reportFile.exists() == false)
				log.error("El archivo no existe: " + reportFile.getPath());

			logoPath = "/images/logo.jpeg";
			logoFile = new File(getClass().getResource(logoPath).getFile());
			log.info("Ruta logo: " + logoFile.getPath());
			if(logoFile.exists() == false)
				log.error("El archivo no existe: " + logoFile.getPath());
			
			subReportDir = new File(getClass().getResource("/jasper/").getFile());
			
			parameters = new HashMap<String, Object>();
			parameters.put("REPORT_CONNECTION", conn);
			parameters.put("folioSalida", folio);
			parameters.put("idPlanta", idPlanta);
			parameters.put("esHorarioEspecial", isHorarioEspecial);
			parameters.put("logoPath", logoFile.getPath());
			parameters.put("subreportPath", subReportDir + File.separator);
			parameters.put("REPORT_LOCALE", new Locale("es", "MX"));
			parameters.put("REPORT_TIME_ZONE", TimeZone.getTimeZone("GMT-6"));

			subject = String.format("Orden de salida %s", folio);
			log.info("Preparando para ejecutar el reporte...");
			
			JasperDesign      objJasperDesign = JRXmlLoader.load(reportFile);
			JasperReport      objJasperReport = JasperCompileManager.compileReport(objJasperDesign);
			
			JasperPrint       objJasperPrint  = JasperFillManager.fillReport(objJasperReport, parameters);
			output = new ByteArrayOutputStream();
			
			exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, objJasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
			exporter.exportReport();
			
			byte[] binContenidoOrdenSalida = output.toByteArray();
			
			//byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conn);
			log.info("Reporte jasper ejecutado, preparando envío de correo...");
			mailInventarioHTML = "/mail/mailOrdenSalida.html";
			mailInventarioFile = new File(getClass().getResource(mailInventarioHTML).getFile());
			log.info("Ruta html mail: " + mailInventarioFile.getPath());
			if(mailInventarioFile.exists() == false)
				log.error("El archivo no existe: " + mailInventarioFile.getPath());
			
			mailInventarioReader = new FileReader(mailInventarioFile);
			reader = new BufferedReader(mailInventarioReader);
			sb = new StringBuilder();
			String linea = null;
			while ((linea = reader.readLine()) != null) {
				sb.append(linea);
			}
			
			String html = sb.toString();
			MailHelper mailUtil = new MailHelper();
			
			ContactosBL contactosBO = new ContactosBL(idCliente);
			ClienteContacto[] cteContactos = contactosBO.getClienteContactos(conn, this.idCliente);
			Correo buzon = null;
			log.info("Leyendo contactos del cliente...");
			for (ClienteContacto cteContacto : cteContactos) {
				log.info("Contacto: " + cteContacto.getContacto().getNombre() + " "
						+ cteContacto.getContacto().getApellido1());
				if (cteContacto.isHabilitado() == false)
					continue;

				MedioContacto[] medios = MedioContactoDAO.get(conn, cteContacto.getIdContacto());

				for (MedioContacto medio : medios) {
					if(medio.getIdMail() == null)
						continue;
					
					Mail mail = MailDAO.get(conn, medio.getIdMail());
					log.info("Mail: " + mail.getMail());
					
					buzon = new Correo(mail.getMail(),
							cteContacto.getContacto().getNombre() + " " + cteContacto.getContacto().getApellido1()
							+ " " + cteContacto.getContacto().getApellido2());
					mailUtil.addTo(buzon);
				}
			}

			Planta planta = PlantaDAO.getPlanta(conn, idPlanta);
			Usuario responsablePlanta = null;
			if (planta.getIdUsuario() != null) {
				responsablePlanta = UsuarioDAO.getByIdUsuario(conn, planta.getIdUsuario());
			}

			if (responsablePlanta != null) {
				buzon = new Correo(responsablePlanta.getMail(), responsablePlanta.getNombre() + " "
						+ responsablePlanta.getApellido1() + " " + responsablePlanta.getApellido2());
				mailUtil.addCC(buzon);
			}
			
			Adjunto attachment = new Adjunto("OrdenSalida" + folio + ".pdf", Adjunto.TP_ARCHIVO_PDF, binContenidoOrdenSalida);

			mailUtil.setMailBody(html);
			mailUtil.setSubject(subject);
			mailUtil.addAttachment(attachment);
			
			for(Adjunto archivo : this.alAdjuntos) {
				mailUtil.addAttachment(archivo);
			}
			
			mailUtil.sendJndiMailMessage(MailHelper.JNDI_MAIL_INVENTARIO);

		} catch (JRException ex) {
			log.error("Problema para generar la orden de salida...", ex);
		} catch (Exception ex) {
			log.error("Problema para enviar el correo electronico para la orden de salida " + folio, ex);
		}

	}
}
