package com.ferbo.clientes.beans;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.file.UploadedFile;

import com.ferbo.clientes.dao.SerieOrdenDAO;
import com.ferbo.clientes.mail.beans.Adjunto;
import com.ferbo.clientes.mail.beans.Planta;
import com.ferbo.clientes.mail.mngr.PlantaDAO;
import com.ferbo.clientes.mail.report.SendMailOrdenSalida;
import com.ferbo.clientes.manager.EmisionSalidasDAO;
import com.ferbo.clientes.manager.PreSalidaDAO;
import com.ferbo.clientes.manager.SerieConstanciaDAO;
import com.ferbo.clientes.manager.ServiciosExtrasDAO;
import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.model.ClienteContacto;
import com.ferbo.clientes.model.Inventario;
import com.ferbo.clientes.model.PreSalida;
import com.ferbo.clientes.model.PreSalidaObservaciones;
import com.ferbo.clientes.model.SerieConstancia;
import com.ferbo.clientes.model.SerieOrden;
import com.ferbo.clientes.model.ServiciosExtras;
import com.ferbo.clientes.util.ClientesException;
import com.ferbo.clientes.util.Conexion;
import com.ferbo.clientes.util.DateUtils;

@Named(value = "mbEmisionSalidas")
@ViewScoped
public class MbEmisionSalidas implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(MbEmisionSalidas.class);
	private EmisionSalidasDAO emisionSalidasDAO;
	private ServiciosExtras serviciosExtras;
	private List<Inventario> listaInventario;
	private List<Inventario> listaInventarioSelect;
	private SerieConstancia serieConstancia;
	private List<PreSalida> listaPresalida;
	private List<ServiciosExtras> listaServicios;
	private List<ServiciosExtras> listaServiciosSelect;
	private List<Planta> listaPlantas;
	private PreSalida preSalida;
	private Date fecha;
	private String placas;
	private String nombreOperador;
	private Date fechaMin;
	private Integer idPlanta;
	private String observaciones;
	private UploadedFile attachmentFile;
	private List<Adjunto> archivosList;
	private Adjunto selectedAttachment;
	private BigDecimal tamanioTotal = null;
	private BigDecimal limite = new BigDecimal("10485760").setScale(2, BigDecimal.ROUND_HALF_UP);
	private BigDecimal megabyte = new BigDecimal("1048576").setScale(2, BigDecimal.ROUND_HALF_UP);
	private SerieOrden serie;
	private SerieOrdenDAO serieDAO = null;
	
	private boolean isOrdenRegistrada = false;
	
	private ClienteContacto cteContacto;
	private FacesContext context;
	private HttpServletRequest request;
	private HttpSession session;
	private Cliente cliente;
	
	private String folioSalida;
	
	public MbEmisionSalidas() {
		this.serieDAO = new SerieOrdenDAO();
	}
	
	@PostConstruct
	public void init() {
		Connection conn = null;
		
		try {
			this.context = FacesContext.getCurrentInstance();
			this.request = (HttpServletRequest) context.getExternalContext().getRequest();
			this.session = request.getSession(false);
			this.cliente = (Cliente) session.getAttribute("cliente");
			
			conn = Conexion.dsConexion();
			
			listaInventarioSelect = new ArrayList<Inventario>();
			listaServiciosSelect = new ArrayList<ServiciosExtras>();
			emisionSalidasDAO = new EmisionSalidasDAO();
			
			ServiciosExtrasDAO serviciosDAO = new ServiciosExtrasDAO();
			listaServicios = serviciosDAO.getServiciosExtras(this.cliente);
			
			listaPlantas = Arrays.asList(PlantaDAO.getPlantas(conn));
			
			fecha = new Date();
			DateUtils.setMinuto(fecha, 0);
			DateUtils.setSegundo(fecha, 0);
			DateUtils.setMilisegundo(fecha, 0);
			
			log.info("Fecha / Hora por defecto: {}", this.fecha);
			
			placas = null;
			nombreOperador = null;
			fechaMin = new Date();
			cteContacto = (ClienteContacto) request.getSession(false).getAttribute("usuario");
			
			
			
			this.archivosList = new ArrayList<Adjunto>();
			this.tamanioTotal = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
			
			String mensaje = String.format("Usuario %s ingresa a Registro de orden de salida.", cteContacto.toString());
			log.info(mensaje);
		} catch(Exception ex) {
			log.error("Problema para obtener información de la base de datos...", ex);
		} finally {
			Conexion.close(conn);
		}
	}

	public void getFolio() {
		Connection conn = null;
		Planta planta = null;
		String codigo = null;
		try {
			conn = Conexion.getConnection();
			planta = PlantaDAO.getPlanta(conn, this.idPlanta);
			this.serie = serieDAO.get(conn, cliente.getIdCliente(), this.idPlanta, "O");
			if(this.serie == null) {
				this.serie = new SerieOrden();
				this.serie.setIdCliente(this.cliente.getIdCliente());
				this.serie.setTipoSerie("O");
				this.serie.setIdPlanta(this.idPlanta);
				this.serie.setNumero(1);
				this.serieDAO.insert(conn, this.serie);
			}
			
			codigo = String.format("%s%s%s%d", "RO", planta.getSufijo(), this.cliente.getCodigoUnico(), this.serie.getNumero());
			conn.commit();
			this.folioSalida = codigo;
			log.info("Folio solicitud de salida: {}", this.folioSalida);
		} catch(Exception ex) {
			log.error("Problema para generar el folio...", ex);
			Conexion.rollback(conn);
		} finally {
			Conexion.close(conn);
		}
	}
	
	public void resultadoPeso(Inventario inventario) {
		log.debug("metodo resultadoPeso");
		FacesMessage message = null;
		Severity severity = null;
		
		BigDecimal tmp = null;
		BigDecimal peso = null;
		BigDecimal existencia = null;
		BigDecimal cantidad = null;
		existencia = new BigDecimal(inventario.getExistencia());
		peso = inventario.getPeso();
		if(existencia.compareTo(BigDecimal.ZERO) == 0) {
			severity = FacesMessage.SEVERITY_ERROR;
			message = new FacesMessage(severity, "Cantidad incorrecta", "Cantidad incorrecta.");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update("form:messages");
	        inventario.setCantidad(null);
	        return;
		}
		
		if(inventario.getCantidad() == null) {
			return;
		}
			
		cantidad = new BigDecimal(inventario.getCantidad());
		
		tmp = peso.divide(existencia, 3, RoundingMode.HALF_UP);
		tmp = tmp.multiply(cantidad);
		
		if(cantidad.compareTo(existencia) > 0) {
			severity = FacesMessage.SEVERITY_ERROR;
			message = new FacesMessage(severity, "Cantidad incorrecta", "Ha indicado una cantidad mayor a la que tiene actualmente para su producto.");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update("form:messages");
	        inventario.setCantidad(null);
	        return;
		}
		
		if(cantidad.compareTo(BigDecimal.ZERO) < 0) {
			severity = FacesMessage.SEVERITY_ERROR;
			message = new FacesMessage(severity, "Cantidad incorrecta", "Ha indicado una cantidad no valida.");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update("form:messages");
	        inventario.setCantidad(null);
	        return;
		}
		inventario.setPesoAprox(tmp);
	}
	
	public void onRowSelect(SelectEvent<Inventario> event) {
		log.debug("metodo onRowSelect");
	    if (event != null && event.getObject() != null && event.getObject() instanceof Inventario) {
	    	Inventario inventario = (Inventario)event.getObject();
	    	inventario.setHabilitado(true);
	    }
	}

	public void onRowUnselect(UnselectEvent<Inventario> event) {
		log.debug("metodo onRowUnselect");
		if (event != null && event.getObject() != null && event.getObject() instanceof Inventario) {
			Inventario inventario = (Inventario) event.getObject();
			inventario.setHabilitado(false);
			inventario.setCantidad(null);
			inventario.setPesoAprox(null);
		}
	}
	
	public void toggleSelect() {
		log.debug("metodo toogleSelect");
		if (listaInventario.stream().filter(inv -> inv.isHabilitado()).count() > 0
				&& listaInventario.size() == listaInventarioSelect.size()) {
			listaInventario.stream().forEach(inv -> {
				inv.setHabilitado(true);
			});
		} else {
			listaInventario.stream().forEach(inv -> {
				inv.setHabilitado(false);
				inv.setCantidad(new Integer(0));
				inv.setPesoAprox(BigDecimal.ZERO);
			});
		}
	}
	
	public void onRowSelectServicios(SelectEvent<ServiciosExtras> event) {
		log.debug("metodo onRowSelectServicios");
	    if (event != null && event.getObject() != null && event.getObject() instanceof ServiciosExtras) {
	    	ServiciosExtras servicio = (ServiciosExtras)event.getObject();
	    	servicio.setHabilitado(true);
	    }
	}

	public void onRowUnselectServicios(UnselectEvent<ServiciosExtras> event) {
		log.debug("metodo onRowUnselectServicios");
	    if (event != null && event.getObject() != null && event.getObject() instanceof ServiciosExtras) {
	    	ServiciosExtras servicio = (ServiciosExtras)event.getObject();
	    	servicio.setHabilitado(false);
	    	servicio.setCantidad(0);
	    }
	}
	
	public void toggleSelectServicios() {
		log.debug("metodo toogleSelectServicios");
		if (listaServicios.stream().filter(servicio -> servicio.isHabilitado()).count() > 0
				&& listaServicios.size() == listaServiciosSelect.size()) {
			listaServicios.stream().forEach(servicio -> {
				servicio.setHabilitado(true);
			});
		} else {
			listaServicios.stream().forEach(servicio -> {
				servicio.setHabilitado(false);
				servicio.setCantidad(0);
			});
		}
	}
	
	public void onChangeFecha() {
		FacesMessage message = null;
		Severity severity = null;
		
		log.info("Fecha / hora solicitado: {}", this.fecha);
		
		Date fechaMin = new Date(this.fecha.getTime());
		DateUtils.setTime(fechaMin, 7, 0, 0, 0);
		
		Date fechaMax = new Date(this.fecha.getTime());
		DateUtils.setTime(fechaMax, 17, 0, 0, 0);
		
		if(this.fecha.getTime() < fechaMin.getTime() || this.fecha.getTime() > fechaMax.getTime()) {
			severity = FacesMessage.SEVERITY_WARN;
			message = new FacesMessage(severity, "Horario no laboral", "Ha seleccionado un horario no laboral, por lo que se realizará el cargo de servicios extras. El horario laboral es de 7:00am a 5:00pm.");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update("form:messages");
		}
	}
	
	public void showSaving() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		mensaje = String.format("Procesando su orden de salida, por favor espere...", this.folioSalida);
		severity = FacesMessage.SEVERITY_WARN;
		message = new FacesMessage(severity, "Emisión de salida", mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
        PrimeFaces.current().ajax().update(":form:messages");
	}

	public void guardarPresalida() {
		Connection conn = null;
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		listaPresalida = new ArrayList<PreSalida>();
		listaServicios = new ArrayList<ServiciosExtras>();
		String tmpFolio = null;
		for (Inventario inventarioSelect : listaInventarioSelect) {
			
			Date horaSalida = DateUtils.getDate("00/00/0001", DateUtils.FORMATO_DD_MM_YYYY);
			int hora = DateUtils.getHora(this.fecha);
			int minuto = DateUtils.getMinuto(this.fecha);
			DateUtils.setTime(horaSalida, hora, minuto, 0, 0);
			
			preSalida = new PreSalida();
			preSalida.setCd_folio_salida(this.folioSalida);
			preSalida.setSt_estado("A");
			preSalida.setFh_salida(fecha);
			preSalida.setTm_salida(horaSalida);
			preSalida.setNb_placa_tte(placas);
			preSalida.setNb_operador_tte(nombreOperador);
			preSalida.setPartida_cve(inventarioSelect.getPartidaClave());
			preSalida.setFolio(inventarioSelect.getFolio());
			preSalida.setNu_cantidad(inventarioSelect.getCantidad());
			preSalida.setIdContacto(cteContacto.getIdContacto());
			listaPresalida.add(preSalida);
			if (tmpFolio == null) {
				tmpFolio = preSalida.getCd_folio_salida();
			}
		}
		
		for (ServiciosExtras serviciosSelect : listaServiciosSelect) {
			serviciosExtras = new ServiciosExtras();
			serviciosExtras.setIdServicioExtra(serviciosSelect.getIdServicioExtra());
			serviciosExtras.setServicioExtra(serviciosSelect.getServicioExtra());
			serviciosExtras.setCantidad(serviciosSelect.getCantidad());
			serviciosExtras.setObservacion(serviciosSelect.getObservacion());
			serviciosExtras.setIdUnidadManejo(serviciosSelect.getIdUnidadManejo());
			listaServicios.add(serviciosExtras);
		}
		
		boolean empty = listaPresalida.stream().filter(preSalida -> preSalida.getFh_salida().equals(null) || 
				                                                    preSalida.getTm_salida().equals(null) || 
				                                                    preSalida.getNb_operador_tte().equals(null) || 
				                                                    preSalida.getNb_placa_tte().equals(null)).count() > 0;
	    boolean empty2 = listaInventarioSelect.stream().filter(inventario -> inventario.getCantidad() == 0).count() > 0;		                                                    
		PreSalidaDAO preSalidaManager = new PreSalidaDAO();
		ServiciosExtrasDAO serviciosManager = new ServiciosExtrasDAO();
		
		try {
			if(isOrdenRegistrada) {
				throw new ClientesException("La constancia ya se encuentra registrada.");
			}
			
			conn = Conexion.getConnection();
			if (preSalida == null) {
				throw new ClientesException("Favor de llenar todos los campos:\n\n\t * Horas\n\n\t * Minutos\n\n\t * Placas de Unidad\n\n\t * Nombre del Operador\n\n\t * Productos del Inventario");
			}
			
			if(empty) {
				throw new ClientesException("Favor de llenar todos los campos:\n\n\t * Horas\n\n\t * Minutos\n\n\t * Placas de Unidad\n\n\t * Nombre del Operador\n\n\t * Productos del Inventario");
			}
			
			if(empty2) {
				throw new ClientesException("El número de unidades de su producto es incorrecto.");
			}
			
			if (listaPresalida.isEmpty()) {
				throw new ClientesException("Favor de colocar productos del inventario.");
			}
			
			preSalidaManager.guardar(conn, listaPresalida);
			
			PreSalidaObservaciones observaciones = new PreSalidaObservaciones(this.folioSalida, this.observaciones);
			serviciosManager.insert(conn, observaciones);
			
			for(ServiciosExtras servicio : listaServicios) {
				servicio.setFolioSalida(this.folioSalida);
				serviciosManager.insert(conn, servicio);
			}
			
			serie.setNumero(serie.getNumero() + 1);
			serieDAO.update(conn, this.serie);
			
			conn.commit();
			
			isOrdenRegistrada = true;
			
			SendMailOrdenSalida sendMail = new SendMailOrdenSalida();
			sendMail.setFolio(tmpFolio);
			sendMail.add(this.archivosList);
			sendMail.start();
			
			mensaje = String.format("Su orden de salida %s se generó correctamente. Recibirá copia de su orden vía correo electrónico.", this.folioSalida);
			severity = FacesMessage.SEVERITY_INFO;
			
		} catch(ClientesException ex) {
			Conexion.rollback(conn);
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema con la emisión de salidas...", ex);
			Conexion.rollback(conn);
			mensaje = "Su solicitud no se pudo generar.\nFavor de comunicarse con el administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			Conexion.close(conn);
			message = new FacesMessage(severity, "Emisión de salida", mensaje);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update(":form:messages");
		}
	}

	public void dialogo(int bandera) {
		FacesMessage message = null;
		if (bandera == 0) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Emision de Salida",
					"Se genero correctamente.\n\n Se le notificara vía email.");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Emision de Salida",
					"Su solicitud no se pudo generar correctamente.\n Favor de comunicarse con el administrador.");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
	}
	
	public void validador(PreSalida preSalida) {
		FacesMessage message = null;
		if (preSalida.getTm_salida() == null || 
				preSalida.getFh_salida() == null || 
				(preSalida.getNb_placa_tte() == null || preSalida.getNb_placa_tte() == "") || 
				(preSalida.getNb_operador_tte() == null || preSalida.getNb_operador_tte() == "") || listaInventario.size() == 0) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Emision de Salida",
					"Favor de llenar todos los campos:\n\n\t * Horas\n\n\t * Minutos\n\n\t * Placas de Unidad\n\n\t * Nombre del Operador\n\n\t * Productos del Inventario");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
	}
	
	public void dialogoHorarioExtra(ActionEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Horario Extra", "Este horario generará un costo extra.");
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}
	
	public void muestraInventario() {
		this.getFolio();
		if(this.idPlanta != null)
			listaInventario = emisionSalidasDAO.getInventario(this.cliente, this.idPlanta);
		else
			listaInventario = new ArrayList<Inventario>();
	}
	
	public void cargarArchivo() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		Adjunto archivo = null;
		BigDecimal tamanio = null;
		
		try {
			this.tamanioTotal = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
			tamanio = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
			
			if(this.attachmentFile == null)
				throw new ClientesException("Debe seleccionar un archivo");
			
			if(this.attachmentFile.getSize() == 0)
				throw new ClientesException("El archivo no debe estar vacío.");
			
			for(Adjunto a : archivosList) {
				tamanio = tamanio.add(new BigDecimal(a.getTamanio()));
			}
			tamanioTotal = tamanioTotal.add(tamanio);
			
			archivo = new Adjunto(attachmentFile.getFileName(), Adjunto.TP_ARCHIVO_GENERICO, attachmentFile.getContent());
			tamanio = tamanio.add(new BigDecimal(archivo.getTamanio()));
			
			if(tamanio.compareTo(limite) > 0) //El tamaño debe ser menor o igual a 10 MB.
				throw new ClientesException("El tamaño de todos los archivos no debe superar los 10 MB.");
			tamanioTotal = tamanio;
			
			log.info("Tamaño total de archivos adjuntos (MB): " + tamanioTotal);
			this.archivosList.add(archivo);
			this.attachmentFile = null;
			
			mensaje = "El archivo se cargó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(ClientesException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema con la emisión de salidas...", ex);
			mensaje = "Su solicitud no se pudo generar.\nFavor de comunicarse con el administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			tamanioTotal = tamanioTotal.divide(megabyte, BigDecimal.ROUND_HALF_UP);
			
			message = new FacesMessage(severity, "Emisión de salida", mensaje);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update(":form:messages");
		}
	}
	
	public void eliminarAdjunto() {
		BigDecimal tamanio = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		this.archivosList.remove(this.selectedAttachment);
		for(Adjunto a : archivosList) {
			tamanio = tamanio.add(new BigDecimal(a.getTamanio()));
		}
		this.tamanioTotal = tamanio.divide(megabyte, BigDecimal.ROUND_HALF_UP);;
	}
	

	public void reload() throws IOException {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}
	
	public List<Inventario> getListaInventario() {
		return listaInventario;
	}

	public void setListaInventario(List<Inventario> listaInventario) {
		this.listaInventario = listaInventario;
	}

	public List<Inventario> getListaInventarioSelect() {
		return listaInventarioSelect;
	}

	public void setListaInventarioSelect(List<Inventario> listaInventarioSelect) {
		this.listaInventarioSelect = listaInventarioSelect;
	}

	public List<ServiciosExtras> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios(List<ServiciosExtras> listaServicios) {
		this.listaServicios = listaServicios;
	}
	
	public List<ServiciosExtras> getListaServiciosSelect() {
		return listaServiciosSelect;
	}

	public void setListaServiciosSelect(List<ServiciosExtras> listaServiciosSelect) {
		this.listaServiciosSelect = listaServiciosSelect;
	}

	public SerieConstancia getSerieConstancia() {
		return serieConstancia;
	}

	public void setSerieConstancia(SerieConstancia serieConstancia) {
		this.serieConstancia = serieConstancia;
	}

	public List<PreSalida> getListaPresalida() {
		return listaPresalida;
	}

	public void setListaPresalida(List<PreSalida> listaPresalida) {
		this.listaPresalida = listaPresalida;
	}

	public PreSalida getPreSalida() {
		return preSalida;
	}

	public void setPreSalida(PreSalida preSalida) {
		this.preSalida = preSalida;
	}

	public String getPlacas() {
		return placas;
	}

	public void setPlacas(String placas) {
		this.placas = placas;
	}

	public String getNombreOperador() {
		return nombreOperador;
	}

	public void setNombreOperador(String nombreOperador) {
		this.nombreOperador = nombreOperador;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getFechaMin() {
		return fechaMin;
	}

	public void setFechaMin(Date fechaMin) {
		this.fechaMin = fechaMin;
	}

	public List<Planta> getListaPlantas() {
		return listaPlantas;
	}

	public void setListaPlantas(List<Planta> listaPlantas) {
		this.listaPlantas = listaPlantas;
	}

	public Integer getIdPlanta() {
		return idPlanta;
	}

	public void setIdPlanta(Integer idPlanta) {
		this.idPlanta = idPlanta;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public UploadedFile getAttachmentFile() {
		return attachmentFile;
	}

	public void setAttachmentFile(UploadedFile attachmentFile) {
		this.attachmentFile = attachmentFile;
	}

	public List<Adjunto> getArchivosList() {
		return archivosList;
	}

	public void setArchivosList(List<Adjunto> archivosList) {
		this.archivosList = archivosList;
	}

	public Adjunto getSelectedAttachment() {
		return selectedAttachment;
	}

	public void setSelectedAttachment(Adjunto selectedAttachment) {
		this.selectedAttachment = selectedAttachment;
	}

	public BigDecimal getTamanioTotal() {
		return tamanioTotal;
	}

	public void setTamanioTotal(BigDecimal tamanioTotal) {
		this.tamanioTotal = tamanioTotal;
	}

	public String getFolioSalida() {
		return folioSalida;
	}

	public void setFolioSalida(String folioSalida) {
		this.folioSalida = folioSalida;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}
