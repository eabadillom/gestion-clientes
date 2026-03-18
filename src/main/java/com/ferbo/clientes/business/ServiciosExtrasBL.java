package com.ferbo.clientes.business;

import com.ferbo.clientes.manager.ServiciosExtrasDAO;
import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.model.ServiciosExtras;
import java.sql.Connection;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class ServiciosExtrasBL 
{
    private final static Logger log = LogManager.getLogger(ServiciosExtrasBL.class);
    
    private final static ServiciosExtrasDAO serviciosDAO = new ServiciosExtrasDAO();
    
    public static List<ServiciosExtras> obtenerSrvExtras(Connection conn, Cliente cliente){
        return serviciosDAO.getServiciosExtras(conn, cliente);
    }
    
}
