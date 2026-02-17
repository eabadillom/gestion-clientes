package com.ferbo.clientes.mail.mngr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.mail.beans.Planta;

public class PlantaDAO extends DAO {
	private static Logger log = LogManager.getLogger(PlantaDAO.class);
	private static final String SELECT = "SELECT planta_cve, planta_ds, planta_abrev, planta_sufijo, planta_cod, id_usuario FROM planta ";
	
	private static synchronized Planta getBean(ResultSet rs)
	throws SQLException {
		Planta bean = new Planta();
		bean.setId(rs.getInt("planta_cve"));
		bean.setDescripcion(getTrim(rs.getString("planta_ds")));
		bean.setNumero(getTrim(rs.getString("planta_abrev")));
		bean.setSufijo(getTrim(rs.getString("planta_sufijo")));
		bean.setCodigo(getTrim(rs.getString("planta_cod")));
		bean.setIdUsuario(getInteger(rs, "id_usuario"));
		return bean;
	}
	
	public static Planta getPlanta(Connection conn, int idPlanta)
	throws SQLException {
		Planta bean = null;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		
		try{
			sql = SELECT + " WHERE planta_cve = ? ";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idPlanta);
			log.debug("Preparando SELECT para la tabla PLANTA...");
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				bean = getBean(rs);
			}
			
			log.debug("El SELECT para la tabla PLANTA termino satisfactoriamente...");
		} finally {
			close(ps);
			close(rs);
		}
		
		return bean;
	}
	
	public static Planta[] getPlantas(Connection conn)
	throws SQLException {
		Planta            beans[] = null;
		Planta            bean       = null;
		ArrayList<Planta> alBeans = null;
		
		PreparedStatement ps = null;
		ResultSet         rs = null;
		String            sql = null;
		
		try{
			sql = SELECT + " ORDER BY planta_ds ";
			ps = conn.prepareStatement(sql);
			log.debug("Preparando SELECT para la tabla PLANTA...");
			
			rs = ps.executeQuery();
			alBeans = new ArrayList<Planta>();
			while(rs.next()) {
				bean = getBean(rs);
				alBeans.add(bean);
			}
			beans = new Planta[alBeans.size()];
			beans = (Planta[]) alBeans.toArray(beans);
			
			log.debug("El SELECT para la tabla PLANTA termino satisfactoriamente...");
		} finally {
			close(ps);
			close(rs);
		}
		
		return beans;
	}
	
	public static synchronized int insert(Connection conn, Planta bean)
	throws SQLException {
		int rows = -1;
		
		PreparedStatement ps = null;
		String            sql = null;
		int id = -1;
		int idx = 1;
		
		try {
			id = getMaxId(conn) + 1;
			bean.setId(id);
			sql = "INSERT INTO planta (planta_cve, planta_ds, planta_abrev, planta_sufijo, planta_cod, id_usuario) VALUES (?, ?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(idx++, id);
			ps.setString(idx++, getTrim(bean.getDescripcion()));
			ps.setString(idx++, getTrim(bean.getNumero()));
			ps.setString(idx++, getTrim(bean.getSufijo()));
			ps.setString(idx++, getTrim(bean.getCodigo()));
			setInteger(ps, idx++, bean.getIdUsuario());
			
			rows = ps.executeUpdate();
			
		} finally {
			close(ps);
		}
		
		return rows;
	}
	
	public static synchronized int update(Connection conn, Planta bean)
	throws SQLException {
		int rows = -1;
		
		PreparedStatement ps = null;
		String            sql = null;
		int idx = 1;
		
		try {
			sql = "UPDATE planta SET planta_ds = ?, planta_abrev = ?, planta_sufijo = ?, planta_cod = ?, id_usuario = ? " + 
					"WHERE planta_cve = ? ";
			ps = conn.prepareStatement(sql);
			ps.setString(idx++, bean.getDescripcion());
			ps.setString(idx++, bean.getNumero());
			ps.setString(idx++, bean.getSufijo());
			ps.setString(idx++, bean.getCodigo());
			setInteger(ps, idx++, bean.getIdUsuario());
			ps.setInt(idx++, bean.getId());
			
			rows = ps.executeUpdate();
			
		} finally {
			close(ps);
		}
		
		return rows;
	}
	
	public static int getMaxId(Connection conn)
	throws SQLException {
		int maxId = -1;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		
		try {
			sql = "select max(planta_cve) as planta_cve from planta";
			ps = conn.prepareStatement(sql);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				maxId = rs.getInt("planta_cve");
			}
			
		} finally {
			close(rs);
			close(ps);
		}
		
		return maxId;
	}
}