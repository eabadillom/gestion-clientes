package com.ferbo.clientes.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.model.Factura;
import com.ferbo.clientes.model.StatusFactura;

public class StatusFacturaDAO extends DAO{
	private static Logger log = LogManager.getLogger(StatusFacturaDAO.class);
    private static final String SELECT = " SELECT id, nombre, descripcion FROM status_factura ";

	
    public static StatusFactura getModel(ResultSet rs) throws SQLException {
    	
    	StatusFactura sf = new StatusFactura();
    	sf.setId(rs.getInt("id"));
    	sf.setNombre(rs.getString("nombre"));
    	sf.setDescripcion(rs.getString("descripcion"));
    	
		return sf;
    	
    }
    
    public StatusFactura buscarPorIdStatus(Connection conn, Integer idStatus) throws SQLException {
    	StatusFactura sf = new StatusFactura();
    	PreparedStatement psSelect = null;
        ResultSet rs = null;
        String fSelect = null;
        
    	try {
    		fSelect = SELECT + " WHERE id = ? ";
    		psSelect = conn.prepareStatement(fSelect);
    		psSelect.setInt(1, idStatus);
    		rs = psSelect.executeQuery();
    		
    		while(rs.next()) {
    			sf = getModel(rs);

    		}
		}finally {
			close(rs);
			close(psSelect);
		}
		return sf;
    	
    }
    
	
	
	
	
	
	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		StatusFacturaDAO.log = log;
	}

}
