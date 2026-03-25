package com.ferbo.clientes.mail.mngr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ferbo.clientes.mail.beans.Contacto;

public class ContactoDAO extends DAO {
	private static final String SELECT = "SELECT id_contacto, nb_nombre, nb_apellido_1, nb_apellido_2 FROM contacto ";
	private static final String INSERT = "INSERT INTO contacto (id_contacto, nb_nombre, nb_apellido_1, nb_apellido_2) VALUES (?, ?, ?, ?)";
	//private static final String UPDATE = "UPDATE contacto SET nb_nombre = ?, nb_apellido_1 = ?, nb_apellido_2 = ? WHERE id_contacto = ? ";
	//private static final String DELETE = "";
	
	private static synchronized Contacto getBean(ResultSet rs)
	throws SQLException {
		Contacto bean = new Contacto();
		
		bean.setIdContacto(rs.getInt("id_contacto"));
		bean.setNombre(rs.getString("nb_nombre"));
		bean.setApellido1(rs.getString("nb_apellido_1"));
		bean.setApellido2(rs.getString("nb_apellido_2"));
		
		return bean;
	}
	
	public static Contacto get(Connection conn, int idContacto)
	throws SQLException {
		Contacto bean = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		int idx = 1;
		
		try {
			sql = SELECT + "WHERE id_contacto = ? ";
			ps = conn.prepareStatement(sql);
			ps.setInt(idx++, idContacto);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				bean = getBean(rs);
			}
		} finally {
			close(rs);
			
			close(ps);
		}
		
		return bean;
	}
	
	public static synchronized Integer getMaxId(Connection conn)
	throws SQLException {
		Integer idContacto = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			sql = "SELECT max(id_contacto) as id_contacto FROM contacto";
			ps = conn.prepareStatement(sql);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				idContacto = rs.getInt("id_contacto");
			}
			
		} finally {
                        close(rs);
			close(ps);
		}
		
		return idContacto;
	}
	
	public static synchronized int insert(Connection conn, Contacto bean)
	throws SQLException {
		int rows = -1;
		PreparedStatement ps = null;
		Integer idContacto = null;
		int idx = 1;
		
		try {
			idContacto = getMaxId(conn) + 1;
			bean.setIdContacto(idContacto);
			
			ps = conn.prepareStatement(INSERT);
			
			ps.setInt(idx++, bean.getIdContacto());
			ps.setString(idx++, bean.getNombre());
			ps.setString(idx++, bean.getApellido1());
			ps.setString(idx++, bean.getApellido2());
			
			rows = ps.executeUpdate();
			
		} finally {
			close(ps);
		}
		
		return rows;
	}
}
