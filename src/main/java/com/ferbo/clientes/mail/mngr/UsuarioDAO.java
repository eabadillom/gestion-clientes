package com.ferbo.clientes.mail.mngr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.mail.beans.Usuario;

public class UsuarioDAO extends DAO {
	
	private static Logger log = LogManager.getLogger(UsuarioDAO.class);
	private static final String SELECT = "SELECT id, usuario, password, nombre, apellido_1, apellido_2, descripcion, perfil, mail, id_planta FROM usuario ";
	private static final String INSERT = "INSERT INTO usuario (usuario, password, nombre, descripcion, perfil, apellido_1, apellido_2, mail, id_planta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE usuario SET password = ?, nombre = ?, descripcion = ?, perfil = ?, apellido_1 = ?, apellido_2 = ?, mail = ?, id_planta = ? WHERE id = ? AND usuario = ? "; 
	
	private static synchronized Usuario getBean(ResultSet rs)
	throws SQLException {
		Usuario bean = new Usuario();
		bean.setId(getInteger(rs, "id"));
		bean.setUsuario(getTrim(rs.getString("usuario")));
		bean.setPassword(getTrim(rs.getString("password")));
		bean.setNombre(getTrim(rs.getString("nombre")));
		bean.setApellido1(getTrim(rs.getString("apellido_1")));
		bean.setApellido2(getTrim(rs.getString("apellido_2")));
		bean.setDescripcion(getTrim(rs.getString("descripcion")));
		bean.setPerfil(getInteger(rs, "perfil"));
		bean.setMail(getTrim(rs.getString("mail")));
		bean.setIdPlanta(getInteger(rs, "id_planta"));
		return bean;
	}
	
	private static synchronized Usuario[] toArray(List<Usuario> alBeans) {
		Usuario [] beans = null;
		beans = new Usuario[alBeans.size()];
		beans = (Usuario[]) alBeans.toArray(beans);
		return beans;
	}
	
	public static Usuario get(Connection conn, String login)
	throws SQLException {
		Usuario usr = null;
		PreparedStatement psSelect = null;
		ResultSet rsSelect = null;
		String sqlSelect = null;
		
		try {
			sqlSelect = SELECT + " WHERE usuario = ? ";
			psSelect = conn.prepareStatement(sqlSelect);
			psSelect.setString(1, login);
			log.debug("Preparando sentencia SELECT para la tabla USUARIO...");
			log.trace("usuario: " + login);
			
			rsSelect = psSelect.executeQuery();
			
			if(rsSelect.next()) {
				usr = getBean(rsSelect);
			}
			
			log.debug("La sentencia SELECT para la tabla USUARIO termino satisfactoriamente.");
			
		} finally {
			close(rsSelect);
			close(psSelect);
		}
		
		return usr;
	}
	
	public static Usuario getByIdUsuario(Connection conn, int idUsuario)
	throws SQLException {
		Usuario usr = null;
		PreparedStatement psSelect = null;
		ResultSet rsSelect = null;
		String sqlSelect = null;
		
		try {
			sqlSelect = SELECT + " WHERE id = ? ";
			psSelect = conn.prepareStatement(sqlSelect);
			psSelect.setInt(1, idUsuario);
			log.debug("Preparando sentencia SELECT para la tabla USUARIO...");
			log.trace("usuario: " + idUsuario);
			
			rsSelect = psSelect.executeQuery();
			
			if(rsSelect.next()) {
				usr = getBean(rsSelect);
			}
			
			log.debug("La sentencia SELECT para la tabla USUARIO termino satisfactoriamente.");
			
		} finally {
			close(rsSelect);
			close(psSelect);
		}
		
		return usr;
	}
	
	public static Usuario[] get(Connection conn)
	throws SQLException {
		Usuario[] beans = null;
		List<Usuario> alBeans = null;
		Usuario bean = null;
		PreparedStatement psSelect = null;
		ResultSet rsSelect = null;
		String sqlSelect = null;
		
		try {
			sqlSelect = SELECT + " ORDER BY usuario";
			psSelect = conn.prepareStatement(sqlSelect);
			
			rsSelect = psSelect.executeQuery();
			alBeans = new ArrayList<Usuario>();
			while(rsSelect.next()) {
				bean = getBean(rsSelect);
				alBeans.add(bean);
			}
			beans = toArray(alBeans);
			
		} finally {
			close(rsSelect);
			close(psSelect);
		}
		
		return beans;
	}
	
	public static Usuario[] get(Connection conn, int idPerfil)
	throws SQLException {
		Usuario[] beans = null;
		List<Usuario> alBeans = null;
		Usuario usr = null;
		PreparedStatement psSelect = null;
		ResultSet rsSelect = null;
		String sqlSelect = null;
		
		try {
			sqlSelect = SELECT + " WHERE perfil = ? ";
			psSelect = conn.prepareStatement(sqlSelect);
			psSelect.setInt(1, idPerfil);
			log.debug("Preparando sentencia SELECT para la tabla USUARIO...");
			log.trace("usuario: " + idPerfil);
			
			rsSelect = psSelect.executeQuery();
			alBeans = new ArrayList<Usuario>();
			while(rsSelect.next()) {
				usr = getBean(rsSelect);
				alBeans.add(usr);
			}
			
			beans = toArray(alBeans);
			log.debug("La sentencia SELECT para la tabla USUARIO termino satisfactoriamente.");
			
		} finally {
			close(rsSelect);
			close(psSelect);
		}
		
		return beans;
	}
	
	public static synchronized int insert(Connection conn, Usuario bean)
	throws SQLException {
		int rows = -1;
		PreparedStatement ps = null;
		ResultSet rs =null;
		int idx = 1;
		
		try {
			ps = conn.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(idx++, getTrim(bean.getUsuario()));
			ps.setString(idx++, getTrim(bean.getPassword()));
			ps.setString(idx++, getTrim(bean.getNombre()));
			ps.setString(idx++, getTrim(bean.getDescripcion()));
			ps.setInt(idx++, bean.getPerfil());
			ps.setString(idx++, getTrim(bean.getApellido1()));
			ps.setString(idx++, getTrim(bean.getApellido2()));
			ps.setString(idx++, getTrim(bean.getMail()));
			ps.setInt(idx++, bean.getIdPlanta());
			
			rows = ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs.next())
				bean.setId(rs.getInt(1));
			
		} finally {
			close(rs);
			close(ps);
		}
		
		return rows;
	}
	
	public static synchronized int update(Connection conn, Usuario bean)
	throws SQLException {
		int rows = -1;
		PreparedStatement ps = null;
		ResultSet rs =null;
		int idx = 1;
		
		try {
			ps = conn.prepareStatement(UPDATE);
			
			ps.setString(idx++, getTrim(bean.getPassword()));
			ps.setString(idx++, getTrim(bean.getNombre()));
			ps.setString(idx++, getTrim(bean.getDescripcion()));
			ps.setInt(idx++, bean.getPerfil());
			ps.setString(idx++, getTrim(bean.getApellido1()));
			ps.setString(idx++, getTrim(bean.getApellido2()));
			ps.setString(idx++, getTrim(bean.getMail()));
			ps.setInt(idx++, bean.getIdPlanta());
			ps.setInt(idx++, bean.getId());
			ps.setString(idx++, getTrim(bean.getUsuario()));
			
			rows = ps.executeUpdate();
			
		} finally {
			close(rs);
			close(ps);
		}
		
		return rows;
	}
	
}