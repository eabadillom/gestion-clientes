package com.ferbo.clientes.mail.mngr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ferbo.clientes.mail.beans.Telefono;

public class TelefonoDAO extends DAO {
	private static final String SELECT = "SELECT id_telefono, nb_telefono, st_principal, tp_telefono FROM telefono ";
	private static final String INSERT = "";
	private static final String UPDATE = "";
	private static final String DELETE = "";	
	
	private static synchronized Telefono getBean(ResultSet rs) 
	throws SQLException {
		Telefono bean = new Telefono();
		bean.setIdTelefono(getInteger(rs, "id_telefono"));
		bean.setTelefono(getTrim(rs.getString("nb_telefono")));
		bean.setPrincipal(rs.getBoolean("st_principal"));
		bean.setTipoTelefono(getInteger(rs, "tp_telefono"));
		return bean;
	}
	
	public static Telefono get(Connection conn, Integer idMedio)
	throws SQLException {
		Telefono bean = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = SELECT + "WHERE id_telefono = ?";
		int idx = 1;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(idx++, idMedio);
			
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
	
	public static synchronized int delete(Connection conn, Integer idMedio) {
		//TODO completar este metodo
		System.out.println("//TODO Completar el borrado de telefonos...");
		return 0;
	}
}