package com.ferbo.clientes.business;

import com.ferbo.clientes.mail.beans.Planta;
import com.ferbo.clientes.mail.mngr.PlantaDAO;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class PlantaBL 
{
    private final static Logger log = LogManager.getLogger(PlantaBL.class);
    
    public static Planta[] obtenerPlantas(Connection conn) throws SQLException{
        return PlantaDAO.getPlantas(conn);
    }
    
}
