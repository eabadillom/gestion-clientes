package com.ferbo.clientes.beans;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
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

import com.ferbo.clientes.business.InventarioBL;
import com.ferbo.clientes.business.PlantaBL;
import com.ferbo.clientes.business.SalidasBL;
import com.ferbo.clientes.business.SerieOrdenBL;
import com.ferbo.clientes.business.ServiciosExtrasBL;
import com.ferbo.clientes.mail.beans.Adjunto;
import com.ferbo.clientes.mail.beans.Planta;
import com.ferbo.clientes.mail.report.SendMailOrdenSalida;
import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.model.ClienteContacto;
import com.ferbo.clientes.model.Inventario;
import com.ferbo.clientes.model.Salida;
import com.ferbo.clientes.model.SalidaDetalle;
import com.ferbo.clientes.model.SerieConstancia;
import com.ferbo.clientes.model.SerieOrden;
import com.ferbo.clientes.model.ServicioSalida;
import com.ferbo.clientes.model.ServiciosExtras;
import com.ferbo.clientes.util.ClientesException;
import com.ferbo.clientes.util.Conexion;
import com.ferbo.clientes.util.DateUtils;
import com.ferbo.clientes.util.FacesUtils;

@Named(value = "mbEmisionSalidas")
@ViewScoped
public class MbEmisionSalidas implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(MbEmisionSalidas.class);
	
	private List<Inventario> listaInventario;
	private List<Inventario> listaInventarioSelect;
	private SerieConstancia serieConstancia;
	private Salida salida;
	private List<SalidaDetalle> listaSalidaDetalle;
	private List<ServicioSalida> listaServicioSalida;
	private List<ServiciosExtras> listaServicios;
	private List<ServiciosExtras> listaServiciosSelect;
	private Planta plantaSelected;
	private List<Planta> listaPlantas;
	private Date fecha;
	private Date fechaMin;
	private UploadedFile attachmentFile;
	private List<Adjunto> archivosList;
	private Adjunto selectedAttachment;
	private BigDecimal tamanioTotal = null;
	private BigDecimal limite = new BigDecimal("10485760").setScale(2, BigDecimal.ROUND_HALF_UP);
	private BigDecimal megabyte = new BigDecimal("1048576").setScale(2, BigDecimal.ROUND_HALF_UP);
	private SerieOrden serie;
	
	private boolean isOrdenRegistrada = false;
	
	private ClienteContacto cteContacto;
	private FacesContext context;
	private HttpServletRequest request;
	private HttpSession session;
	private Cliente cliente;
	
	private String folioSalida;
	
	public MbEmisionSalidas() {
            this.listaInventarioSelect = new ArrayList<Inventario>();
            this.listaServiciosSelect = new ArrayList<ServiciosExtras>();
            
            this.fecha = new Date();
            DateUtils.setMinuto(this.fecha, 0);
            DateUtils.setSegundo(this.fecha, 0);
            DateUtils.setMilisegundo(this.fecha, 0);
            log.info("Fecha / Hora por defecto: {}", this.fecha);
            this.fechaMin = new Date();
            
            this.archivosList = new ArrayList<Adjunto>();
            this.tamanioTotal = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
            
            this.context = FacesContext.getCurrentInstance();
            this.request = (HttpServletRequest) this.context.getExternalContext().getRequest();
            this.session = this.request.getSession(false);
            this.cliente = (Cliente) this.session.getAttribute("cliente");
            this.cteContacto = (ClienteContacto) this.request.getSession(false).getAttribute("usuario");
            
            String mensaje = String.format("Usuario %s ingresa a Registro de orden de salida.", this.cteContacto.getUsuario());
            log.info(mensaje);
	}
	
	@PostConstruct
	public void init() {
		Connection conn = null;
		this.crearSalida();
		
		try {
			conn = Conexion.dsConexion();
			
			this.listaServicios = ServiciosExtrasBL.obtenerSrvExtras(conn, cliente);
			this.listaPlantas = Arrays.asList(PlantaBL.obtenerPlantas(conn));
		} catch(Exception ex) {
			log.error("Problema para obtener información de la base de datos...", ex);
		} finally {
			Conexion.close(conn);
		}
	}
        
	public void crearSalida(){
		salida = new Salida();
	}
        
	public void muestraInventario() {
		Connection conn = null;
		try {
			conn = Conexion.dsConexion();
			
			this.getFolio(conn);
			conn.commit();
			
			if(this.plantaSelected != null) {
				listaInventario = InventarioBL.obtenerInventario(conn, this.cliente, this.plantaSelected);
			} else {
				listaInventario = new ArrayList<Inventario>();
			}
		} catch(ClientesException | SQLException ex) {
			log.error("Problema para obtener información de la base de datos...", ex);
			Conexion.rollback(conn);
		} finally {
			Conexion.close(conn);
		}
	}

	public void getFolio(Connection conn) throws ClientesException, SQLException {
		FacesUtils.requireNonNull(plantaSelected, "Favor de seleccionar una planta");
		this.serie = SerieOrdenBL.obtenerSerie(conn, this.plantaSelected, this.cliente);
		this.folioSalida = SerieOrdenBL.crearFolioSalida(conn, this.plantaSelected, this.cliente, this.serie);
		log.info("Folio solicitud de salida: {}", this.folioSalida);
	}
	
	public void resultadoPeso(Inventario inventario) {
		log.debug("metodo resultadoPeso");
		BigDecimal tmp = null;
		BigDecimal cantidad = null;
		BigDecimal existencia = new BigDecimal(inventario.getExistencia());
		BigDecimal peso = inventario.getPeso();
		
		if(existencia.compareTo(BigDecimal.ZERO) == 0) {
			FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Inventario", "Cantidad incorrecta");
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
			FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Inventario", "Ha indicado una cantidad mayor a la que tiene actualmente para su producto.");
			PrimeFaces.current().ajax().update("form:messages");
			inventario.setCantidad(null);
			return;
		}
		
		if(cantidad.compareTo(BigDecimal.ZERO) < 0) {
			FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Inventario", "Ha indicado una cantidad no valida.");
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
		if (this.listaInventario.stream().filter(inv -> inv.isHabilitado()).count() > 0
				&& this.listaInventario.size() == this.listaInventarioSelect.size()) {
			this.listaInventario.stream().forEach(inv -> {
				inv.setHabilitado(true);
			});
		} else {
			this.listaInventario.stream().forEach(inv -> {
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
		if (this.listaServicios.stream().filter(servicio -> servicio.isHabilitado()).count() > 0
				&& this.listaServicios.size() == this.listaServiciosSelect.size()) {
			this.listaServicios.stream().forEach(servicio -> {
				servicio.setHabilitado(true);
			});
		} else {
			this.listaServicios.stream().forEach(servicio -> {
				servicio.setHabilitado(false);
				servicio.setCantidad(0);
			});
		}
	}
	
	public void onChangeFecha() {
		log.info("Fecha / hora solicitado: {}", this.fecha);
		
		Date fechaMin = new Date(this.fecha.getTime());
		DateUtils.setTime(fechaMin, 7, 0, 0, 0);
		
		Date fechaMax = new Date(this.fecha.getTime());
		DateUtils.setTime(fechaMax, 17, 0, 0, 0);
		
		if(this.fecha.getTime() < fechaMin.getTime() || this.fecha.getTime() > fechaMax.getTime()) {
			FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Horario no laboral", "Ha seleccionado un horario no laboral, por lo que se realizará el cargo de servicios extras. El horario laboral es de 7:00am a 5:00pm.");
			PrimeFaces.current().ajax().update("form:messages");
		}
	}
	
	public Integer numeroProductos() {
		if(this.listaInventarioSelect == null)
			return 0;
		return this.listaInventarioSelect.size();
	}
	
	public Integer cantidadTotal() {
		if(this.listaInventarioSelect == null)
			return 0;
		
		return this.listaInventarioSelect.stream().map(Inventario::getCantidad)
	    .filter(Objects::nonNull)
	    .reduce(0, Integer::sum);
	}
	
	public BigDecimal pesoTotal() {
		if(this.listaInventarioSelect == null)
			return BigDecimal.ZERO.setScale(3, RoundingMode.HALF_UP);
		
		return this.listaInventarioSelect.stream()
				.map(Inventario::getPesoAprox)
				.filter(Objects::nonNull)
				.reduce(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), BigDecimal::add)
				;
	}

	public void guardarPresalida() {
		Connection conn = null;
		String mensaje = null;
		
		this.listaSalidaDetalle = new ArrayList<SalidaDetalle>();
		this.listaServicioSalida = new ArrayList<ServicioSalida>();
		List<ServiciosExtras> listaServiciosMod = new ArrayList<ServiciosExtras>();
		String tmpFolio = null;
		
		try {
			conn = Conexion.getConnection();
			
			if(this.listaInventarioSelect == null)
				throw new ClientesException("No se detectaron productos seleccionados.");
			
			if(this.listaInventarioSelect.size() <= 0)
				throw new ClientesException("No hay productos seleccionados.");
			
			if(this.listaInventarioSelect.stream().filter(inventario -> inventario.getCantidad() == null).count() > 0)
				throw new ClientesException("Hay productos que no tienen indicada la cantidad.");
			
			boolean empty = this.listaInventarioSelect.stream().filter(inventario -> inventario.getCantidad() == 0).count() > 0;
			
			if(empty) {
				throw new ClientesException("El número de unidades de su producto es incorrecto.");
			}
			
			FacesUtils.requireNonNull(this.fecha, "Favor de llenar el campo de la fecha");
			FacesUtils.requireNonNull(this.salida.getNombreTransportista(), "Favor de llenar el campo del operador");
			FacesUtils.requireNonNull(this.salida.getPlacasTransporte(), "Favor de llenar el campo de las placas");
			FacesUtils.requireNonNull(this.salida.getObservaciones(), "Favor de llenar el campo de las obervaciones");
			FacesUtils.requireNonNull(this.cliente, "Favor de seleccionar al cliente");

			this.salida = SalidasBL.guardarSalida(conn, this.folioSalida, this.cliente, this.cteContacto, this.salida, this.fecha);
			
			if (tmpFolio == null) {
				tmpFolio = salida.getFolioSalida();
			}
			
			Salida auxSalida = SalidasBL.consultarSalida(conn, tmpFolio);
			
			this.listaSalidaDetalle = SalidasBL.agregarSalDet(conn, this.listaInventarioSelect, auxSalida);
			
			SalidasBL.guardarSalDet(conn, this.listaSalidaDetalle);
			
			if(this.isOrdenRegistrada) {
				throw new ClientesException("La orden de retiro ya se encuentra registrada.");
			}
			
			if(!this.listaServiciosSelect.isEmpty()){
				listaServiciosMod = SalidasBL.agregarServicios(this.listaServiciosSelect);
				this.listaServicioSalida = SalidasBL.agregarSrvSalida(listaServiciosMod, auxSalida, this.folioSalida);
				SalidasBL.guardarSrvSalida(conn, this.listaServicioSalida);
			}
			
			SerieOrdenBL.guardarSerieOrden(conn, this.serie);
			conn.commit();
            this.isOrdenRegistrada = true;
            
            SendMailOrdenSalida sendMail = new SendMailOrdenSalida();
            sendMail.setFolio(tmpFolio);
            sendMail.add(this.archivosList);
            sendMail.start();
            
            mensaje = String.format("Su orden de salida %s se generó correctamente. Recibirá copia de su orden vía correo electrónico.", this.folioSalida);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Emisión de salida", mensaje);
		} catch(ClientesException ex) {
			log.error("Problema con la emisión de salidas...", ex);
			Conexion.rollback(conn);
			mensaje = ex.getMessage();
			FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Emisión de salida", mensaje);
		} catch (Exception ex) {
			log.error("Problema con la emisión de salidas...", ex);
			Conexion.rollback(conn);
			mensaje = "Su solicitud no se pudo generar.\nFavor de comunicarse con el administrador del sistema.";
			FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Emisión de salida", mensaje);
		} finally {
			Conexion.close(conn);
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
	
	public void dialogoHorarioExtra(ActionEvent event) {
		FacesUtils.addDynamicDialogMessage(FacesMessage.SEVERITY_INFO, "Horario Extra", "Este horario generará un costo extra.");
	}
	
	public void cargarArchivo() {
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
			
			FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Emisión de salida", "El archivo se cargó correctamente.");
		} catch(ClientesException ex) {
			log.error("Problema con la emisión de salidas...", ex);
                        FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Emisión de salida", ex.getMessage());
		} catch (Exception ex) {
			log.error("Problema con la emisión de salidas...", ex);
			FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Emisión de salida", "Su solicitud no se pudo generar.\nFavor de comunicarse con el administrador del sistema.");
		} finally {
			tamanioTotal = tamanioTotal.divide(megabyte, BigDecimal.ROUND_HALF_UP);
			
			PrimeFaces.current().ajax().update(":form:messages");
		}
	}
	
	public void eliminarAdjunto() {
		BigDecimal tamanio = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		this.archivosList.remove(this.selectedAttachment);
		for(Adjunto a : archivosList) {
			tamanio = tamanio.add(new BigDecimal(a.getTamanio()));
		}
		this.tamanioTotal = tamanio.divide(megabyte, BigDecimal.ROUND_HALF_UP);
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

        public Salida getSalida() {
            return salida;
        }

        public void setSalida(Salida salida) {
            this.salida = salida;
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

        public Planta getPlantaSelected() {
            return plantaSelected;
        }

        public void setPlantaSelected(Planta plantaSelected) {
            this.plantaSelected = plantaSelected;
        }

	public List<Planta> getListaPlantas() {
		return listaPlantas;
	}

	public void setListaPlantas(List<Planta> listaPlantas) {
		this.listaPlantas = listaPlantas;
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
