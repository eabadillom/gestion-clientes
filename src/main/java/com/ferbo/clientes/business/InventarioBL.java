package com.ferbo.clientes.business;

import com.ferbo.clientes.mail.beans.Planta;
import com.ferbo.clientes.manager.EmisionSalidasDAO;
import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.model.Inventario;
import java.sql.Connection;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class InventarioBL 
{
    private final static Logger log = LogManager.getLogger(InventarioBL.class);
    
    private final static EmisionSalidasDAO emisionSalidasDAO = new EmisionSalidasDAO();
    
    public static List<Inventario> obtenerInventario(Connection conn, Cliente cliente, Planta planta){
        return emisionSalidasDAO.getInventario(conn, cliente, planta.getId());
    }
    
}
