package com.ferbo.clientes.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.model.PrecioServicio;

public class PrecioServicioDAO extends DAO{

	private static Logger log = LogManager.getLogger(PrecioServicioDAO.class);
	
	private static final String SELECT = "SELECT id, cliente, servicio, unidad, precio, aviso_cve "
			+ " FROM precio_servicio ";
	
	public PrecioServicio getBean(ResultSet rs) throws SQLException{
		
		PrecioServicio bean = new PrecioServicio();
		
		bean.setId(rs.getInt("id"));
		bean.setIdCliente(rs.getInt("cliente"));
		bean.setIdServicio(rs.getInt("servicio"));
		bean.setIdUnidad(rs.getInt("unidad"));
		bean.setPrecio(rs.getBigDecimal("precio"));
		bean.setAvisoCve(rs.getInt("aviso_cve"));
		
		return bean;
		
	}
	
	public List<PrecioServicio> getPrecioServiciosPorCte(Connection conn,Integer idAviso, Integer idCliente)throws SQLException{
		
		List<PrecioServicio> beans = new ArrayList<PrecioServicio>();
		PrecioServicio bean = new PrecioServicio();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			
			int id = 1;
			
			sql = SELECT + "WHERE aviso_cve = ? AND cliente = ? ";			
			ps = conn.prepareStatement(sql);
			
			ps.setInt(id++, idAviso );
			ps.setInt(id++, idCliente);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				bean = getBean(rs);
				beans.add(bean);
			}
			
		}finally {
			close(ps);
			close(rs);
		}
		
		return beans;
		
	}
	
	
public PrecioServicio getPrecioServiciosPorCte(Connection conn,Integer idAviso, Integer idCliente, Integer idServicio)throws SQLException{
		
		
		PrecioServicio bean = new PrecioServicio();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			
			int id = 1;
			
			sql = SELECT + "WHERE aviso_cve = ? AND cliente = ? AND servicio = ? ";			
			ps = conn.prepareStatement(sql);
			
			ps.setInt(id++, idAviso );
			ps.setInt(id++, idCliente);
			ps.setInt(id++, idServicio );
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				bean = getBean(rs);		
			}
			
		}finally {
			close(ps);
			close(rs);
		}
		
		return bean;
		
	}
	
	
}
