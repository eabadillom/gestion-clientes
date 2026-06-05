package com.ferbo.clientes.beans;

import com.ferbo.clientes.business.ConsultaSalidasBL;
import com.ferbo.clientes.mail.report.SendMailCancelacionOrdenSalida;
import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.model.ClienteContacto;
import com.ferbo.clientes.util.ClientesException;
import com.ferbo.clientes.util.Conexion;
import com.ferbo.clientes.util.DateUtils;
import com.ferbo.clientes.util.ManageStatus;
import com.ferbo.gestion.core.model.catalogo.almacen.Camara;
import com.ferbo.gestion.core.model.catalogo.almacen.Planta;
import com.ferbo.gestion.core.model.inventario.entrada.Partida;
import com.ferbo.gestion.core.model.inventario.salida.orden.Salida;
import com.ferbo.gestion.core.model.inventario.salida.orden.SalidaDetalle;
import com.ferbo.gestion.reports.jasper.OrdenRetiroJR;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

@Named(value = "consultarSalidas")
@ViewScoped
public class ConsultarSalidasBean implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(ConsultarSalidasBean.class);
    
    private final ConsultaSalidasBL consultaSalidasBL = new ConsultaSalidasBL();
    
    private List<Salida> listSalidas;
    private Salida salidaSelected;
    
    private List<SalidaDetalle> listSalidaDetalle;
    private List<Partida> listPartidas;
    
    private Integer totalCantidad;
    private BigDecimal totalPeso;
    
    private Date fechaInicio;
    private Date fechaFin;
    
    private Cliente cliente;
    private ClienteContacto cteContacto;
    private FacesContext context;
    private HttpServletRequest request;
    private HttpSession session;
    
    private OrdenRetiroJR ordenRetiroJR;
    private StreamedContent filePDF;
    
    private ManageStatus status;

    public ConsultarSalidasBean() {
        this.fechaFin = new Date();
        this.fechaInicio = DateUtils.addDay(fechaFin, -7);
        
        this.context = FacesContext.getCurrentInstance();
        this.request = (HttpServletRequest) this.context.getExternalContext().getRequest();
        this.session = this.request.getSession(false);
        this.cliente = (Cliente) this.session.getAttribute("cliente");
        this.cteContacto = (ClienteContacto) this.request.getSession(false).getAttribute("usuario");
        this.listSalidas = new ArrayList();
        status = new ManageStatus();
        
        String mensaje = String.format("Usuario %s ingresa a Consulta de orden de salida.", this.cteContacto.getUsuario());
        log.info(mensaje);
    }
    
    public void consultarSalidas() 
    {
        FacesMessage message = null;
        String mensaje = "Consulta de Ordenes de Retiro";
        String detalle = null;
        Severity severity = null;

        try {
            if(this.fechaInicio == null)
                throw new ClientesException("Debe seleccionar la fecha de inicio.");
            
            if(this.fechaFin == null)
                throw new ClientesException("Debe seleccionar la fecha fin.");
            
            if(this.cliente == null)
                throw new ClientesException("No se encuentra el cliente disponible.");
            
            this.listSalidas = this.consultaSalidasBL.buscarPorClientePeriodo(this.cliente.getIdCliente(), DateUtils.convertirALocalDate(this.fechaInicio), DateUtils.convertirALocalDate(this.fechaFin));
            
            severity = FacesMessage.SEVERITY_INFO;
            detalle = "Su consulta fue generada con exito.";
        } catch(ClientesException ex) {
            log.warn("Solicitud incorrecta: {}", ex.getMessage());
            detalle = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch(Exception ex) {
            log.error("Error con consultar la solicitud...", ex);
            detalle = "Error interno del sistema. Intente nuevamente. Si el problema persiste, contacte al soporte del sistema.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, mensaje, detalle);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:dt-salidas");
        }
    }
    
    public void obtenerDetalles(Salida salida) 
    {
        FacesMessage message = null;
        String mensaje = "Consulta de Ordenes de Retiro";
        String detalle = null;
        Severity severity = null;
        try {
            if(salida == null)
                throw new ClientesException("Error al seleccionar la salida, intente nuevamente. Si el problema persiste, contacte al soporte del sistema");
            
            this.salidaSelected = salida;
            
            this.totalCantidad = 0;
            this.totalPeso = BigDecimal.ZERO;
            this.listSalidaDetalle = this.salidaSelected.getDetalles();
            
            for(SalidaDetalle aux : this.listSalidaDetalle){
                this.totalCantidad = this.totalCantidad + aux.getCantidad();
                this.totalPeso = this.totalPeso.add(aux.getPeso());
            }
            
            severity = FacesMessage.SEVERITY_INFO;
            detalle = String.format("Ha seleccionado el orden de salida %s.", this.salidaSelected.getFolio());
        } catch(ClientesException ex) {
            log.warn("Solicitud incorrecta: {}", ex.getMessage());
            detalle = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch(Exception ex) {
            log.error("Error con consultar la solicitud...", ex);
            detalle = "Error interno del sistema. Intente nuevamente. Si el problema persiste, contacte al soporte del sistema.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, mensaje, detalle);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void obtenerReporte() 
    {
        String filename = null;
        String images = "/images/logo.jpeg";
        FacesMessage message = null;
        String mensaje = null;
        Severity severity = null;
        Connection conn = null;
        List<Integer> alPlantas = null;
        Boolean isHorarioNoLaboral = null;
        
        Date fechaSalida = null;
        Date horaSalida = null;
        Date horaLimite = null;
        Integer diaSalida = null;
        
        try {
            if(this.salidaSelected == null)
                throw new ClientesException("Error folio de salida no encontrado. Si el problema persiste, contacte al soporte del sistema");
            
            filename = String.format("OrdenSalida%s.pdf", this.salidaSelected.getFolio());
            conn = Conexion.getConnection();
            
            alPlantas = new ArrayList<Integer>();
            if (horaSalida == null)
                horaSalida = DateUtils.convertirADate(salidaSelected.getHoraSalida());

            if (fechaSalida == null)
                fechaSalida = DateUtils.convertirADate(salidaSelected.getFechaSalida());
            
            List<SalidaDetalle> listSalidaDetalle = salidaSelected.getDetalles();
            
            for (SalidaDetalle detSalida : listSalidaDetalle) 
            {
                Partida partida = detSalida.getPartida();
                Camara camara = partida.getCamara();
                Planta planta = camara.getPlanta();
                
                if (alPlantas.contains(planta.getId())) {
                    continue;
                }

                alPlantas.add(camara.getPlanta().getId());
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
            
            this.ordenRetiroJR = new OrdenRetiroJR(conn, images);
            
            for (Integer idPlanta : alPlantas) {
                byte[] bytes = this.ordenRetiroJR.getPDF(this.salidaSelected.getFolio(), idPlanta, isHorarioNoLaboral);
                InputStream input = new ByteArrayInputStream(bytes);
                this.filePDF = DefaultStreamedContent.builder().contentType("application/pdf").name(filename).stream(() -> input).build();
                log.info("Generado {}...", filename);
            }
            
            log.info("Documentos generados");
            mensaje = String.format("Se ha descargado el orden de salida %s.", this.salidaSelected.getFolio());
            severity = FacesMessage.SEVERITY_INFO;
        } catch (Exception ex) {
            log.error("Problema general...", ex);
            mensaje = String.format("No se puede descargar el folio %s", this.salidaSelected.getFolio());
            severity = FacesMessage.SEVERITY_ERROR;
            PrimeFaces.current().ajax().update("form:messages");
        } finally {
            Conexion.close(conn);
            message = new FacesMessage(severity, "Consulta de Ordenes de Retiro", mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
    public void cancelarOrden(){
        FacesMessage message = null;
        String mensaje = null;
        Severity severity = null;
        try{
            if(this.salidaSelected == null)
                throw new ClientesException("Error: folio de salida no encontrado. Si el problema persiste, contacte al soporte del sistema");
            
            SendMailCancelacionOrdenSalida sendMailCancelacionOrdenSalida = new SendMailCancelacionOrdenSalida();
            sendMailCancelacionOrdenSalida.setSalidaSelected(this.salidaSelected);
            sendMailCancelacionOrdenSalida.start();
            
            mensaje = String.format("Enviando la petición de cancelación del folio %s", this.salidaSelected.getFolio());
            severity = FacesMessage.SEVERITY_INFO;
        } catch (Exception ex) {
            log.error("Problema general...", ex);
            mensaje = String.format("No se puede cancelar el folio %s", this.salidaSelected.getFolio());
            severity = FacesMessage.SEVERITY_ERROR;
            PrimeFaces.current().ajax().update("form:messages");
        } finally {
            message = new FacesMessage(severity, "Consulta de Ordenes de Retiro", mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ClienteContacto getCteContacto() {
        return cteContacto;
    }

    public void setCteContacto(ClienteContacto cteContacto) {
        this.cteContacto = cteContacto;
    }

    public List<Salida> getListSalidas() {
        return listSalidas;
    }

    public void setListSalidas(List<Salida> listSalidas) {
        this.listSalidas = listSalidas;
    }

    public Salida getSalidaSelected() {
        return salidaSelected;
    }

    public void setSalidaSelected(Salida salidaSelected) {
        this.salidaSelected = salidaSelected;
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

    public List<SalidaDetalle> getListSalidaDetalle() {
        return listSalidaDetalle;
    }

    public void setListSalidaDetalle(List<SalidaDetalle> listSalidaDetalle) {
        this.listSalidaDetalle = listSalidaDetalle;
    }

    public List<Partida> getListPartidas() {
        return listPartidas;
    }

    public void setListPartidas(List<Partida> listPartidas) {
        this.listPartidas = listPartidas;
    }

    public Integer getTotalCantidad() {
        return totalCantidad;
    }

    public void setTotalCantidad(Integer totalCantidad) {
        this.totalCantidad = totalCantidad;
    }

    public BigDecimal getTotalPeso() {
        return totalPeso;
    }

    public void setTotalPeso(BigDecimal totalPeso) {
        this.totalPeso = totalPeso;
    }

    public StreamedContent getFilePDF() {
        return filePDF;
    }

    public void setFilePDF(StreamedContent filePDF) {
        this.filePDF = filePDF;
    }

    public ManageStatus getStatus() {
        return status;
    }

    public void setStatus(ManageStatus status) {
        this.status = status;
    }
    
}
