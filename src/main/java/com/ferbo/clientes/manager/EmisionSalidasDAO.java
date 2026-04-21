package com.ferbo.clientes.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.model.Inventario;
import com.ferbo.clientes.util.Conexion;

public class EmisionSalidasDAO {
	private static Logger log = LogManager.getLogger(EmisionSalidasDAO.class);

	private static final String SELECT_INVENTARIO = "SELECT * FROM ( "
			+ "	SELECT\n"
			+ "		prd.numero_prod AS codigo, "
			+ "		prd.producto_ds AS producto, "
			+ "		detPart.dtp_caducidad AS caducidad, "
			+ "		ltrim(rtrim(detPart.dtp_sap)) AS sap, "
			+ "		ltrim(rtrim(detPart.dtp_po)) AS po, "
			+ "		ltrim(rtrim(detPart.dtp_pedimento)) AS pedimento, "
			+ "		ltrim(rtrim(detPart.dtp_lote)) AS lote, "
			+ "		ltrim(rtrim(detPart.dtp_tarimas)) AS tarima, "
			+ "		(parEnt.cantidad_total - COALESCE(sal.cantidad, 0)) AS cantidad, "
			+ "		udm.unidad_de_manejo_ds AS unidad_cobro, "
			+ "		(parEnt.peso_total - COALESCE(sal.peso, 0)) AS peso, "
			+ "		'' AS solicitado, "
			+ "		'' AS peso_solicitado, "
			+ "		parEnt.partida_cve AS partida_cve, "
			+ "		cddEnt.folio_cliente AS folio_cliente, "
			+ "		cddEnt.folio AS folio, "
			+ "		plt.PLANTA_DS, "
			+ "		plt.planta_abrev "
			+ "	FROM partida parEnt "
			+ "	INNER JOIN constancia_de_deposito cddEnt ON parEnt.folio = cddEnt.folio AND cddEnt.fecha_ingreso <= CURRENT_DATE() "
			+ "	INNER JOIN unidad_de_producto udp ON udp.unidad_de_producto_cve = parEnt.unidad_de_producto_cve "
			+ "	INNER JOIN producto prd ON prd.producto_cve = udp.producto_cve "
			+ "	INNER JOIN unidad_de_manejo udm ON udm.unidad_de_manejo_cve = udp.unidad_de_manejo_cve "
			+ "	INNER JOIN cliente cli ON cli.cte_cve = cddEnt.cte_cve AND cli.cte_cve = ? "
			+ "	INNER JOIN ( "
			+ "		SELECT tdp.* "
			+ "		FROM detalle_partida tdp "
			+ "		INNER JOIN ( "
			+ "			SELECT "
			+ "				partida_cve, "
			+ "				max(det_part_cve) AS det_part_cve "
			+ "			FROM detalle_partida "
			+ "			GROUP BY partida_cve "
			+ "		) tmdp ON tdp.partida_cve = tmdp.partida_cve AND tdp.det_part_cve = tmdp.det_part_cve "
			+ "	) detPart ON detPart.partida_cve = parEnt.partida_cve "
			+ "	INNER JOIN camara cam ON cam.camara_cve = parEnt.camara_cve "
			+ "	INNER JOIN planta plt ON plt.planta_cve = cam.planta_cve "
			+ "	LEFT OUTER JOIN posicion_partida pp ON parEnt.partida_cve = pp.ID_PARTIDA "
			+ "	LEFT OUTER JOIN posicion pos ON pp.ID_POSICION = pos.id_posicion "
			+ "	LEFT OUTER JOIN ( "
			+ "		SELECT "
			+ "			cddSal.folio AS folio, "
			+ "			cliSal.cte_nombre AS cliente, "
			+ "			parSal.partida_cve AS partida_cve, "
			+ "			sum(COALESCE(dcs.peso, 0)) AS peso, "
			+ "			sum(COALESCE(dcs.cantidad, 0)) AS cantidad, "
			+ "			'' AS solicitado, "
			+ "			'' AS peso_solicitado "
			+ "		FROM partida parSal "
			+ "		INNER JOIN constancia_salida cSal ON cSal.status != 2 AND cSal.fecha <= CURRENT_DATE() "
			+ "		INNER JOIN detalle_constancia_salida dcs ON dcs.constancia_cve = cSal.id AND parSal.partida_cve = dcs.partida_cve "
			+ "		INNER JOIN constancia_de_deposito cddSal ON parSal.folio = cddSal.folio "
			+ "		INNER JOIN cliente cliSal ON cliSal.cte_cve = cddSal.cte_cve AND cliSal.cte_cve = ? "
			+ "		GROUP BY folio, partida_cve, cliente "
			+ "	) sal ON cddEnt.FOLIO = sal.folio AND parEnt.partida_cve = sal.partida_cve "
			+ "	WHERE "
			+ "		cddEnt.status = 1 "
			+ "		AND plt.PLANTA_CVE = ? "
			+ "	) T "
			+ "WHERE cantidad > 0 "
			+ "ORDER BY "
			+ "T.folio, T.producto ";	

	public synchronized Inventario getBean(ResultSet rs)
	throws SQLException {
		Inventario bean = new Inventario();
		
		bean = new Inventario();
		bean.setCodigo(rs.getString("codigo"));
		bean.setProducto(rs.getString("producto"));
		bean.setCaducidad(rs.getDate("caducidad"));
		bean.setSap(rs.getString("sap"));
		bean.setPo(rs.getString("po"));
		bean.setPedimento(rs.getString("pedimento"));
		bean.setLote(rs.getString("lote"));
		bean.setTarima(rs.getString("tarima"));
		bean.setExistencia(rs.getInt("cantidad"));
		bean.setUnidad(rs.getString("unidad_cobro"));
		bean.setPeso(rs.getBigDecimal("peso"));
		bean.setPartidaClave(rs.getInt("partida_cve"));
		bean.setFolio(rs.getInt("folio"));
		bean.setFolioCliente(rs.getString("folio_cliente"));
		bean.setPlantaNombre(rs.getString("PLANTA_DS"));
		bean.setPlantaAbrev(rs.getString("planta_abrev"));
		
		return bean;
	}
	
	public List<Inventario> getInventario(Connection conn, Cliente cliente, Integer idPlanta) {
		List<Inventario> beans = null;
		Inventario bean = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int idx = 1;
		try {
			ps = conn.prepareStatement(SELECT_INVENTARIO);
			log.debug(SELECT_INVENTARIO);
			ps.setInt(idx++, cliente.getIdCliente());
			ps.setInt(idx++, cliente.getIdCliente());
			ps.setInt(idx++, idPlanta);
			rs = ps.executeQuery();
			beans = new ArrayList<>();
			while (rs.next()) {
				bean = getBean(rs);
				beans.add(bean);
			}
		} catch (SQLException ex) {
			log.error("Problema para obtener el inventario", ex);
		} finally {
			Conexion.close(rs);
			Conexion.close(ps);
		}
		return beans;
	}	

}
