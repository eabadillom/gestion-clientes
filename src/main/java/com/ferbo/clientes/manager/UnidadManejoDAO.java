package com.ferbo.clientes.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.model.UnidadDeManejo;


public class UnidadManejoDAO extends DAO{
	
	private static Logger log = LogManager.getLogger(UnidadManejoDAO.class);
	private static final String SELECT = "SELECT UNIDAD_DE_MANEJO_CVE, UNIDAD_DE_MANEJO_DS"
			+ " FROM unidad_de_manejo";
	
	private static UnidadDeManejo getBean(ResultSet rs) throws SQLException{
		
		UnidadDeManejo bean = new UnidadDeManejo();
		
		bean.setUnidad_de_manejo_cve(rs.getInt("UNIDAD_DE_MANEJO_CVE"));
		bean.setUnidad_de_manejo_ds(rs.getString("UNIDAD_DE_MANEJO_DS"));
		
		return bean;
	}
	
	
	public List<UnidadDeManejo> getUnidadesDeManejo(Connection con)throws SQLException{
		
		List<UnidadDeManejo> beans = new ArrayList<UnidadDeManejo>();
		UnidadDeManejo bean = new UnidadDeManejo();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			sql = SELECT;
			
			ps = con.prepareStatement(sql);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				bean = getBean(rs);
				beans.add(bean);
			}
			
		}finally {
			close(rs);
			close(ps);
		}
		
		return beans;		
	}
	
	
	public UnidadDeManejo getUnidadDeManejo(Connection conn, Integer idUnidad) throws SQLException{
		
		UnidadDeManejo bean = new UnidadDeManejo();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		
		
		try {
			
			sql = SELECT + " WHERE UNIDAD_DE_MANEJO_CVE = ?";
			
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idUnidad);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				bean = getBean(rs);
			}
			
		}finally {
			close(ps);
			close(rs);
		}
		
		
		return bean;
	}
	
	
	
	

}
