package com.ferbo.clientes.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.model.Aviso;
import com.ferbo.clientes.model.Cliente;

public class AvisoDAO extends DAO{
	
	private static Logger log = LogManager.getLogger(AvisoDAO.class);
	
	private static final String SELECT = "SELECT aviso_cve, planta_cve, aviso_observaciones, cte_cve FROM aviso ";

	public Aviso getBean(ResultSet rs)throws SQLException {
		
		Aviso bean = new Aviso();
		
		bean.setAvisoCve(rs.getInt("aviso_cve"));
		bean.setPlantaCve(rs.getInt("planta_cve"));
		bean.setAvisoObservaciones(rs.getString("aviso_observaciones"));
		bean.setCteCve(rs.getInt("cte_cve"));
		
		return bean;
	}
	
	public List<Aviso> getAvisosCte(Connection conn, Integer idCliente)throws SQLException{
		
		List<Aviso> beans = new ArrayList<Aviso>();
		Aviso bean = new Aviso();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			
			sql = SELECT + "WHERE cte_cve = ?";
			
			ps = conn.prepareStatement(sql);
			
			ps.setInt(1, idCliente);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				bean = getBean(rs);
				beans.add(bean);
			}
			
		} finally {
			close(ps);
			close(rs);
		}
		
		return beans;
		
	}
	
}
