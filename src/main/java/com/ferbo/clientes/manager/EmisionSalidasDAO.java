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

import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.model.Inventario;
import com.ferbo.clientes.util.Conexion;

public class EmisionSalidasDAO extends DAO {
	private static Logger log = LogManager.getLogger(EmisionSalidasDAO.class);

	private static final String SELECT_INVENTARIO = "SELECT * FROM ( \n"
			+ "	SELECT\n"
			+ "		prd.numero_prod AS codigo, \n"
			+ "		prd.producto_ds AS producto, \n"
			+ "		detPart.dtp_caducidad AS caducidad, \n"
			+ "		ltrim(rtrim(detPart.dtp_sap)) AS sap, \n"
			+ "		ltrim(rtrim(detPart.dtp_po)) AS po, \n"
			+ "		ltrim(rtrim(detPart.dtp_pedimento)) AS pedimento, \n"
			+ "		ltrim(rtrim(detPart.dtp_lote)) AS lote, \n"
			+ "		ltrim(rtrim(detPart.dtp_tarimas)) AS tarima, \n"
			+ "		(parEnt.cantidad_total - COALESCE(sal.cantidad, 0)) AS cantidad,\n"
			+ "		rsv.cantidad AS cantidad_rsv,\n"
			+ "		udm.unidad_de_manejo_ds AS unidad_cobro, \n"
			+ "		(parEnt.peso_total - COALESCE(sal.peso, 0)) AS peso, \n"
			+ "		'' AS solicitado, \n"
			+ "		'' AS peso_solicitado, \n"
			+ "		parEnt.partida_cve AS partida_cve, \n"
			+ "		cddEnt.folio_cliente AS folio_cliente, \n"
			+ "		cddEnt.folio AS folio, \n"
			+ "		plt.PLANTA_DS, \n"
			+ "		plt.planta_abrev \n"
			+ "	FROM partida parEnt \n"
			+ "	INNER JOIN constancia_de_deposito cddEnt ON parEnt.folio = cddEnt.folio AND cddEnt.fecha_ingreso <= ? \n"
			+ "	INNER JOIN unidad_de_producto udp ON udp.unidad_de_producto_cve = parEnt.unidad_de_producto_cve \n"
			+ "	INNER JOIN producto prd ON prd.producto_cve = udp.producto_cve \n"
			+ "	INNER JOIN unidad_de_manejo udm ON udm.unidad_de_manejo_cve = udp.unidad_de_manejo_cve \n"
			+ "	INNER JOIN cliente cli ON cli.cte_cve = cddEnt.cte_cve AND cli.cte_cve = ?\n"
			+ "	INNER JOIN ( \n"
			+ "		SELECT tdp.* \n"
			+ "		FROM detalle_partida tdp \n"
			+ "		INNER JOIN ( \n"
			+ "			SELECT \n"
			+ "				partida_cve, \n"
			+ "				max(det_part_cve) AS det_part_cve \n"
			+ "			FROM detalle_partida \n"
			+ "			GROUP BY partida_cve \n"
			+ "		) tmdp ON tdp.partida_cve = tmdp.partida_cve AND tdp.det_part_cve = tmdp.det_part_cve \n"
			+ "	) detPart ON detPart.partida_cve = parEnt.partida_cve \n"
			+ "	INNER JOIN camara cam ON cam.camara_cve = parEnt.camara_cve \n"
			+ "	INNER JOIN planta plt ON plt.planta_cve = cam.planta_cve \n"
			+ "	LEFT OUTER JOIN posicion_partida pp ON parEnt.partida_cve = pp.ID_PARTIDA \n"
			+ "	LEFT OUTER JOIN posicion pos ON pp.ID_POSICION = pos.id_posicion \n"
			+ "	LEFT OUTER JOIN ( \n"
			+ "		SELECT\n"
			+ "			dcs.PARTIDA_CVE as partida_cve,\n"
			+ "			sum(dcs.CANTIDAD) as cantidad,\n"
			+ "			sum(dcs.PESO) as peso\n"
			+ "		FROM  constancia_salida cs\n"
			+ "		INNER JOIN detalle_constancia_salida dcs ON cs.id = dcs.constancia_cve\n"
			+ "		WHERE cs.STATUS = 1\n"
			+ "		AND cs.FECHA <= ?\n"
			+ "		GROUP BY dcs.PARTIDA_CVE\n"
			+ "	) sal ON parEnt.partida_cve = sal.partida_cve\n"
			+ "	LEFT OUTER JOIN (\n"
			+ "		select\n"
			+ "			sd.partida_cve,\n"
			+ "			sum(sd.nu_cantidad) as cantidad,\n"
			+ "			sum(sd.ct_peso_aprox) as peso\n"
			+ "		from salida s\n"
			+ "		inner join salida_detalle sd on s.cd_salida = sd.cd_salida\n"
			+ "		where s.cd_status = 1\n"
			+ "		and s.fh_registro >= ?\n"
			+ "		and s.fh_salida <= ?\n"
			+ "		group by sd.partida_cve\n"
			+ "	) rsv ON parEnt.PARTIDA_CVE = rsv.partida_cve\n"
			+ "	WHERE \n"
			+ "		cddEnt.status = 1 \n"
			+ "		AND plt.PLANTA_CVE = ? \n"
			+ "	) T \n"
			+ "WHERE cantidad > 0 \n"
			+ "ORDER BY \n"
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
		bean.setCantidadReservada(getInteger(rs, "cantidad_rsv"));
		bean.setUnidad(rs.getString("unidad_cobro"));
		bean.setPeso(rs.getBigDecimal("peso"));
		bean.setPartidaClave(rs.getInt("partida_cve"));
		bean.setFolio(rs.getInt("folio"));
		bean.setFolioCliente(rs.getString("folio_cliente"));
		bean.setPlantaNombre(rs.getString("PLANTA_DS"));
		bean.setPlantaAbrev(rs.getString("planta_abrev"));
		
		return bean;
	}
	
	public List<Inventario> getInventario(Connection conn, Date fecha, Cliente cliente, Integer idPlanta) {
		List<Inventario> beans = null;
		Inventario bean = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int idx = 1;
		try {
			ps = conn.prepareStatement(SELECT_INVENTARIO);
			log.debug(SELECT_INVENTARIO);
			
			ps.setDate(idx++, new java.sql.Date(fecha.getTime()));
			ps.setInt(idx++, cliente.getIdCliente());
			ps.setDate(idx++, new java.sql.Date(fecha.getTime()));
			ps.setDate(idx++, new java.sql.Date(fecha.getTime()));
			ps.setDate(idx++, new java.sql.Date(fecha.getTime()));
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
