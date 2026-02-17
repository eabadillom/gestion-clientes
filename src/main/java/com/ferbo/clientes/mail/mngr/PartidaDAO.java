package com.ferbo.clientes.mail.mngr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.mail.beans.Partida;

public class PartidaDAO extends DAO {
	private static Logger log = LogManager.getLogger(PartidaDAO.class);
	
	public static Partida[] getAll(Connection conn, int folio)
	throws SQLException{
		Partida[]          partidas   = null;
		Partida            partida    = null;
		PreparedStatement  psSelect   = null;
		ResultSet          rsSelect   = null;
		String             sqlSelect  = null;
		ArrayList<Partida> alPartidas = null;
		
		try {
			sqlSelect = "SELECT partida_cve, camara_cve, folio, peso_total, cantidad_total, unidad_de_producto_cve, "
				+ "cantidad_de_cobro, unidad_de_cobro, partida_seq, valorMercancia, rendimiento, no_tarimas FROM partida "
				+ "WHERE folio = ? AND unidad_de_producto_cve > 0";
			psSelect = conn.prepareStatement(sqlSelect);
			psSelect.setInt(1, folio);
			
			log.debug("Preparando SELECT para la tabla PARTIDA...");
			rsSelect = psSelect.executeQuery();
			
			alPartidas = new ArrayList<Partida>();
			
			while(rsSelect.next()){
				partida = new Partida();
				partida.setIdPartida(rsSelect.getInt("partida_cve"));
				partida.setIdCamara(rsSelect.getInt("camara_cve"));
				partida.setFolio(rsSelect.getInt("folio"));
				partida.setPesoTotal(rsSelect.getBigDecimal("peso_total"));
				partida.setCantidadTotal(rsSelect.getInt("cantidad_total"));
				partida.setIdUnidadProducto(rsSelect.getInt("unidad_de_producto_cve"));
				partida.setCantidadCobro(rsSelect.getBigDecimal("cantidad_de_cobro"));
				partida.setUnidadCobro(rsSelect.getInt("unidad_de_cobro"));
				partida.setPartidaSeq(rsSelect.getInt("partida_seq"));
				partida.setValorMercancia(rsSelect.getBigDecimal("valorMercancia"));
				partida.setRendimiento(rsSelect.getBigDecimal("rendimiento"));
				partida.setNoTarimas(rsSelect.getBigDecimal("no_tarimas"));
				
				alPartidas.add(partida);
			}
			partidas = new Partida[alPartidas.size()];
			partidas = (Partida[]) alPartidas.toArray(partidas);
			log.debug("La sentencia SELECT para la tabla PARTIDA concluyo satisfactoriamente...");
		} finally {
			close(psSelect);
			close(rsSelect);
		}
		
		return partidas;
	}
	
	public static Partida get(Connection conn, int idPartida)
	throws SQLException {
		Partida           partida = null;
		PreparedStatement psSelect = null;
		ResultSet         rsSelect = null;
		String            sqlSelect = null;
		
		try{
			sqlSelect = "SELECT partida_cve, camara_cve, folio, peso_total, cantidad_total, "
				+ "unidad_de_producto_cve, cantidad_de_cobro, unidad_de_cobro, partida_seq, "
				+ "valorMercancia, rendimiento, no_tarimas FROM partida WHERE partida_cve = ? ";
			
			psSelect = conn.prepareStatement(sqlSelect);
			psSelect.setInt(1, idPartida);
			log.debug("Preparando SELECT para la tabla PARTIDA.");
			log.debug(sqlSelect + " [ " + idPartida + " ]");
			
			rsSelect = psSelect.executeQuery();
			if(rsSelect.next()) {
				
				partida = new Partida();
				partida.setIdPartida(rsSelect.getInt("partida_cve"));
				partida.setIdCamara(rsSelect.getInt("camara_cve"));
				partida.setFolio(rsSelect.getInt("folio"));
				partida.setPesoTotal(rsSelect.getBigDecimal("peso_total"));
				partida.setCantidadTotal(rsSelect.getInt("cantidad_total"));
				partida.setIdUnidadProducto(rsSelect.getInt("unidad_de_producto_cve"));
				partida.setCantidadCobro(rsSelect.getBigDecimal("cantidad_de_cobro"));
				partida.setUnidadCobro(rsSelect.getInt("unidad_de_cobro"));
				partida.setPartidaSeq(rsSelect.getInt("partida_seq"));
				partida.setValorMercancia(rsSelect.getBigDecimal("valorMercancia"));
				partida.setRendimiento(rsSelect.getBigDecimal("rendimiento"));
				partida.setNoTarimas(rsSelect.getBigDecimal("no_tarimas"));
			}
			log.debug("La sentencia SELECT concluyo satisfactoriamente.");
		} finally {
			close(rsSelect);
			close(psSelect);
		}
		
		return partida;
	}
	
	public static synchronized int update(Connection conn, Partida partida)
	throws SQLException {
		int rows = -1;
		PreparedStatement ps = null;
		String sql = null;
		
		try {
			sql = "UPDATE partida SET"
					+ " CAMARA_CVE = ?,"
					+ " PESO_TOTAL = ?,"
					+ " CANTIDAD_TOTAL = ?,"
					+ " UNIDAD_DE_PRODUCTO_CVE = ?, "
					+ " cantidad_de_cobro = ?,"
					+ " unidad_de_cobro = ?,"
					+ " partida_seq = ?,"
					+ " valorMercancia = ?, "
					+ " rendimiento = ?,"
					+ " no_tarimas = ?"
					+ " WHERE"
					+ " PARTIDA_CVE = ? ";
			
			ps = conn.prepareStatement(sql);
			
			ps.setInt(1, partida.getIdCamara());
			ps.setBigDecimal(2, partida.getPesoTotal());
			ps.setInt(3, partida.getCantidadTotal());
			ps.setInt(4, partida.getIdUnidadProducto());
			ps.setBigDecimal(5, partida.getCantidadCobro());
			ps.setInt(6, partida.getUnidadCobro());
			ps.setInt(7, partida.getPartidaSeq());
			ps.setBigDecimal(8, partida.getValorMercancia());
			ps.setBigDecimal(9, partida.getRendimiento());
			ps.setBigDecimal(10, partida.getNoTarimas());
			ps.setInt(11, partida.getIdPartida());
			
			rows = ps.executeUpdate();
			
		} finally {
			close(ps);
		}
		
		return rows;
	}
	
	public static synchronized int[] update(Connection conn, Partida[] partidas)
	throws SQLException {
		int[] rows = null;
		PreparedStatement ps = null;
		String sql = null;
		int batchSize = 1000;
		int count = 0;
		
		try {
			sql = "UPDATE partida SET "
					+ " CAMARA_CVE = ?, "
					+ " PESO_TOTAL = ?, "
					+ " CANTIDAD_TOTAL = ?, "
					+ " UNIDAD_DE_PRODUCTO_CVE = ?, "
					+ " cantidad_de_cobro = ?, "
					+ " unidad_de_cobro = ?, "
					+ " partida_seq = ?, "
					+ " valorMercancia = ?, "
					+ " rendimiento = ?, "
					+ " no_tarimas = ? "
					+ " WHERE PARTIDA_CVE = ? ";
			
			ps = conn.prepareStatement(sql);
			
			for(Partida partida: partidas) {
				
				ps.setInt(1, partida.getIdCamara());
				ps.setBigDecimal(2, partida.getPesoTotal());
				ps.setInt(3, partida.getCantidadTotal());
				ps.setInt(4, partida.getIdUnidadProducto());
				ps.setBigDecimal(5, partida.getCantidadCobro());
				ps.setInt(6, partida.getUnidadCobro());
				ps.setInt(7, partida.getPartidaSeq());
				ps.setBigDecimal(8, partida.getValorMercancia());
				ps.setBigDecimal(9, partida.getRendimiento());
				ps.setBigDecimal(10, partida.getNoTarimas());
				ps.setInt(11, partida.getIdPartida());
				
				ps.addBatch();
				
				if(++count % batchSize == 0) {
					ps.executeBatch();
					count = 0;
				}
			}
			
			rows = ps.executeBatch();
			
		} finally {
			close(ps);
		}
		
		return rows;
	}
	
	public static synchronized int delete(Connection conn, int idPartida)
	throws SQLException {
		int rows = -1;
		PreparedStatement ps = null;
		String sql = null;
		
		try {
			sql = "DELETE FROM partida WHERE partida_cve = ? ";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idPartida);
			
			rows = ps.executeUpdate();
			
		} finally {
			close(ps);
		}
		
		return rows;
	}
}