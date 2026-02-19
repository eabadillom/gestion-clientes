package com.ferbo.clientes.mail.mngr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ferbo.clientes.mail.beans.MedioContacto;

public class MedioContactoDAO  extends DAO{
	
	private static final String SELECT = "SELECT id_medio, tp_medio, st_medio, id_contacto, id_mail, id_telefono FROM medio_cnt ";
	private static final String INSERT = "INSERT INTO medio_cnt (tp_medio, st_medio, id_contacto, id_mail, id_telefono) VALUES(?, ?, ?, ?, ?) ";
	private static final String DELETE = "DELETE FROM medio_cnt WHERE id_medio = ? AND tp_medio = ?";
	
	private static synchronized MedioContacto getBean(Connection conn, ResultSet rs)
	throws SQLException {
		MedioContacto bean = new MedioContacto();
		bean.setIdMedio(getInteger(rs, "id_medio"));
		bean.setTipoMedio(rs.getString("tp_medio"));
		bean.setHabilitado(rs.getBoolean("st_medio"));
		bean.setIdContacto(rs.getInt("id_contacto"));
		bean.setIdMail(getInteger(rs, "id_mail"));
		bean.setIdTelefono(getInteger(rs, "id_telefono"));
		return bean;
	}
	
	public static MedioContacto get(Connection conn, Integer idMedio, String tipoMedio)
	throws SQLException {
		MedioContacto bean = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		int idx = 1;
		
		try {
			sql = SELECT + "WHERE id_medio = ? AND tp_medio = ? ";
			ps = conn.prepareStatement(sql);
			ps.setInt(idx++, idMedio);
			ps.setString(idx++, tipoMedio);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				bean = getBean(conn, rs);
			}
			
		} finally {
			close(rs);
			close(ps);
		}
		
		return bean;
	}
	
	public static MedioContacto[] get(Connection conn, Integer idContacto)
	throws SQLException {
		MedioContacto[]     beans = null;
		List<MedioContacto> alBeans = null;
		MedioContacto       bean = null;
		PreparedStatement   ps = null;
		ResultSet           rs = null;
		String              sql = null;
		int                 idx = 1;
		
		try {
			sql = SELECT + "WHERE id_contacto = ? ";
			ps = conn.prepareStatement(sql);
			ps.setInt(idx++, idContacto);
			
			
			rs = ps.executeQuery();
			alBeans = new ArrayList<MedioContacto>();
			while(rs.next()) {
				bean = getBean(conn, rs);
				
				alBeans.add(bean);
			}
			
			beans = new MedioContacto[alBeans.size()];
			beans = (MedioContacto[]) alBeans.toArray(beans);
			
		} finally {
			close(rs);
			close(ps);
		}
		
		return beans;
	}
	
	public static synchronized int getMaxId(Connection conn)
	throws SQLException {
		Integer maxId = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			sql = "SELECT MAX(id_medio) as id_medio FROM medio_cnt ";
			ps = conn.prepareStatement(sql);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				maxId = rs.getInt("id_medio");
			}
			
			if(maxId == null)
				maxId = new Integer(0);
			
		} finally {
			close(rs);
			close(ps);
		}
		
		return maxId;
	}
	
	public static synchronized int insert(Connection conn, MedioContacto bean)
	throws SQLException{
		int rows = -1;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int idx = 1;
		
		try {
			ps = conn.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(idx++, bean.getTipoMedio());
			ps.setBoolean(idx++, bean.isHabilitado());
			ps.setInt(idx++, bean.getIdContacto());
			setInteger(ps, idx++, bean.getIdMail());
			setInteger(ps, idx++, bean.getIdTelefono());
			
			rows = ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			
			if(rs.next()) {
				bean.setIdMedio(rs.getInt(1));
			}
			
		} finally {
                        close(rs);
			close(ps);
		}
		return rows;
	}
	
	
	
	public static synchronized int delete(Connection conn, MedioContacto bean)
	throws SQLException {
		int rows = -1;
		PreparedStatement ps = null;
		int idx = 1;
		
		try {
			
			ps = conn.prepareStatement(DELETE);
			ps.setInt(idx++, bean.getIdMedio());
			ps.setString(idx++, bean.getTipoMedio());
			
			rows = ps.executeUpdate();
			
		} finally {
			close(ps);
		}
		
		return rows;
	}
}
