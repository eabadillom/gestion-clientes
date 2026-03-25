package com.ferbo.clientes.business;

import com.ferbo.clientes.dao.SerieOrdenDAO;
import com.ferbo.clientes.mail.beans.Planta;
import com.ferbo.clientes.manager.SalidaDAO;
import com.ferbo.clientes.manager.SalidaDetalleDAO;
import com.ferbo.clientes.manager.ServicioSalidaDAO;
import com.ferbo.clientes.manager.StatusSalidaDAO;
import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.model.ClienteContacto;
import com.ferbo.clientes.model.Inventario;
import com.ferbo.clientes.model.Salida;
import com.ferbo.clientes.model.SalidaDetalle;
import com.ferbo.clientes.model.ServicioSalida;
import com.ferbo.clientes.model.ServiciosExtras;
import com.ferbo.clientes.model.StatusSalida;
import com.ferbo.clientes.util.ClientesException;
import com.ferbo.clientes.util.DateUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class SalidasBL 
{
    private final static Logger log = LogManager.getLogger(SalidasBL.class);
    
    private final static String ENVIADO = "E";
    private final static String CANCELADO = "C";
    
    private final static SalidaDAO salidaDAO = new SalidaDAO();
    private final static SalidaDetalleDAO salidaDetalleDAO = new SalidaDetalleDAO();
    private final static ServicioSalidaDAO serviciosSalidaDAO = new ServicioSalidaDAO();
    private final static StatusSalidaDAO statusSalidaDAO = new StatusSalidaDAO();
    
    public static StatusSalida obtenerStatusEnviado(Connection conn){
        return statusSalidaDAO.findStatusByClave(conn, ENVIADO);
    }
    
    public static StatusSalida obtenerStatusCancelado(Connection conn){
        return statusSalidaDAO.findStatusByClave(conn, CANCELADO);
    }
    
    public static Salida consultarSalida(Connection conn, String folioSalida) throws SQLException{
        return salidaDAO.findSalidasByFolio(conn, folioSalida);
    }
    
    public static Salida guardarSalida(Connection conn, String folioSalida, Cliente cliente, ClienteContacto cteContacto, Salida salida, Date fecha) throws SQLException
    {
        StatusSalida statusSalidaEnviado = obtenerStatusEnviado(conn);
        Date horaSalida = DateUtils.getDate("00/00/0001", DateUtils.FORMATO_DD_MM_YYYY);
        int hora = DateUtils.getHora(fecha);
        int minuto = DateUtils.getMinuto(fecha);
        DateUtils.setTime(horaSalida, hora, minuto, 0, 0);
        
        salida.setFolioSalida(folioSalida);
        salida.setIdCliente(cliente.getIdCliente());
        salida.setIdContacto(cteContacto.getIdContacto());
        salida.setIdStatus(statusSalidaEnviado.getIdEstatus());
        salida.setFechaSalida(fecha);
        salida.setHoraSalida(horaSalida);
        salida.setFechaRegistro(new Date());

        salidaDAO.save(conn, salida);
        return salida;
    }
    
    public static List<SalidaDetalle> consultarSalidasDetalles(Connection conn, Salida salida){
        return salidaDetalleDAO.findIdSalida(conn, salida.getIdSalida());
    }
    
    public static List<SalidaDetalle> agregarSalDet(Connection conn, List<Inventario> listInventario, Salida salida){
        List<SalidaDetalle> auxSalidaDetalle = new ArrayList();
        
        for (Inventario inventarioSelect : listInventario) {
            SalidaDetalle salidaDetalle = new SalidaDetalle();
            salidaDetalle.setIdSalida(salida.getIdSalida());
            salidaDetalle.setIdPartida(inventarioSelect.getPartidaClave());
            salidaDetalle.setCantidad(inventarioSelect.getCantidad());
            salidaDetalle.setPesoAprox(inventarioSelect.getPesoAprox());
            auxSalidaDetalle.add(salidaDetalle);
        }
        
        return auxSalidaDetalle;
    }
    
    public static void guardarSalDet(Connection conn, List<SalidaDetalle> listSalidaDetalle) throws SQLException
    {
        for(SalidaDetalle aux : listSalidaDetalle){
            salidaDetalleDAO.save(conn, aux);
        }
        
    }
    
    public static List<ServiciosExtras> agregarServicios(List<ServiciosExtras> listServicios) throws ClientesException
    {
        List<ServiciosExtras> servicios = new ArrayList();
        
        for (ServiciosExtras serviciosSelect : listServicios) {
            ServiciosExtras serviciosExtras = new ServiciosExtras();
            serviciosExtras.setIdServicioExtra(serviciosSelect.getIdServicioExtra());
            serviciosExtras.setServicioExtra(serviciosSelect.getServicioExtra());
            serviciosExtras.setCantidad(serviciosSelect.getCantidad());
            serviciosExtras.setIdUnidadManejo(serviciosSelect.getIdUnidadManejo());
            servicios.add(serviciosExtras);
        }
        
        return servicios;
    }
    
    public static List<ServicioSalida> agregarSrvSalida(List<ServiciosExtras> listServicios, Salida salida, String folioSalida)
    {
        List<ServicioSalida> listServicioSalida = new ArrayList();
        
        for(ServiciosExtras servicio : listServicios) {
            servicio.setFolioSalida(folioSalida);
            ServicioSalida serviciosSalida = new ServicioSalida();
            serviciosSalida.setIdSalida(salida.getIdSalida());
            serviciosSalida.setIdServicio(servicio.getIdServicioExtra());
            serviciosSalida.setIdUnidadDeManejo(servicio.getIdUnidadManejo());
            serviciosSalida.setCantidad(servicio.getCantidad());
            listServicioSalida.add(serviciosSalida);
        }
        
        return listServicioSalida;
    }
    
    public static void guardarSrvSalida(Connection conn, List<ServicioSalida> listSrvSalida) throws SQLException
    {
        if(!listSrvSalida.isEmpty()){
            for(ServicioSalida aux : listSrvSalida){
                serviciosSalidaDAO.save(conn, aux);
            }
        }
    }
    
}
