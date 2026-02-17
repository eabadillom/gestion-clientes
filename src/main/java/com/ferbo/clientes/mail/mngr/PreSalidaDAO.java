package com.ferbo.clientes.mail.mngr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ferbo.clientes.mail.beans.PreSalida;

public class PreSalidaDAO extends DAO {
	private static final String SELECT = "select id_pre_salida, cd_folio_salida, st_estado, fh_salida, tm_salida, nb_placa_tte, nb_operador_tte, partida_cve, folio, nu_cantidad from pre_salida ";
	private static final String UPDATE = "UPDATE pre_salida SET id_pre_salida = ?, cd_folio_salida = ?, st_estado = ?, fh_salida = ?, tm_salida = ?, nb_placa_tte = ?, nb_operador_tte = ?, partida_cve = ?, folio = ?, nu_cantidad = ? WHERE id_pre_salida = ? ";
	
	private static synchronized PreSalida getBean(ResultSet rs)
	throws SQLException {
		PreSalida bean = new PreSalida();
		bean.setIdPreSalida(rs.getInt("id_pre_salida"));
		bean.setFolioSalida(rs.getString("cd_folio_salida"));
		bean.setEstado(rs.getString("st_estado"));
		bean.setFechaSalida(rs.getDate("fh_salida"));
		bean.setHoraSalida(rs.getTimestamp("tm_salida"));
		bean.setPlacaTransporte(rs.getString("nb_placa_tte"));
		bean.setNombreTransportista(rs.getString("nb_operador_tte"));
		bean.setIdPartida(rs.getInt("partida_cve"));
		bean.setFolio(rs.getInt("folio"));
		bean.setCantidad(rs.getInt("nu_cantidad"));
		
		return bean;
	}
	
	public static PreSalida[] get(Connection conn, String folio)
	throws SQLException {
		PreSalida[] beans = null;
		PreSalida bean = null;
		List<PreSalida> alBeans = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		int idx = 1;
		
		try {
			sql = SELECT + " WHERE cd_folio_salida = ? ";
			ps = conn.prepareStatement(sql);
			ps.setString(idx++, folio);
			
			rs = ps.executeQuery();
			alBeans = new ArrayList<PreSalida>();
			while(rs.next()) {
				bean = getBean(rs);
				alBeans.add(bean);
			}
			beans = new PreSalida[alBeans.size()];
			beans = (PreSalida[]) alBeans.toArray(beans);
			
		} finally {
			close(rs);
			close(ps);
		}
		
		return beans;
	}
	
	public static synchronized int update(Connection conn, PreSalida bean)
	throws SQLException {
		int rows = -1;
		PreparedStatement ps = null;
		int idx = 1;
		
		try {
			ps = conn.prepareStatement(UPDATE);
			ps.setInt(idx++, bean.getIdPreSalida());
			ps.setString(idx++, bean.getFolioSalida());
			ps.setString(idx++, bean.getEstado());
			ps.setDate(idx++, getSqlDate(bean.getFechaSalida()));
			ps.setTimestamp(idx++, new java.sql.Timestamp(bean.getHoraSalida().getTime()));
			ps.setString(idx++, bean.getPlataTransporte());
			ps.setString(idx++, bean.getNombreTransportista());
			ps.setInt(idx++, bean.getIdPartida());
			ps.setInt(idx++, bean.getFolio());
			ps.setInt(idx++, bean.getCantidad());
			ps.setInt(idx++, bean.getIdPreSalida());
			
			rows = ps.executeUpdate();
			
		} finally {
			close(ps);
		}
		
		return rows;
	}
}
