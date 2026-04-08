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
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.ferbo.clientes.manager.FacturaDAO;
import com.ferbo.clientes.manager.StatusFacturaDAO;
import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.model.ClienteContacto;
import com.ferbo.clientes.model.Factura;
import com.ferbo.clientes.model.StatusFactura;
import com.ferbo.clientes.util.ClientesException;
import com.ferbo.clientes.util.Conexion;
import com.ferbo.clientes.util.JasperReportUtil;
import com.ferbo.facturama.business.CfdiBL;
import com.ferbo.facturama.response.FileViewModel;
import com.ferbo.facturama.tools.FacturamaException;


@Named(value = "repfacturacion")
@ViewScoped
public class RepFacturacion implements Serializable {

	private static final long serialVersionUID = 1L;
	private Date fecha;
	private Date fecha_ini;
	private Date fecha_fin;
	private Date maxDate;
	private Cliente clienteSelect;
	private List<Factura> listaFactura;
	private Factura facturaSelect;
	private ClienteContacto usuario;
	private Integer idCliente;
	private FacesContext context;
	private HttpServletRequest request;
	private HttpSession session;
	private StreamedContent file;
	private BigDecimal subtotal;
	private BigDecimal Total;
	private BigDecimal iva;
    private CfdiBL cfdiBL = null;
    private String statusNombre;
    
	private static Logger log = (Logger) LogManager.getLogger(RepFacturacion.class);
	@Inject
	private MbLogin login;


	public RepFacturacion() {
		

		fecha_ini = new Date();
		fecha_fin = new Date();
		listaFactura = new ArrayList<Factura>();
		facturaSelect = new Factura();
		cfdiBL = new CfdiBL();
		subtotal = new BigDecimal(0);
		iva = new BigDecimal(0);
		Total = new BigDecimal(0);
			
	}

	@PostConstruct
	public void init() {
       
		Connection conn = null;
		facturaSelect = new Factura();
		clienteSelect = new Cliente();
		try {
			context = FacesContext.getCurrentInstance();
			request = (HttpServletRequest) context.getExternalContext().getRequest();
			session = request.getSession(false);
			this.setUsuario((ClienteContacto) session.getAttribute("usuario"));
			conn = Conexion.getConnection();
			this.idCliente= (Integer) session.getAttribute("idCliente");
			clienteSelect = (Cliente) session.getAttribute("cliente");
			
			byte bytes[] = {};
			this.file = DefaultStreamedContent.builder()
					.contentType("application/pdf")
					.contentLength(bytes.length)
					.name("factura.pdf")
					.stream(() -> new ByteArrayInputStream(bytes) )
					.build();
			
		} catch (SQLException ex) {
			log.error("Problema para obtener la información de la facturación...", ex);
		} catch (Exception ex) {
			log.error("Problema general para obtener la información de la facturacíon...", ex);
		} finally {
			Conexion.close(conn);
		}
		Date today = new Date();
		maxDate = new Date(today.getTime());

	}


	public void ConsultaFactura() {
 		FacturaDAO facturaM = null;
		Connection conn = null;
    	Integer statusID;
		StatusFactura sf = new StatusFactura();
		StatusFacturaDAO sfm = new StatusFacturaDAO();
    	try {
			
			conn = Conexion.getConnection();
			facturaM = new FacturaDAO();
			
			if (login.getCliente().getIdCliente() == 0) {
				
				listaFactura = facturaM.buscarFacturas(conn,fecha_ini, fecha_fin);
				totales();
				
			} else {
				
				listaFactura = facturaM.buscarFacturasPorCliente(conn,fecha_ini, fecha_fin, clienteSelect.getIdCliente());
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Lista de facturas agregadas"));
				totales();
			}
			
			for(Factura f : listaFactura) {
				statusID = f.getIdStatus();
				sf = sfm.buscarPorIdStatus(conn, statusID);
				log.debug(statusID);
				log.debug(sf);
				f.setStatus(sf);

			}
				
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			Conexion.close(conn);
		}
	};
	
	

	
	public void generateReport() {
		Factura fc = facturaSelect;
		
		String reportNameJASPER = "/jasper/FacturaGestion.jrxml";
		File reportFile = new File(getClass().getResource(reportNameJASPER).getFile());
		log.info(reportFile.getPath());

		String sLogoPath = "/images/logo.jpeg";
		File logoFile = new File(getClass().getResource(sLogoPath).getFile());
		log.info("Imagen: " + logoFile.getPath());

		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		parameters = new HashMap<String, Object>();
		
		try {
			connection = Conexion.dsConexion();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("idFactura", fc.getId());
			parameters.put("imagen", logoFile.getPath());
			log.info("Parametros: " + parameters.toString());
			jasperReportUtil.createPdf(getNameFilePdf(), parameters, reportFile.getPath());
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
		String strDatePeriodoI = dateFormatPeriodo.format(getFecha_ini());
		String strDatePeriodoF = dateFormatPeriodo.format(getFecha_fin());
		return "factura_" + strDatePeriodoI + "_" + strDatePeriodoF + ".pdf";
	}
	
	
	public void totales() {
		BigDecimal sumaSubtotal;
		BigDecimal sumaIva;
		BigDecimal sumaTotal;
		
		sumaSubtotal = subTotalFacturas(listaFactura);
		subtotal = subtotal.add(sumaSubtotal);
		log.debug(subtotal);
		
		sumaIva = ivalFacturas(listaFactura);
		iva = iva.add(sumaIva);
		log.debug(iva);
		
		sumaTotal = TotalFacturas(listaFactura);
		Total = Total.add(sumaTotal);
		log.debug(Total);
	}

	public BigDecimal subTotalFacturas(List<Factura> lista) {
		
		BigDecimal subTotal = new BigDecimal(0);
		
		for(Factura sc: listaFactura) {
			subTotal = subTotal.add(sc.getSubtotal());
		}
		
		return subTotal;
	}
	
	public BigDecimal ivalFacturas(List<Factura> lista) {
		
		BigDecimal ivaSuma = new BigDecimal(0);
		
		for(Factura sc: listaFactura) {
			ivaSuma = ivaSuma.add(sc.getIva());
		}
		
		return ivaSuma;
	}
	
	public BigDecimal TotalFacturas(List<Factura> lista) {
		
		BigDecimal totalSuma = new BigDecimal(0);
		
		for(Factura sc: listaFactura) {
			totalSuma = totalSuma.add(sc.getTotal());
		}
		
		return totalSuma;
	}
	
	public void listadoFacturasPDF() {
		System.out.println("pruebas pruebas pruebas");
		
		String reportNameJASPER = "/jasper/listadoFacturas.jrxml";
		File reportFile = new File(getClass().getResource(reportNameJASPER).getFile());
		log.info(reportFile.getPath());

		String sLogoPath = "/images/logo.jpeg";
		File logoFile = new File(getClass().getResource(sLogoPath).getFile());
		log.info("Imagen: " + logoFile.getPath());

		JasperReportUtil jasperReportUtil = new JasperReportUtil();
		Map<String, Object> parameters = new HashMap<String, Object>();
		Connection connection = null;
		parameters = new HashMap<String, Object>();
		
		try {
			connection = Conexion.dsConexion();
			parameters.put("REPORT_CONNECTION", connection);
			parameters.put("image", logoFile.getPath());
			parameters.put("idCliente", clienteSelect.getIdCliente());
			parameters.put("fecha_ini", fecha_ini);
			parameters.put("fecha_fin", fecha_fin);
			log.info("Parametros: " + parameters.toString());
			jasperReportUtil.createPdf(getNameFilePdf(), parameters, reportFile.getPath());
		} catch (SQLException ex) {
			log.error("Problema en base de datos...", ex);
		} catch (Exception ex) {
			log.error("Problema general...", ex);
		} finally {
			Conexion.close(connection);
		}
	}

	public void cargaFactura(Factura f) {
		this.facturaSelect = f;
		log.debug("Factura {}", this.facturaSelect);
	}

	public void descargaCFDIPDF() {

		FacesContext context = null;
		HttpServletResponse response = null;
		ServletOutputStream output = null;
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		  String sContent = null;
	        byte[] content = null;
	        
	        String fileName = null;
	        
		try {
			if(this.facturaSelect.getUuid() == null)
				throw new ClientesException("La factura no está timbrada.");
			
			
			cfdiBL = new CfdiBL();
			FileViewModel filePDF = cfdiBL.getFile("pdf", "issuedLite", facturaSelect.getUuid());
			fileName = String.format("Factura-%s-%s.pdf", this.facturaSelect.getNomSerie(), this.facturaSelect.getNumero());
			sContent = filePDF.getContent();
        	content = Base64.getDecoder().decode(sContent);
        	
        	context = FacesContext.getCurrentInstance();
			response = (HttpServletResponse) context.getExternalContext().getResponse();
			output = response.getOutputStream();
			output.flush();
			String disposition = String.format("attachment; filename=\"%s\"", fileName);
			response.setHeader("Content-Disposition", disposition);
			response.addHeader("Content-Disposition", disposition);
			response.setContentType("application/pdf");
        	
			
			output.write(content);
			output.flush();
			output.close();
        	
		} catch (FacturamaException e) {
			severity = FacesMessage.SEVERITY_ERROR;
			mensaje = "Se presentó un problema en la comunicación con Facturama...";
			
		} catch (ClientesException e) {
			severity = FacesMessage.SEVERITY_ERROR;
			mensaje = "La factura aún no ha sido timbrada";

		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			severity = FacesMessage.SEVERITY_INFO;
				message = new FacesMessage(severity, "Error en impresion", mensaje);
				FacesContext.getCurrentInstance().addMessage(null, message);
				PrimeFaces.current().ajax().update("form:messages");
		
		}
	}
	
	
	public void descargaCFDIXML() {
		FacesContext context = null;
		HttpServletResponse response = null;
		ServletOutputStream output = null;
		Severity severity = null;
		String mensaje = null;
		  String sContent = null;
	        byte[] content = null;
	    	FacesMessage message = null;
	        String fileName = null;
	        
		try {
			if(this.facturaSelect.getUuid() == null)
				throw new ClientesException("La factura no está timbrada.");
			
			cfdiBL = new CfdiBL();
			FileViewModel fileXML = cfdiBL.getFile("xml", "issuedLite", facturaSelect.getUuid());
			fileName = String.format("Factura-%s-%s.pdf", this.facturaSelect.getNomSerie(), this.facturaSelect.getNumero());
			sContent = fileXML.getContent();
        	content = Base64.getDecoder().decode(sContent);
        	
        	context = FacesContext.getCurrentInstance();
			response = (HttpServletResponse) context.getExternalContext().getResponse();
			output = response.getOutputStream();
			output.flush();
			String disposition = String.format("attachment; filename=\"%s\"", fileName);
			response.setHeader("Content-Disposition", disposition);
			response.addHeader("Content-Disposition", disposition);
			response.setContentType("application/pdf");
        	
			output.write(content);
			output.flush();
        	
        	
		} catch (FacturamaException e) {
			e.printStackTrace();
		} catch (ClientesException e) {
			severity = FacesMessage.SEVERITY_ERROR;
			mensaje = "La factura aún no ha sido timbrada";
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			severity = FacesMessage.SEVERITY_INFO;
			message = new FacesMessage(severity, "Error en impresion", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
		}
	}
	

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getFecha_ini() {
		return fecha_ini;
	}

	public void setFecha_ini(Date fecha_ini) {
		this.fecha_ini = fecha_ini;
	}

	public Date getFecha_fin() {
		return fecha_fin;
	}

	public void setFecha_fin(Date fecha_fin) {
		this.fecha_fin = fecha_fin;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}

	public MbLogin getLogin() {
		return login;
	}

	public void setLogin(MbLogin login) {
		this.login = login;
	}

	public List<Factura> getListaFactura() {
		return listaFactura;
	}

	public void setListaFactura(List<Factura> listaFactura) {
		this.listaFactura = listaFactura;
	}

	public Factura getFacturaSelect() {
		return facturaSelect;
	}

	public void setFacturaSelect(Factura facturaSelect) {
		this.facturaSelect = facturaSelect;
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

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getTotal() {
		return Total;
	}

	public void setTotal(BigDecimal total) {
		Total = total;
	}

	public BigDecimal getIva() {
		return iva;
	}

	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}

	public CfdiBL getCfdiBL() {
		return cfdiBL;
	}

	public void setCfdiBL(CfdiBL cfdiBL) {
		this.cfdiBL = cfdiBL;
	}

	public String getStatusNombre() {
		return statusNombre;
	}

	public void setStatusNombre(String statusNombre) {
		this.statusNombre = statusNombre;
	}



}
