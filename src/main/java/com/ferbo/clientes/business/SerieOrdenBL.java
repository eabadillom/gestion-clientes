package com.ferbo.clientes.business;

import com.ferbo.clientes.dao.SerieOrdenDAO;
import com.ferbo.clientes.mail.beans.Planta;
import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.model.SerieOrden;
import com.ferbo.clientes.util.Conexion;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class SerieOrdenBL 
{
    private final static Logger log = LogManager.getLogger(SerieOrdenBL.class);
    
    private final static SerieOrdenDAO serieDAO = new SerieOrdenDAO();
    
    public static void guardarSerieOrden(Connection conn, SerieOrden serie) throws SQLException {
        serie.setNumero(serie.getNumero() + 1);
        serieDAO.update(conn, serie);
    }
    
    public static SerieOrden obtenerSerie(Connection conn, Planta planta, Cliente cliente) throws SQLException
    {
        return serieDAO.get(conn, cliente.getIdCliente(), planta.getId(), "O");
    }
    
    public static String craerFolio(Connection conn, Planta planta, Cliente cliente, SerieOrden serie) throws SQLException  
    {
        String folio = null;
        
        if(serie == null) {
            serie = new SerieOrden();
            serie.setIdCliente(cliente.getIdCliente());
            serie.setTipoSerie("O");
            serie.setIdPlanta(planta.getId());
            serie.setNumero(1);
            serieDAO.insert(conn, serie);
        }

        folio = String.format("%s%s%s%d", "RO", planta.getSufijo(), cliente.getCodigoUnico(), serie.getNumero());
        return folio;
    }
    
}
