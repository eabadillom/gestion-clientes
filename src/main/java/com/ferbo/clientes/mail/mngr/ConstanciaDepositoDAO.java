package com.ferbo.clientes.mail.mngr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.mail.beans.ConstanciaDeposito;
import com.ferbo.clientes.util.DateUtils;
import com.ferbo.clientes.util.DateUtilsException;

public class ConstanciaDepositoDAO extends DAO {
	
	private static Logger log = LogManager.getLogger(ConstanciaDepositoDAO.class);
	
	public static ConstanciaDeposito getConstanciaDeposito(Connection conn, int idConstancia)
	throws SQLException {
		ConstanciaDeposito cdd = null;
		PreparedStatement psSelect = null;
		ResultSet rsSelect = null;
		String sqlSelect = null;
		
		try {
			sqlSelect = "SELECT cdd.folio, cdd.cte_cve, cdd.fecha_ingreso, cdd.nombre_transportista, "
				+ "cdd.placas_transporte, cdd.observaciones, cdd.folio_cliente, cdd.valor_declarado, "
				+ "cdd.status, cdd.aviso_cve, cdd.temperatura FROM constancia_de_deposito cdd "
				+ " WHERE cdd.folio = ? ";
			psSelect = conn.prepareStatement(sqlSelect);
			psSelect.setInt(1, idConstancia);
			
			rsSelect = psSelect.executeQuery();
			if(rsSelect.next()){
				cdd = new ConstanciaDeposito();
				cdd.setIdConstancia(rsSelect.getInt("folio"));
				cdd.setIdCliente(rsSelect.getInt("cte_cve"));
				cdd.setFechaIngreso(new Date(rsSelect.getDate("fecha_ingreso").getTime()));
				cdd.setNombreTransportista(rsSelect.getString("nombre_transportista"));
				cdd.setPlacasTransporte(rsSelect.getString("placas_transporte"));
				cdd.setObservaciones(rsSelect.getString("observaciones"));
				cdd.setFolioCliente(rsSelect.getString("folio_cliente"));
				cdd.setValorDeclarado(rsSelect.getBigDecimal("valor_declarado"));
				cdd.setStatus(rsSelect.getInt("status"));
				cdd.setAvisoCve(rsSelect.getInt("aviso_cve"));
				cdd.setTemperatura(rsSelect.getString("temperatura"));
			}
		} finally {
			close(psSelect);
			close(rsSelect);
		}
		
		return cdd;
	}

	public static ConstanciaDeposito getConstanciaDeposito(Connection conn, String folio)
	throws SQLException {
		ConstanciaDeposito cdd = null;
		PreparedStatement psSelect = null;
		ResultSet rsSelect = null;
		String sqlSelect = null;
		
		try {
			sqlSelect = "SELECT cdd.folio, cdd.cte_cve, cdd.fecha_ingreso, cdd.nombre_transportista, "
				+ "cdd.placas_transporte, cdd.observaciones, cdd.folio_cliente, cdd.valor_declarado, "
				+ "cdd.status, cdd.aviso_cve, cdd.temperatura FROM constancia_de_deposito cdd "
				+ " WHERE cdd.folio_cliente = ? ";
			psSelect = conn.prepareStatement(sqlSelect);
			psSelect.setString(1, folio);
			
			rsSelect = psSelect.executeQuery();
			if(rsSelect.next()){
				cdd = new ConstanciaDeposito();
				cdd.setIdConstancia(rsSelect.getInt("folio"));
				cdd.setIdCliente(rsSelect.getInt("cte_cve"));
				cdd.setFechaIngreso(new Date(rsSelect.getDate("fecha_ingreso").getTime()));
				cdd.setNombreTransportista(rsSelect.getString("nombre_transportista"));
				cdd.setPlacasTransporte(rsSelect.getString("placas_transporte"));
				cdd.setObservaciones(rsSelect.getString("observaciones"));
				cdd.setFolioCliente(rsSelect.getString("folio_cliente"));
				cdd.setValorDeclarado(rsSelect.getBigDecimal("valor_declarado"));
				cdd.setStatus(rsSelect.getInt("status"));
				cdd.setAvisoCve(rsSelect.getInt("aviso_cve"));
				cdd.setTemperatura(rsSelect.getString("temperatura"));
			}
		} finally {
			close(psSelect);
			close(rsSelect);
		}
		
		return cdd;
	}
	
	public static ConstanciaDeposito[] getAll(Connection conn, int cteCve, Date fechaInicial, Date fechaFinal)
	throws SQLException {
		ConstanciaDeposito[] constancias   = null;
		ConstanciaDeposito   cdd           = null;
		PreparedStatement    psSelect      = null;
		ResultSet            rsSelect      = null;
		String               sqlSelect     = null;
		ArrayList<ConstanciaDeposito>            alConstancias = null;
		Date                 fechaFinalMod = null;
		
		try{
			sqlSelect = "SELECT cdd.folio, cdd.cte_cve, cdd.fecha_ingreso, cdd.nombre_transportista, "
				+ "cdd.placas_transporte, cdd.observaciones, cdd.folio_cliente, cdd.valor_declarado, "
				+ "cdd.status, cdd.aviso_cve, cdd.temperatura FROM constancia_de_deposito cdd "
				+ "WHERE cdd.cte_cve = ? AND cdd.fecha_ingreso BETWEEN ? AND ? and cdd.aviso_cve IS NOT NULL";
			psSelect = conn.prepareStatement(sqlSelect);
			psSelect.setInt(1, cteCve);
			psSelect.setDate(2, new java.sql.Date(fechaInicial.getTime()));
			fechaFinalMod = new Date(fechaFinal.getTime());
			DateUtils.setTime(fechaFinalMod, 11, 59, 59, 999);
			psSelect.setDate(3, new java.sql.Date(fechaFinalMod.getTime()));
			
			log.debug("Preparando SELECT para la tabla CONSTANCIA_DE_DEPOSITO...");
			if(log.isDebugEnabled()){
				try {
					log.debug("Fecha de inicio de periodo: " + DateUtils.getString(fechaInicial, DateUtils.FORMATO_YYYY_MM_DD_HH_MM_SS));
					log.debug("Fecha de fin de periodo: " + DateUtils.getString(fechaFinalMod, DateUtils.FORMATO_YYYY_MM_DD_HH_MM_SS));
				} catch (DateUtilsException ex) {
					log.error(ex);
				}
			}
			rsSelect = psSelect.executeQuery();
			
			alConstancias = new  ArrayList<ConstanciaDeposito>();
			
			while(rsSelect.next()) {
				cdd = new ConstanciaDeposito();
				cdd.setIdConstancia(rsSelect.getInt("folio"));
				cdd.setIdCliente(rsSelect.getInt("cte_cve"));
				cdd.setFechaIngreso(new Date(rsSelect.getDate("fecha_ingreso").getTime()));
				cdd.setNombreTransportista(rsSelect.getString("nombre_transportista"));
				cdd.setPlacasTransporte(rsSelect.getString("placas_transporte"));
				cdd.setObservaciones(rsSelect.getString("observaciones"));
				cdd.setFolioCliente(rsSelect.getString("folio_cliente"));
				cdd.setValorDeclarado(rsSelect.getBigDecimal("valor_declarado"));
				cdd.setStatus(rsSelect.getInt("status"));
				cdd.setAvisoCve(rsSelect.getInt("aviso_cve"));
				cdd.setTemperatura(rsSelect.getString("temperatura"));
				
				alConstancias.add(cdd);
			}
			
			constancias = new ConstanciaDeposito[alConstancias.size()];
			constancias = (ConstanciaDeposito[])alConstancias.toArray(constancias);
			log.debug("Constancias leidas: " + constancias.length);
			
		} finally {
			close(psSelect);
			close(rsSelect);
		}
		
		return constancias;
	}
	
	
	/**Obtiene todas aquellas constancias de depósito que son estrictamente anteriores a la fecha de corte dada.
	 * @param conn
	 * @param cteCve
	 * @param fechaCorte
	 * @return
	 * @throws SQLException
	 */
	public static ConstanciaDeposito[] getAll(Connection conn, int cteCve, Date fechaCorte)
	throws SQLException {
		ConstanciaDeposito[] constancias   = null;
		ConstanciaDeposito   cdd           = null;
		PreparedStatement    psSelect      = null;
		ResultSet            rsSelect      = null;
		String               sqlSelect     = null;
		ArrayList<ConstanciaDeposito>            alConstancias = null;
		
		try{
			sqlSelect = "SELECT cdd.folio, cdd.cte_cve, cdd.fecha_ingreso, cdd.nombre_transportista, "
				+ "cdd.placas_transporte, cdd.observaciones, cdd.folio_cliente, cdd.valor_declarado, "
				+ "cdd.status, cdd.aviso_cve, cdd.temperatura FROM constancia_de_deposito cdd "
				+ "WHERE cdd.cte_cve = ? AND cdd.fecha_ingreso < ? and cdd.aviso_cve IS NOT NULL";
			psSelect = conn.prepareStatement(sqlSelect);
			psSelect.setInt(1, cteCve);
			psSelect.setDate(2, new java.sql.Date(fechaCorte.getTime()));
			log.debug("Preparando SELECT para CONSTANCIA_DE_DEPOSITO...");
			log.debug(sqlSelect);
			rsSelect = psSelect.executeQuery();
			
			alConstancias = new  ArrayList<ConstanciaDeposito>();
			
			while(rsSelect.next()) {
				cdd = new ConstanciaDeposito();
				cdd.setIdConstancia(rsSelect.getInt("folio"));
				cdd.setIdCliente(rsSelect.getInt("cte_cve"));
				cdd.setFechaIngreso(new Date(rsSelect.getDate("fecha_ingreso").getTime()));
				cdd.setNombreTransportista(rsSelect.getString("nombre_transportista"));
				cdd.setPlacasTransporte(rsSelect.getString("placas_transporte"));
				cdd.setObservaciones(rsSelect.getString("observaciones"));
				cdd.setFolioCliente(rsSelect.getString("folio_cliente"));
				cdd.setValorDeclarado(rsSelect.getBigDecimal("valor_declarado"));
				cdd.setStatus(rsSelect.getInt("status"));
				cdd.setAvisoCve(rsSelect.getInt("aviso_cve"));
				cdd.setTemperatura(rsSelect.getString("temperatura"));
				
				alConstancias.add(cdd);
			}
			
			constancias = new ConstanciaDeposito[alConstancias.size()];
			constancias = (ConstanciaDeposito[])alConstancias.toArray(constancias);
			log.debug("Constancias leidas: " + constancias.length);
			
		} finally {
			close(psSelect);
			close(rsSelect);
		}
		
		return constancias;
	}
	
	public static synchronized int update(Connection conn, ConstanciaDeposito constancia)
	throws SQLException {
		int rows = -1;
		PreparedStatement ps = null;
		String sql = null;
		
		try {
			sql = "UPDATE constancia_de_deposito set " + 
					"    FECHA_INGRESO = ?, " + 
					"    NOMBRE_TRANSPORTISTA = ?, " + 
					"    PLACAS_TRANSPORTE = ?, " + 
					"    OBSERVACIONES = ?, " + 
					"    valor_declarado = ?, " + 
					"    status = ?, " + 
					"    aviso_cve = ?, " + 
					"    temperatura = ? " + 
					"WHERE FOLIO = ? ";
			ps = conn.prepareStatement(sql);
			ps.setDate(1, new java.sql.Date(constancia.getFechaIngreso().getTime()));
			ps.setString(2, constancia.getNombreTransportista());
			ps.setString(3, constancia.getPlacasTransporte());
			ps.setString(4, constancia.getObservaciones());
			ps.setBigDecimal(5, constancia.getValorDecimal());
			ps.setInt(6, constancia.getStatus());
			ps.setInt(7, constancia.getAvisoCve());
			ps.setString(8, constancia.getTemperatura());
			ps.setInt(9, constancia.getIdConstancia());
			
			rows = ps.executeUpdate();
			
		} finally {
			close(ps);
		}
		
		
		return rows;
	}
	
	public static ConstanciaDeposito[] getConstanciasConSaldo(Connection conn, int cteCve, Date fechaCorte)
	throws SQLException {
		ConstanciaDeposito []         constancias = null;
		ConstanciaDeposito            cdd           = null;
		PreparedStatement             psSelect      = null;
		ResultSet                     rsSelect      = null;
		String                        sqlSelect     = null;
		ArrayList<ConstanciaDeposito> alConstancias = null;
		
		try{
			sqlSelect = "SELECT folio, folio_cliente, cte_cve, fecha_ingreso, nombre_transportista, placas_transporte, "
				+ "observaciones, valor_declarado, status, aviso_cve, temperatura FROM (SELECT cdd.folio, cdd.folio_cliente, "
				+ "cdd.cte_cve, cdd.fecha_ingreso, cdd.nombre_transportista, cdd.placas_transporte, cdd.observaciones, "
				+ "cdd.valor_declarado, cdd.status, cdd.aviso_cve, cdd.temperatura, (p.peso_total - ISNULL(s.peso, 0) )as peso, "
				+ "'Kilogramo' as unidad_peso, (p.cantidad_total - ISNULL(s.cantidad, 0) ) as cantidad, "
				+ "udm.unidad_de_manejo_ds as unidad_manejo, prd.producto_ds FROM constancia_de_deposito cdd "
				+ "INNER JOIN partida p ON p.folio = cdd.folio  INNER JOIN detalle_partida dp ON p.partida_cve = dp.partida_cve "
                + "AND det_part_cve = 1 INNER JOIN unidad_de_producto udp "
				+ "ON p.unidad_de_producto_cve = udp.unidad_de_producto_cve INNER JOIN producto prd "
				+ "ON udp.producto_cve = prd.producto_cve INNER JOIN unidad_de_manejo udm "
				+ "ON udp.unidad_de_manejo_cve = udm.unidad_de_manejo_cve INNER JOIN aviso a ON cdd.aviso_cve = a.aviso_cve "
				+ "LEFT OUTER JOIN (SELECT dcs.partida_cve, MAX(cs.fecha) as fecha_ult_sal, SUM(dcs.peso) as peso, "
				+ "'Kilogramo' as unidad_peso, SUM(dcs.cantidad) AS cantidad, dcs.unidad as unidad_manejo "
				+ "FROM constancia_salida cs INNER JOIN detalle_constancia_salida dcs ON cs.id = dcs.constancia_cve "
				+ "WHERE cs.status = 1 AND cs.cliente_cve = ? AND cs.fecha <= ? GROUP BY dcs.partida_cve, dcs.unidad "
				+ ") s ON p.partida_cve = s.partida_cve WHERE cdd.aviso_cve IS NOT NULL AND peso > 0.0 AND cantidad > 0 "
				+ "AND cdd.cte_cve = ? AND cdd.fecha_ingreso <= ? ) T GROUP BY folio, folio_cliente, cte_cve, "
				+ "fecha_ingreso, nombre_transportista, placas_transporte, observaciones, valor_declarado, "
				+ "status, aviso_cve, temperatura ORDER BY fecha_ingreso ASC"
				;
			
			psSelect = conn.prepareStatement(sqlSelect);
			psSelect.setInt(1, cteCve);
			psSelect.setDate(2, new java.sql.Date(fechaCorte.getTime()));
			psSelect.setInt(3, cteCve);
			psSelect.setDate(4, new java.sql.Date(fechaCorte.getTime()));
			log.debug("Preparando SELECT para CONSTANCIA_DE_DEPOSITO...");
			log.debug(sqlSelect);
			log.trace("cteCve: " + cteCve);
			log.trace("fechaCorte: " + fechaCorte); 
			rsSelect = psSelect.executeQuery();
			
			alConstancias = new  ArrayList<ConstanciaDeposito>();
			
			while(rsSelect.next()) {
				cdd = new ConstanciaDeposito();
				cdd.setIdConstancia(rsSelect.getInt("folio"));
				cdd.setIdCliente(rsSelect.getInt("cte_cve"));
				cdd.setFechaIngreso(new Date(rsSelect.getDate("fecha_ingreso").getTime()));
				cdd.setNombreTransportista(rsSelect.getString("nombre_transportista"));
				cdd.setPlacasTransporte(rsSelect.getString("placas_transporte"));
				cdd.setObservaciones(rsSelect.getString("observaciones"));
				cdd.setFolioCliente(rsSelect.getString("folio_cliente"));
				cdd.setValorDeclarado(rsSelect.getBigDecimal("valor_declarado"));
				cdd.setStatus(rsSelect.getInt("status"));
				cdd.setAvisoCve(rsSelect.getInt("aviso_cve"));
				cdd.setTemperatura(rsSelect.getString("temperatura"));
				
				alConstancias.add(cdd);
			}
			
			constancias = new ConstanciaDeposito[alConstancias.size()];
			constancias = (ConstanciaDeposito[])alConstancias.toArray(constancias);
			log.debug("Constancias leidas: " + constancias.length);
			
		} finally {
			close(psSelect);
			close(rsSelect);
		}
		
		
		
		
		return constancias;
	}
	
	public static ConstanciaDeposito[] getConstanciasSaldoNoCero(Connection conn, int cteCve, Date fechaCorte)
	throws SQLException {
		ConstanciaDeposito []         constancias = null;
		ConstanciaDeposito            cdd           = null;
		PreparedStatement             psSelect      = null;
		ResultSet                     rsSelect      = null;
		String                        sqlSelect     = null;
		ArrayList<ConstanciaDeposito> alConstancias = null;
		
		try{
			sqlSelect = "SELECT folio, folio_cliente, cte_cve, fecha_ingreso, nombre_transportista, placas_transporte, "
				+ "observaciones, valor_declarado, status, aviso_cve, temperatura FROM (SELECT cdd.folio, cdd.folio_cliente, "
				+ "cdd.cte_cve, cdd.fecha_ingreso, cdd.nombre_transportista, cdd.placas_transporte, cdd.observaciones, "
				+ "cdd.valor_declarado, cdd.status, cdd.aviso_cve, cdd.temperatura, (p.peso_total - ISNULL(s.peso, 0) )as peso, "
				+ "'Kilogramo' as unidad_peso, (p.cantidad_total - ISNULL(s.cantidad, 0) ) as cantidad, "
				+ "udm.unidad_de_manejo_ds as unidad_manejo, prd.producto_ds FROM constancia_de_deposito cdd "
				+ "INNER JOIN partida p ON p.folio = cdd.folio  INNER JOIN detalle_partida dp ON p.partida_cve = dp.partida_cve "
                + "AND det_part_cve = 1 INNER JOIN unidad_de_producto udp "
				+ "ON p.unidad_de_producto_cve = udp.unidad_de_producto_cve INNER JOIN producto prd "
				+ "ON udp.producto_cve = prd.producto_cve INNER JOIN unidad_de_manejo udm "
				+ "ON udp.unidad_de_manejo_cve = udm.unidad_de_manejo_cve INNER JOIN aviso a ON cdd.aviso_cve = a.aviso_cve "
				+ "LEFT OUTER JOIN (SELECT dcs.partida_cve, MAX(cs.fecha) as fecha_ult_sal, SUM(dcs.peso) as peso, "
				+ "'Kilogramo' as unidad_peso, SUM(dcs.cantidad) AS cantidad, dcs.unidad as unidad_manejo "
				+ "FROM constancia_salida cs INNER JOIN detalle_constancia_salida dcs ON cs.id = dcs.constancia_cve "
				+ "WHERE cs.status = 1 AND cs.cliente_cve = ? AND cs.fecha <= ? GROUP BY dcs.partida_cve, dcs.unidad "
				+ ") s ON p.partida_cve = s.partida_cve WHERE cdd.aviso_cve IS NOT NULL AND (peso != 0.0 OR cantidad != 0) "
				+ "AND cdd.cte_cve = ? AND cdd.fecha_ingreso <= ? ) T GROUP BY folio, folio_cliente, cte_cve, "
				+ "fecha_ingreso, nombre_transportista, placas_transporte, observaciones, valor_declarado, "
				+ "status, aviso_cve, temperatura ORDER BY fecha_ingreso ASC"
				;
			
			psSelect = conn.prepareStatement(sqlSelect);
			psSelect.setInt(1, cteCve);
			psSelect.setDate(2, new java.sql.Date(fechaCorte.getTime()));
			psSelect.setInt(3, cteCve);
			psSelect.setDate(4, new java.sql.Date(fechaCorte.getTime()));
			log.debug("Preparando SELECT para CONSTANCIA_DE_DEPOSITO...");
			log.debug(sqlSelect);
			log.trace("cteCve: " + cteCve);
			log.trace("fechaCorte: " + fechaCorte); 
			rsSelect = psSelect.executeQuery();
			
			alConstancias = new  ArrayList<ConstanciaDeposito>();
			
			while(rsSelect.next()) {
				cdd = new ConstanciaDeposito();
				cdd.setIdConstancia(rsSelect.getInt("folio"));
				cdd.setIdCliente(rsSelect.getInt("cte_cve"));
				cdd.setFechaIngreso(new Date(rsSelect.getDate("fecha_ingreso").getTime()));
				cdd.setNombreTransportista(rsSelect.getString("nombre_transportista"));
				cdd.setPlacasTransporte(rsSelect.getString("placas_transporte"));
				cdd.setObservaciones(rsSelect.getString("observaciones"));
				cdd.setFolioCliente(rsSelect.getString("folio_cliente"));
				cdd.setValorDeclarado(rsSelect.getBigDecimal("valor_declarado"));
				cdd.setStatus(rsSelect.getInt("status"));
				cdd.setAvisoCve(rsSelect.getInt("aviso_cve"));
				cdd.setTemperatura(rsSelect.getString("temperatura"));
				
				alConstancias.add(cdd);
			}
			
			constancias = new ConstanciaDeposito[alConstancias.size()];
			constancias = (ConstanciaDeposito[])alConstancias.toArray(constancias);
			log.debug("Constancias leidas: " + constancias.length);
			
		} finally {
			close(psSelect);
			close(rsSelect);
		}
		
		return constancias;
	}
	
	public static ConstanciaDeposito isConstanciaConSalidas(Connection conn, int idConstancia)
	throws SQLException {
		ConstanciaDeposito cdd = null;
		PreparedStatement  ps  = null;
		ResultSet          rs  = null;
		String             sql = null;
		
		try {
			sql = "select " + 
				"    cdd.FOLIO, " + 
				"    cdd.CTE_CVE, " + 
				"    cdd.FECHA_INGRESO, " + 
				"    cdd.NOMBRE_TRANSPORTISTA, " + 
				"    cdd.PLACAS_TRANSPORTE, " + 
				"    cdd.OBSERVACIONES, " + 
				"    cdd.folio_cliente, " + 
				"    cdd.valor_declarado, " + 
				"    cdd.status, " + 
				"    cdd.aviso_cve, " + 
				"    cdd.temperatura " + 
				"from constancia_de_deposito cdd " + 
				"inner join partida p on p.FOLIO = cdd.FOLIO " + 
				"inner join detalle_constancia_salida dcs on p.PARTIDA_CVE = dcs.PARTIDA_CVE " + 
				"inner join constancia_salida cs on cs.id = dcs.CONSTANCIA_CVE " +
				//"where cs.STATUS = 1 AND cdd.folio = ? "
				"where cdd.folio = ? "
				;
			
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idConstancia);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				cdd = new ConstanciaDeposito();
				cdd.setIdConstancia(rs.getInt("folio"));
				cdd.setIdCliente(rs.getInt("cte_cve"));
				cdd.setFechaIngreso(new Date(rs.getDate("fecha_ingreso").getTime()));
				cdd.setNombreTransportista(rs.getString("nombre_transportista"));
				cdd.setPlacasTransporte(rs.getString("placas_transporte"));
				cdd.setObservaciones(rs.getString("observaciones"));
				cdd.setFolioCliente(rs.getString("folio_cliente"));
				cdd.setValorDeclarado(rs.getBigDecimal("valor_declarado"));
				cdd.setStatus(rs.getInt("status"));
				cdd.setAvisoCve(rs.getInt("aviso_cve"));
				cdd.setTemperatura(rs.getString("temperatura"));
				
			}
			
		} finally {
			close(ps);
			close(rs);
		}
		
		return cdd;
	}
}