package com.ferbo.clientes.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.model.Servicio;

public class ServicioDAO extends DAO {
	
	private static Logger log = LogManager.getLogger(ServicioDAO.class);
	
	private static final String SELECT = "SELECT SERVICIO_CVE, SERVICIO_DS, COBRO, SERVICIO_COD, cd_unidad, SERVICIO_NOM, uuid, ST_DEFAULT"
			+ " FROM servicio "; 

	public Servicio getBean(ResultSet rs)throws SQLException{
		
		Servicio bean = new Servicio();
		
		bean.setServicioCve(rs.getInt("SERVICIO_CVE"));
		bean.setServicioDs(rs.getString("SERVICIO_DS"));
		bean.setCobro(rs.getInt("COBRO"));
		bean.setServicioCod(rs.getString("SERVICIO_COD"));
		bean.setCdUnidad(rs.getString("cd_unidad"));
		bean.setServicioNom(rs.getString("SERVICIO_NOM"));
		bean.setUuid(rs.getString("uuid"));
		bean.setSt_Default(rs.getBoolean("ST_DEFAULT"));
		
		return bean;
	}
	
	public Servicio getServicio(Connection conn,Integer idServicio)throws SQLException {
		
		Servicio bean = new Servicio();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			
			sql = SELECT + " WHERE SERVICIO_CVE = ? ";
			
			ps = conn.prepareStatement(sql);
			
			ps.setInt(1, idServicio);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				bean = getBean(rs);
			}
			
		}finally {
			
		}
		
		return bean;
		
	}
	
	
}
