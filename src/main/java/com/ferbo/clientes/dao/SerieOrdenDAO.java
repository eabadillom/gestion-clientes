package com.ferbo.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ferbo.clientes.manager.DAO;
import com.ferbo.clientes.model.SerieOrden;

public class SerieOrdenDAO extends DAO {

	private static final String SELECT = "SELECT id_cliente, tp_serie, id_planta, nu_serie FROM serie_orden ";

	public SerieOrden getBean(ResultSet rs) throws SQLException {
		SerieOrden bean = new SerieOrden();

		bean.setIdCliente(getInteger(rs, "id_cliente"));
		bean.setTipoSerie(rs.getString("tp_serie"));
		bean.setIdPlanta(getInteger(rs, "id_planta"));
		bean.setNumero(getInteger(rs, "nu_serie"));

		return bean;
	}

	public SerieOrden get(Connection conn, Integer idCliente, Integer idPlanta, String tipoSerie) throws SQLException {
		SerieOrden bean = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		int idx = 1;

		try {
			
			sql = SELECT + " WHERE id_cliente = ? AND tp_serie = ? AND id_planta = ? ";
			ps = conn.prepareStatement(sql);
			setInteger(ps, idx++, idCliente);
			ps.setString(idx++, tipoSerie);
			setInteger(ps, idx++, idPlanta);

			rs = ps.executeQuery();

			if (rs.next()) {
				bean = this.getBean(rs);
			}
			
		} finally {
			close(ps);
			close(rs);
		}

		return bean;
	}

	public int insert(Connection conn, SerieOrden serie) throws SQLException {
		int rows = -1;
		PreparedStatement ps = null;
		int idx = 1;
		String sql = null;
		
		try {
			sql = "INSERT INTO serie_orden (id_cliente, tp_serie, id_planta, nu_serie) VALUES(?, ?, ?, ?)";
			ps = conn.prepareStatement(sql);
			setInteger(ps, idx++, serie.getIdCliente());
			ps.setString(idx++, serie.getTipoSerie());
			setInteger(ps, idx++, serie.getIdPlanta());
			setInteger(ps, idx++, serie.getNumero());
			
			rows = ps.executeUpdate();
			
		} finally {
			close(ps);
		}
		
		return rows;
	}

	public int update(Connection conn, SerieOrden serie) throws SQLException {
		int rows = -1;
		PreparedStatement ps = null;
		int idx = 1;
		String sql = null;
		
		try {
			sql = "UPDATE serie_orden SET nu_serie = ? WHERE id_cliente = ? AND tp_serie = ? AND id_planta = ? ";
			ps = conn.prepareStatement(sql);
			setInteger(ps, idx++, serie.getNumero());
			setInteger(ps, idx++, serie.getIdCliente());
			ps.setString(idx++, serie.getTipoSerie());
			setInteger(ps, idx++, serie.getIdPlanta());
			
			rows = ps.executeUpdate();
			
		} finally {
			close(ps);
		}
		
		return rows;
		
	}

}
