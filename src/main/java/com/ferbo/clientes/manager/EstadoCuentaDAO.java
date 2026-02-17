package com.ferbo.clientes.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.model.EstadoCuenta;

public class EstadoCuentaDAO extends DAO {
	
	private static Logger log = LogManager.getLogger(EstadoCuentaDAO.class);

	public static EstadoCuenta getModel(ResultSet rs) throws SQLException {
		
		EstadoCuenta esc = new EstadoCuenta();
		esc.setCteCve(rs.getInt("cte_cve"));
		esc.setNumeroCte(rs.getString("numero_cte"));
		esc.setNombreCliente(rs.getString("nombre_cliente"));
		esc.setNomSerie(rs.getString("nom_serie"));
		esc.setFolio(rs.getString("numero"));
		esc.setFecha(rs.getDate("fecha"));
		esc.setMovimiento(rs.getString("movimiento"));
		esc.setConcepto(rs.getString("concepto"));
		esc.setSaldoInicial(rs.getBigDecimal("saldo_inicial"));
		esc.setDebe(rs.getBigDecimal("debe"));
		esc.setHaber(rs.getBigDecimal("haber"));
		esc.setParcial(rs.getBigDecimal("parcial"));
		return esc;
		
	}
    public List<EstadoCuenta> listaEstadoCuenta(Connection conn, Date fecha, Integer clienteCve, Date fechaFin) throws SQLException {
    	PreparedStatement psSelect = null;
        ResultSet rsSelect = null;
        String sql = null;
        ArrayList<EstadoCuenta> listaEstadoCuenta= null;
        EstadoCuenta ec= null;
        int idx = 1;
        
        try {
			sql = "SELECT "
					+ " edc.cte_cve, edc.numero_cte, edc.nombre_cliente, edc.nom_serie, edc.numero, edc.fecha, edc.movimiento, edc.concepto,"
					+ " COALESCE(si.saldo_inicial, 0) as saldo_inicial, edc.debe, edc.haber, COALESCE (edc.debe,0) - COALESCE(edc.haber,0) AS parcial "
					+ "FROM ( "
					+ " SELECT "
					+ " cte.CTE_CVE AS cte_cve, cte.numero_cte, cte.CTE_NOMBRE as nombre_cliente,"
					+ " f.nom_serie, f.numero, f.fecha, 'Factura' as movimiento, 'Ventas del día' AS concepto,"
					+ " f.total as debe, null as haber "
					+ " FROM factura f "
					+ " INNER JOIN cliente cte ON f.cliente = cte.CTE_CVE "
					+ " WHERE f.status NOT IN (0,2)"
					+ " AND (f.cliente = ? )" //1
					+ " AND f.fecha BETWEEN ? AND ? "//2 y 3
					+ " UNION ALL"
					+ " SELECT "
					+ " cte.CTE_CVE AS cte_cve, cte.numero_cte, cte.CTE_NOMBRE as nombre_cliente, "
					+ " f.nom_serie, f.numero, p.fecha, 'Pago' as movimiento , 'Depósito del día' AS concepto, "
					+ " null as debe, p.monto  as haber "
					+ " FROM pago p "
					+ " INNER JOIN factura f ON f.id = p.factura "
					+ " INNER JOIN cliente cte ON f.cliente = cte.CTE_CVE "
					+ " WHERE p.tipo NOT IN (5) "
					+ " AND f.status NOT IN (0,2) "
					+ " AND (f.cliente = ? ) " //4
					+ " AND p.fecha BETWEEN ? AND ? " //5 y 6
					+ " UNION ALL "
					+ " SELECT "
					+ " cte.CTE_CVE AS cte_cve, cte.numero_cte, cte.CTE_NOMBRE as nombre_cliente, "
					+ " '' as nom_serie, nc.NUMERO, nc.FECHA, CONCAT('N.C. ', nc.NUMERO) AS movimiento, 'Nota de crédito' AS concepto, "
					+ " null as debe, nf.CANTIDAD as haber "
					+ " FROM nota_credito nc "
					+ "INNER JOIN nota_x_facturas nf ON nc.id = nf.NOTA "
					+ " INNER JOIN factura f ON nf.FACTURA = f.id "
					+ " INNER JOIN cliente cte ON f.cliente = cte.CTE_CVE "
					+ " WHERE nc.STATUS NOT IN (0,2) "
					+ " AND (f.cliente = ? ) "//7
					+ " AND nc.fecha BETWEEN ? AND ?  " // 8 y 9
					+ " ) edc "
					+ " LEFT OUTER JOIN ( "
					+ " SELECT saldoInicial.cliente, sum(saldo_inicial) as saldo_inicial FROM ( "
					+ " SELECT f.cliente, (f.total - COALESCE(p.monto, 0)) as saldo_inicial "
					+ " FROM factura f "
					+ " INNER JOIN status_factura sf ON sf.id = f.status "
					+ " LEFT OUTER JOIN ( "
					+ " SELECT "
					+ " factura, sum(p.monto) as monto "
					+ " FROM pago p "
					+ " WHERE p.fecha <  ? " //10
					+ " GROUP BY factura "
					+ " ) p ON f.id = p.factura "
					+ " WHERE f.status NOT IN (0,2) "
					+ " AND (f.cliente = ? ) "//11
					+ " AND f.fecha < ? "//12
					+ " ) saldoInicial "
					+ " WHERE saldoInicial.saldo_inicial > 0 "
					+ " GROUP BY cliente "
					+ " ) si ON si.cliente = edc.cte_cve "
					+ " ORDER BY edc.nombre_cliente, edc.fecha, edc.movimiento";
			
			psSelect =  conn.prepareStatement(sql);
			psSelect.setInt(idx++, clienteCve);
			psSelect.setDate(idx++, getSqlDate(fecha));
			psSelect.setDate(idx++, getSqlDate(fechaFin));
			psSelect.setInt(idx++, clienteCve);
			psSelect.setDate(idx++, getSqlDate(fecha));
			psSelect.setDate(idx++, getSqlDate(fechaFin));
			psSelect.setInt(idx++, clienteCve);
			psSelect.setDate(idx++, getSqlDate(fecha));
			psSelect.setDate(idx++, getSqlDate(fechaFin));
			psSelect.setDate(idx++, getSqlDate(fecha));
			psSelect.setInt(idx++, clienteCve);
			psSelect.setDate(idx++, getSqlDate(fecha));
			
			rsSelect = psSelect.executeQuery();
		    listaEstadoCuenta = new ArrayList<EstadoCuenta>();
		    
		    while(rsSelect.next()) {
				ec = getModel(rsSelect);
				listaEstadoCuenta.add(ec);
			}
		} catch (Exception e) {
			log.error("Problema con la consulta de estado de cuenta", e);
		}finally {
			close(rsSelect);
			close(psSelect);
		}
        
		return listaEstadoCuenta;
    }

	
	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		EstadoCuentaDAO.log = log;
	}

}
