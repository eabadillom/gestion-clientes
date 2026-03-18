package com.ferbo.clientes.beans;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.ferbo.clientes.manager.ClienteDAO;
import com.ferbo.clientes.manager.EstadoCuentaDAO;
import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.model.ClienteContacto;
import com.ferbo.clientes.model.EstadoCuenta;
import com.ferbo.clientes.util.ClientesException;
import com.ferbo.clientes.util.Conexion;
import com.ferbo.clientes.util.DateUtils;
import com.ferbo.clientes.util.JasperReportUtil;

@Named(value = "mbEstadoCuenta")
@ViewScoped
public class MBEstadoCuenta implements Serializable {

	private static Logger log = LogManager.getLogger(MBEstadoCuenta.class);

	private static final long serialVersionUID = 1L;
	private EstadoCuentaDAO estadoCuentaManager;
	
	private Date fechaInicio;
	private Date fechaFin;
	private Date maxDate;
	private Date mesActual;
	private EstadoCuenta estadoCuenta;
	private Cliente clienteSelect;
	private ClienteDAO clienteM;
	private ClienteContacto usuario;
	private Integer idCliente;
	private FacesContext context;
	private HttpServletRequest request;
	private HttpSession session;
	private List<EstadoCuenta> listaestadoCuenta;
	private StreamedContent file;
	private BigDecimal saldoInicial;
	private Cliente cliente;


	public MBEstadoCuenta() {
		estadoCuentaManager= new EstadoCuentaDAO();
		setListaestadoCuenta(new ArrayList<EstadoCuenta>());
		saldoInicial = new BigDecimal(0);
	}
	
	@PostConstruct
	public void init() {
		Connection conn = null;
		this.mesActual = new Date();
		clienteM = new ClienteDAO();
		fechaInicio = new Date(Calendar.MONTH);
		fechaFin = new Date(Calendar.MONTH);
		Date today = new Date();
		long oneDay = 24 * 60 * 60 * 1000;
		clienteM = new ClienteDAO();
		clienteSelect = new Cliente();
		maxDate = new Date(today.getTime());
		
		try {
			context = FacesContext.getCurrentInstance();
			request = (HttpServletRequest) context.getExternalContext().getRequest();
			session = request.getSession(false);
			this.setUsuario((ClienteContacto) session.getAttribute("usuario"));
			conn = Conexion.getConnection();
			this.idCliente= (Integer) session.getAttribute("idCliente");
			this.cliente = (Cliente) session.getAttribute("cliente");
			clienteSelect = clienteM.get(conn, cliente.getIdCliente());
			
			byte bytes[] = {};
			this.file = DefaultStreamedContent.builder()
					.contentType("application/pdf")
					.contentLength(bytes.length)
					.name("estadoCuenta.pdf")
					.stream(() -> new ByteArrayInputStream(bytes) )
					.build();
		
		} catch (SQLException e) {
			log.info("Problema en base de datos...", e);
		}finally {
			Conexion.close(conn);
		}
	}
	
	public void establecerPeriodo() {
		this.fechaInicio = DateUtils.getFirstDayOfMonth(mesActual);
		this.fechaFin = DateUtils.getLastDayOfMonth(mesActual);
		log.info("Periodo: {} - {}", this.fechaInicio, this.fechaFin);
	}
	
	public void Consultar() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		Date finMes;
		Connection conn = null;
                mesActual = DateUtils.getFirstDayOfMonth(mesActual);
		finMes = DateUtils.getLastDayOfMonth(mesActual);
		try {
			conn = Conexion.getConnection();
			
			if(clienteSelect == null)
				throw new ClientesException("No hay un cliente asignado.");
			
			log.info("Inicio mes: {}", this.mesActual);
			log.info("Fin mes: {}", finMes);
			
			listaestadoCuenta = estadoCuentaManager.listaEstadoCuenta(conn,mesActual, clienteSelect.getIdCliente(), finMes);
			
			if(listaestadoCuenta == null)
				throw new ClientesException("No es posible recuperar el saldo inicial.");
			
			if(listaestadoCuenta.size() > 0) {
				saldoInicial = listaestadoCuenta.get(0).getSaldoInicial();
			}

		} catch (ClientesException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (Exception e) {
			mensaje = e.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
                        Conexion.close(conn);
			PrimeFaces.current().ajax().update("form:messages");
		}
	}

	public void exportarPDF() {
		String reportNameJASPER = "/jasper/EstadoCuentaCliente.jrxml";
		File reportFile = new File(getClass().getResource(reportNameJASPER).getFile());
		Date finMes;
		log.info(reportFile.getPath());

		String sLogoPath = "/images/logo.png";
		File logoFile = new File(getClass().getResource(sLogoPath).getFile());
		log.info("Imagen: " + logoFile.getPath());
		finMes = DateUtils.getLastDayOfMonth(mesActual);
		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		parameters = new HashMap<String, Object>();

		try {
			connection = Conexion.dsConexion();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("idCliente", clienteSelect.getIdCliente());
			parameters.put("fechaInicio", mesActual);
			parameters.put("fechaFin", finMes);
			parameters.put("imagen", logoFile.getPath());
			log.info("Parametros: " + parameters.toString());
			this.file = jasperReportUtil.getPdf(getNameFilePdf(), parameters, reportFile.getAbsolutePath());
		} catch (SQLException ex) {
			log.error("Problema en base de datos...", ex);
		} catch (Exception ex) {
			log.error("Problema general...", ex);
		} finally {
			Conexion.close(connection);
		}
	}

	public String getNameFilePdf() {
		DateFormat dateFormatPeriodo = new SimpleDateFormat("yyyy-MM-dd");
		String strDatePeriodoI = dateFormatPeriodo.format(getFechaInicio());
		String strDatePeriodoF = dateFormatPeriodo.format(getFechaFin());
		return "factura_" + strDatePeriodoI + "_" + strDatePeriodoF + ".pdf";
	}

	public void exportarExcel() {

		log.debug("...");
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

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public Date getMesActual() {
		return mesActual;
	}

	public void setMesActual(Date mesActual) {
		this.mesActual = mesActual;
	}

	public EstadoCuenta getEstadoCuenta() {
		return estadoCuenta;
	}

	public void setEstadoCuenta(EstadoCuenta estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}

	public List<EstadoCuenta> getListaestadoCuenta() {
		return listaestadoCuenta;
	}

	public void setListaestadoCuenta(List<EstadoCuenta> listaestadoCuenta) {
		this.listaestadoCuenta = listaestadoCuenta;
	}
	public Cliente getClienteSelect() {
		return clienteSelect;
	}
	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}
	public ClienteContacto getUsuario() {
		return usuario;
	}
	public void setUsuario(ClienteContacto usuario) {
		this.usuario = usuario;
	}
	public Integer getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public BigDecimal getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(BigDecimal saldoInicial) {
		this.saldoInicial = saldoInicial;
	}
	
	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}
}
