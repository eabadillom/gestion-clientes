package com.ferbo.clientes.mail.mngr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ferbo.clientes.mail.beans.Mail;


public class MailDAO extends DAO {
	private static final String SELECT = "SELECT id_mail, nb_mail, st_principal, tp_mail FROM mail ";
	private static final String INSERT = "INSERT INTO mail (nb_mail, st_principal, tp_mail) VALUES(?, ?, ?)";
	private static final String DELETE = "DELETE FROM mail WHERE id_mail = ? ";
	
	private static synchronized Mail getBean(ResultSet rs)
	throws SQLException {
		Mail bean = new Mail();
		bean.setIdMail(getInteger(rs, "id_mail"));
		bean.setMail(getTrim(rs.getString("nb_mail")));
		bean.setPrincipal(rs.getBoolean("st_principal"));
		bean.setTipoMail(getInteger(rs, "tp_mail"));
		return bean;
	}
	
	public static Mail get(Connection conn, Integer idMail)
	throws SQLException {
		Mail bean = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = SELECT + "WHERE id_mail = ?";
		int idx = 1;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(idx++, idMail);
			
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

	public static synchronized int insert(Connection conn, Mail bean)
	throws SQLException {
		int rows = -1;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int idx = 1;
		
		try {
			ps = conn.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(idx++, bean.getMail());
			ps.setBoolean(idx++, bean.isPrincipal());
			ps.setInt(idx++, bean.getTipoMail());
			
			rows = ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			
			if(rs.next()) {
				bean.setIdMail(rs.getInt(1));
			}
			
		} finally {
                        close(rs);
			close(ps);
		}
		return rows;
	}

	public static synchronized int delete(Connection conn, Integer idMail)
	throws SQLException {
		int rows = -1;
		PreparedStatement ps = null;
		int idx = 1;
		
		try {
			ps = conn.prepareStatement(DELETE);
			ps.setInt(idx++, idMail);
			
			rows = ps.executeUpdate();
		} finally {
			close(ps);
		}
		
		return rows;
	}
}
