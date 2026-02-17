package com.ferbo.clientes.beans;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.file.UploadedFile;

import com.ferbo.clientes.mail.beans.Adjunto;
import com.ferbo.clientes.mail.beans.Planta;
import com.ferbo.clientes.mail.mngr.PlantaDAO;
import com.ferbo.clientes.mail.report.SendMailOrdenEntrada;
import com.ferbo.clientes.manager.AvisoDAO;
import com.ferbo.clientes.manager.IngresoDAO;
import com.ferbo.clientes.manager.IngresoProductoDAO;
import com.ferbo.clientes.manager.IngresoServicioDAO;
import com.ferbo.clientes.manager.PrecioServicioDAO;
import com.ferbo.clientes.manager.ProductoDAO;
import com.ferbo.clientes.manager.ProductoPorClienteDAO;
import com.ferbo.clientes.manager.SerieConstanciaDAO;
import com.ferbo.clientes.manager.ServicioDAO;
import com.ferbo.clientes.manager.UnidadManejoDAO;
import com.ferbo.clientes.model.Aviso;
import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.model.ClienteContacto;
import com.ferbo.clientes.model.Ingreso;
import com.ferbo.clientes.model.IngresoProducto;
import com.ferbo.clientes.model.IngresoServicio;
import com.ferbo.clientes.model.PrecioServicio;
import com.ferbo.clientes.model.Producto;
import com.ferbo.clientes.model.ProductoPorCliente;
import com.ferbo.clientes.model.SerieConstancia;
import com.ferbo.clientes.model.Servicio;
import com.ferbo.clientes.model.UnidadDeManejo;
import com.ferbo.clientes.util.ClientesException;
import com.ferbo.clientes.util.Conexion;
import com.ferbo.clientes.util.DateUtils;

@Named(value = "mbEmisionEntradas")
@ViewScoped
public class MbEmisionEntradas implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(MbEmisionEntradas.class);
	
	private Ingreso ingreso;
	
	private IngresoProducto ingresoProducto;
	private IngresoProducto ingresoProductoSelect;
	private List<IngresoProducto> listaIngresoProducto;
	
	private IngresoServicio ingresoServicio;
	private IngresoServicio ingresoServicioSelect;
	private List<IngresoServicio> listaIngresoServicio;
	
	private UploadedFile attachmentFile;
	
	private Integer idServicio;
	private Servicio servicio;
	private ServicioDAO servicioManager;
	private List<Servicio> listaServicios;
	
	private PrecioServicioDAO precioServicioManager;
	private List<PrecioServicio> listaPrecioServicio;
		
	private AvisoDAO avisoManager;
	private List<Aviso> listaAvisos;
	
	private ProductoPorClienteDAO productoPorClienteManager;
	private List<ProductoPorCliente> listaProductosPorCte;
	
	private ProductoDAO productoManager;
	private List<Producto> listaProducto;
	
	private UnidadManejoDAO unidadDeManejoManager;
	private UnidadDeManejo unidadDeManejo;
	private List<UnidadDeManejo> listaUnidadDeManejo;
	
	private List<Planta> listaPlantas;
	private Integer idPlanta;
	
	private List<Adjunto> archivosList;
	private Adjunto selectedAttachment;
	
	private SerieConstancia serieConstancia;
	private SerieConstanciaDAO serieConstanciaManager;
	
	private Date fechaMin;
	private Date fecha;
	
	private Integer numTarimas;
	private Integer sumaTarimas;
	private Integer sumaCantidad;
	private BigDecimal sumaPeso;
	private boolean save;
	
	private BigDecimal tamanioTotal = null;
	private BigDecimal limite = new BigDecimal("10485760").setScale(2,BigDecimal.ROUND_HALF_UP);
	private BigDecimal megabyte = new BigDecimal("104876").setScale(2,BigDecimal.ROUND_HALF_UP);
	
	
	private Cliente cliente;
	
	
	@PostConstruct
	public void init() {	
		
		Connection conn = null;
		FacesContext faceContext = null;
		HttpServletRequest request = null;
		HttpSession session = null;
		ClienteContacto usuario = null;
		
		try {
			
			faceContext = FacesContext.getCurrentInstance();
            request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
            session = request.getSession(false);
            this.cliente = (Cliente) session.getAttribute("cliente");
			
			conn = Conexion.dsConexion();
			serieConstanciaManager = new SerieConstanciaDAO();
			productoPorClienteManager = new ProductoPorClienteDAO();
			productoManager = new ProductoDAO();
			unidadDeManejoManager = new UnidadManejoDAO();
			avisoManager = new AvisoDAO();
			precioServicioManager = new PrecioServicioDAO();
			servicioManager = new ServicioDAO();
			
			ingreso = new Ingreso();			
			ingresoProducto = new IngresoProducto();
			ingresoServicio = new IngresoServicio();
			
			ingresoProductoSelect = new IngresoProducto();
			ingresoServicioSelect = new IngresoServicio();
			
			listaIngresoProducto = new ArrayList<IngresoProducto>();
			listaPrecioServicio = new ArrayList<PrecioServicio>();
			listaServicios = new ArrayList<Servicio>();
			listaIngresoServicio = new ArrayList<IngresoServicio>();
			archivosList = new ArrayList<Adjunto>();
			
			listaAvisos = avisoManager.getAvisosCte(conn,this.cliente.getIdCliente());			
			listaPlantas = Arrays.asList(PlantaDAO.getPlantas(conn));			
			serieConstancia = serieConstanciaManager.getSerie(conn, this.cliente, "I");			
			listaUnidadDeManejo = unidadDeManejoManager.getUnidadesDeManejo(conn);
			
			fechaMin = new Date();
			ingreso.setFechaHora(new Date());
			ingreso.setFolio(getFolio());
			ingreso.setStatus(1);
			ingreso.setIdCliente(this.cliente.getIdCliente());
			
			
			usuario = (ClienteContacto) session.getAttribute("usuario");
			
			ingreso.setIdContacto(usuario.getIdContacto());
			
			numTarimas = 1;
			sumaTarimas = 0;
			sumaCantidad = 0;
			sumaPeso = new BigDecimal(0);
			save = false;
			
			defaultDataCte(conn);
			
		} catch(Exception ex) {
			log.error("Problema para obtener información de la base de datos...", ex);
		} finally {
			Conexion.close(conn);
		}
		
	}
	
	public String getFolio() {
		
		String codigo = this.cliente.getCodigoUnico();
		codigo = codigo + String.valueOf(serieConstancia.getNumero());
		
		return codigo;
	}
	
	public void onChangeFecha() {
		FacesMessage message = null;
		Severity severity = null;
		long fecha = this.ingreso.getFechaHora().getTime();
		
		Date fechaMin = new Date(fecha);
		DateUtils.setTime(fechaMin, 7, 0, 0, 0);
		
		Date fechaMax = new Date(fecha);
		DateUtils.setTime(fechaMax, 17, 0, 0, 0);
		
		if(fecha < fechaMin.getTime() || fecha > fechaMax.getTime()) {
			severity = FacesMessage.SEVERITY_WARN;
			message = new FacesMessage(severity, "Horario no laboral", "Ha seleccionado un horario no laboral, por lo que se realizará el cargo de servicios extras. El horario laboral es de 7:00am a 5:00pm.");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update("form:messages");
		}
	}
	
	public void defaultDataCte(Connection conn)throws SQLException {
		
		serviciosPorCte(conn);
		productosPorCte(conn);
		
	}
	
	//------------------------------- PRODUCTOS ----------------------------------------------
	
	public void productosPorCte(Connection conn)throws SQLException {
		
		try {
		
			listaProductosPorCte = productoPorClienteManager.getProductosPorCte(conn, this.cliente.getIdCliente());
			listaProducto = new ArrayList<Producto>();
			for(ProductoPorCliente pc: listaProductosPorCte) {
				Producto prod = new Producto();
				prod = productoManager.getProductos(conn, pc.getIdProducto());
				listaProducto.add(prod);
			}
			
		} catch (Exception e) {
			log.info("NO se pudo recuperar la lista de productos por cliente" + e.getMessage());
		}
				
		
	}
	
	public void addPartida() {
		
		Connection conn = null;
		IngresoProducto producto = null;
		Producto prodTemp = null;
		UnidadDeManejo unidadDeManejo = null;
		
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
		
			conn = Conexion.getConnection();
			log.info("Ingreso producto: {}", ingresoProducto);
			
			prodTemp = productoManager.getProductos(conn, ingresoProducto.getIdProducto());
			unidadDeManejo = unidadDeManejoManager.getUnidadDeManejo(conn, ingresoProducto.getIdUnidadMedida());
			
			ingresoProducto.setIdPlanta(idPlanta);
			ingresoProducto.setNoTarimas(BigDecimal.ONE);
			ingresoProducto.setProducto(prodTemp);
			ingresoProducto.setUnidadDeManejo(unidadDeManejo);
			
			//Integer id = 1;//asigno al ingresoproducto un id temporal
			
			for(int i=0 ; i<this.numTarimas ; i++) {
				//ingresoProducto.setIdIngresoProducto(id++);
				producto = (IngresoProducto) ingresoProducto.clone();
				
				//pie de tabla (SUMAS)
				sumaTarimas += producto.getNoTarimas().intValue();
				sumaCantidad += producto.getCantidad();
				sumaPeso = sumaPeso.add(producto.getPeso());
				
				listaIngresoProducto.add(producto);				
				
			}
			
			ingresoProducto = new IngresoProducto();
			numTarimas = 1;
			
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "Se agrego correctamente";
			
		} catch (Exception e) {
			severity = FacesMessage.SEVERITY_ERROR;
			mensaje = "Error al agregar producto";
			log.info("Error al clonar ingreso_producto"+e);
		}finally {
			Conexion.close(conn);
			message = new FacesMessage(severity,"Registro producto",mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:pnlProducto","form:messages");			
		}
				
	}
	
	
	public void deleteProducto() {
		
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
		
		listaIngresoProducto.remove(ingresoProductoSelect);
		
		//resta de cantidades del producto a eliminar
		sumaTarimas -= 1 ;
		sumaCantidad -= ingresoProductoSelect.getCantidad(); 
		sumaPeso = sumaPeso.subtract(ingresoProductoSelect.getPeso());
		
		severity = FacesMessage.SEVERITY_INFO;
		mensaje = "El producto se elimino correctamente";
		
		}catch (Exception e) {
			severity = FacesMessage.SEVERITY_ERROR;
			mensaje = "El producto no se pudo eliminar";
			log.info("Error al eliminar producto" + e.getMessage());
		}finally {
			message = new FacesMessage(severity,"Eliminacion de producto",mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			
			PrimeFaces.current().ajax().update("form:messages","form:dt-ingresoProducto");
		}
		
	}
	
	//------------------------------- SERVICIOS ----------------------------------------------
	
	
	public void serviciosPorCte(Connection conn) {
		
		Aviso avisoCte = new Aviso();
		Servicio servicio = null;
		
		try {
			
			avisoCte = listaAvisos.get(0);
			
			listaPrecioServicio = precioServicioManager.getPrecioServiciosPorCte(conn, avisoCte.getAvisoCve(), this.cliente.getIdCliente());
			
			for(PrecioServicio ps: listaPrecioServicio) {
				servicio = new Servicio();				
				servicio = servicioManager.getServicio(conn, ps.getIdServicio());
				
				listaServicios.add(servicio);
			}
			
		} catch (Exception e) {
			log.info("Error al traer precio servicio por cliente" + e.getMessage());
		}
		
	}
	
	public void unidadServicio()throws SQLException {
		
		Connection conn = null;
		Aviso avisoCte = null;
		PrecioServicio precioServicio = null;
		
		try {
			
			conn = Conexion.getConnection();
			avisoCte = listaAvisos.get(0);
			precioServicio = precioServicioManager.getPrecioServiciosPorCte(conn, avisoCte.getAvisoCve(), this.cliente.getIdCliente(), ingresoServicio.getIdServicio());
			unidadDeManejo = unidadDeManejoManager.getUnidadDeManejo(conn, precioServicio.getIdUnidad());
			
			ingresoServicio.setServicio(servicioManager.getServicio(conn, ingresoServicio.getIdServicio()));
			ingresoServicio.setUnidadDeManejo(unidadDeManejo);
			
		} finally {
			Conexion.close(conn);
		}
		
	}
	
	public void agregarServicio(){
		
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
			
			ingresoServicio.setIdUnidad(unidadDeManejo.getUnidad_de_manejo_cve());//DUDA				
			listaIngresoServicio.add(ingresoServicio);
			
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "El servicio se agrego correctamente";
			
			ingresoServicio = new IngresoServicio();
		}catch(Exception e) {
		
			severity = FacesMessage.SEVERITY_ERROR;
			mensaje = "El servicio no se registro";
			
			log.info("Error al agregar el servicio");
			
		}finally {
			message = new FacesMessage(severity, "Servicio", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:dt-Servicios","form:messages");
		}	
				
		
	}
	
	public void deleteServicio() {
		
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
			
			listaIngresoServicio.remove(ingresoServicioSelect);
			
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "Servicio eliminado correctamente";
			
		} catch (Exception e) {
			
			severity = FacesMessage.SEVERITY_ERROR;
			mensaje = "Servicio no eliminado";
			
			log.info("Error al eliminar servicio");
		}finally {
			
			message = new FacesMessage(severity, "Servicio", mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages","form:dt-Servicios");
		}

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
	
	
	public void save()throws SQLException {
		
		Connection conn = null;
		
		int insertsIngreso = 0;
		int insertsIngresoProducto = 0;
		int sumaRegistrosP = 0;
		int insertsIngresoServicio = 0;
		int sumaRegistrosS = 0;		
		int updateRegistros = 0;
		
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
			
			if(save==true) {
				throw new ClientesException("La solicitud ya está guardada.");
			}
			
			conn = Conexion.getConnection();
			
			log.info("Ingresando al registro en BD");
			
			insertsIngreso = IngresoDAO.insert(conn, ingreso);	
			
			log.info("Los renglones de la tabla insert afectados fueron: " + insertsIngreso);
			
			for(IngresoProducto ip: listaIngresoProducto) {				
				ip.setIdIngreso(ingreso.getIdIngreso());
				insertsIngresoProducto = IngresoProductoDAO.insert(conn, ip);
				sumaRegistrosP += insertsIngresoProducto;
			}
			
			log.info("Los renglones de la tabla insert producto afectados fueron: " + sumaRegistrosP);
			
			for(IngresoServicio is: listaIngresoServicio) {
				is.setIdIngreso(ingreso.getIdIngreso());
				insertsIngresoServicio = IngresoServicioDAO.insert(conn, is);
				sumaRegistrosS += insertsIngresoServicio;
			}
			
			log.info("Los renglones de la tabla insert servicios afectados fueron: " + sumaRegistrosS);			
			
			serieConstancia.setNumero(serieConstancia.getNumero() + 1);
			
//			updateRegistros = serieConstanciaManager.update(conn, serieConstancia);
			
			log.info("Se actualizo serie constancia" + updateRegistros);
			
			conn.commit();
			
			save = true;
			
			SendMailOrdenEntrada sendMail = new SendMailOrdenEntrada();
			
			sendMail.setFolio(ingreso.getFolio());
			sendMail.add(archivosList);
			sendMail.start();
			
			severity = FacesMessage.SEVERITY_INFO;
			mensaje = "La entrada se registro correctamente";
			
		} catch(ClientesException ex) {
			Conexion.rollback(conn);
			severity = FacesMessage.SEVERITY_WARN;
			mensaje = ex.getMessage();
		} catch (SQLException e) {
			Conexion.rollback(conn);
			severity = FacesMessage.SEVERITY_ERROR;
			mensaje = "La entrada no se pudo registrar Correctamente";
		}finally {
			message = new FacesMessage(severity,"Entrada",mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
			Conexion.close(conn);
		}
	}
	
	public Date getFechaMin() {
		return fechaMin;
	}

	public void setFechaMin(Date fechaMin) {
		this.fechaMin = fechaMin;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
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

	public SerieConstancia getSerieConstancia() {
		return serieConstancia;
	}

	public void setSerieConstancia(SerieConstancia serieConstancia) {
		this.serieConstancia = serieConstancia;
	}

	public List<Producto> getListaProducto() {
		return listaProducto;
	}

	public void setListaProducto(List<Producto> listaProducto) {
		this.listaProducto = listaProducto;
	}

	public UnidadManejoDAO getUnidadDeManejoManager() {
		return unidadDeManejoManager;
	}

	public void setUnidadDeManejoManager(UnidadManejoDAO unidadDeManejoManager) {
		this.unidadDeManejoManager = unidadDeManejoManager;
	}

	public List<UnidadDeManejo> getListaUnidadDeManejo() {
		return listaUnidadDeManejo;
	}

	public void setListaUnidadDeManejo(List<UnidadDeManejo> listaUnidadDeManejo) {
		this.listaUnidadDeManejo = listaUnidadDeManejo;
	}

	public Integer getNumTarimas() {
		return numTarimas;
	}

	public void setNumTarimas(Integer numTarimas) {
		this.numTarimas = numTarimas;
	}

	public IngresoProducto getIngresoProducto() {
		return ingresoProducto;
	}

	public void setIngresoProducto(IngresoProducto ingresoProducto) {
		this.ingresoProducto = ingresoProducto;
	}

	public List<IngresoProducto> getListaIngresoProducto() {
		return listaIngresoProducto;
	}

	public void setListaIngresoProducto(List<IngresoProducto> listaIngresoProducto) {
		this.listaIngresoProducto = listaIngresoProducto;
	}

	public IngresoProducto getIngresoProductoSelect() {
		return ingresoProductoSelect;
	}

	public void setIngresoProductoSelect(IngresoProducto ingresoProductoSelect) {
		this.ingresoProductoSelect = ingresoProductoSelect;
	}

	public Integer getSumaTarimas() {
		return sumaTarimas;
	}

	public void setSumaTarimas(Integer sumaTarimas) {
		this.sumaTarimas = sumaTarimas;
	}

	public Integer getSumaCantidad() {
		return sumaCantidad;
	}

	public void setSumaCantidad(Integer sumaCantidad) {
		this.sumaCantidad = sumaCantidad;
	}

	public BigDecimal getSumaPeso() {
		return sumaPeso;
	}

	public void setSumaPeso(BigDecimal sumaPeso) {
		this.sumaPeso = sumaPeso;
	}

	public List<Servicio> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios(List<Servicio> listaServicios) {
		this.listaServicios = listaServicios;
	}

	public Integer getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	public IngresoServicio getIngresoServicio() {
		return ingresoServicio;
	}

	public void setIngresoServicio(IngresoServicio ingresoServicio) {
		this.ingresoServicio = ingresoServicio;
	}

	public List<IngresoServicio> getListaIngresoServicio() {
		return listaIngresoServicio;
	}

	public void setListaIngresoServicio(List<IngresoServicio> listaIngresoServicio) {
		this.listaIngresoServicio = listaIngresoServicio;
	}

	public Ingreso getIngreso() {
		return ingreso;
	}

	public void setIngreso(Ingreso ingreso) {
		this.ingreso = ingreso;
	}

	public IngresoServicio getIngresoServicioSelect() {
		return ingresoServicioSelect;
	}

	public void setIngresoServicioSelect(IngresoServicio ingresoServicioSelect) {
		this.ingresoServicioSelect = ingresoServicioSelect;
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

	public UploadedFile getAttachmentFile() {
		return attachmentFile;
	}

	public void setAttachmentFile(UploadedFile attachmentFile) {
		this.attachmentFile = attachmentFile;
	}

	public BigDecimal getTamanioTotal() {
		return tamanioTotal;
	}

	public void setTamanioTotal(BigDecimal tamanioTotal) {
		this.tamanioTotal = tamanioTotal;
	}


	

	
}
