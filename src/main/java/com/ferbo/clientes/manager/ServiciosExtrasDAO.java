package com.ferbo.clientes.manager;

import static com.ferbo.clientes.util.Conexion.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.model.PreSalidaObservaciones;
import com.ferbo.clientes.model.ServiciosExtras;
import com.ferbo.clientes.util.Conexion;

public class ServiciosExtrasDAO {

	private static Logger log = LogManager.getLogger(PreSalidaDAO.class);
	
	private static final String SELECT_SERVICIOSEXTRAS = "SELECT "
			+ "	s.servicio_cve as idServicioExtra, "
			+ "	s.servicio_ds as ServicioExtra, "
			+ "	u.UNIDAD_DE_MANEJO_CVE as idUnidadManejo, "
			+ "	u.UNIDAD_DE_MANEJO_DS as unidadManejo, "
			+ "	tc.nombre as tipoCobro "
			+ "FROM cliente c "
			+ "INNER JOIN aviso a ON c.CTE_CVE = a.cte_cve "
			+ "INNER JOIN precio_servicio ps ON ps.aviso_cve = a.aviso_cve AND ps.cliente = a.cte_cve "
			+ "INNER JOIN servicio s ON s.servicio_cve = ps.servicio "
			+ "INNER JOIN unidad_de_manejo u ON ps.unidad = u.unidad_de_manejo_cve "
			+ "INNER JOIN tipo_cobro tc ON s.cobro = tc.id "
			+ "WHERE a.cte_cve = c.cte_cve "
			+ "	AND s.cobro NOT IN (4) "
			+ "	AND c.cte_cve = ? "
			+ "GROUP BY "
			+ "	s.servicio_cve, "
			+ "	s.servicio_ds, "
			+ "	u.UNIDAD_DE_MANEJO_CVE, "
			+ "	u.UNIDAD_DE_MANEJO_DS, "
			+ "	tc.nombre "
			+ "ORDER BY "
			+ "	s.servicio_ds ASC ";
	
	private static final String INSERT_SERVICIOSEXTRAS = "INSERT INTO pre_salida_srv (cd_folio_salida, id_servicio, nu_cantidad, id_unidad_manejo, observacion) values (?, ?, ?, ?, ?);";
	private static final String INSERT_SERVICIOSEXTRASOBS = "INSERT INTO pre_salida_obs (cd_folio_salida, nb_observaciones) values (?, ?);";
	
	public synchronized ServiciosExtras getBean(ResultSet rs )
	throws SQLException {
		ServiciosExtras bean = new ServiciosExtras();
		
		bean.setIdServicioExtra(rs.getInt("idServicioExtra"));
		bean.setServicioExtra(rs.getString("ServicioExtra"));
		bean.setIdUnidadManejo(rs.getInt("idUnidadManejo"));
		bean.setUnidadManejo(rs.getString("unidadManejo"));
		bean.setTipoCobro(rs.getString("tipoCobro"));
		return bean;
	}

	public List<ServiciosExtras> getServiciosExtras(Cliente cliente) {
		List<ServiciosExtras> beans = null;
		ServiciosExtras bean = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(SELECT_SERVICIOSEXTRAS);
			log.debug(SELECT_SERVICIOSEXTRAS);
			ps.setInt(1, cliente.getIdCliente());
			rs = ps.executeQuery();
			beans = new ArrayList<>();
			while (rs.next()) {
				bean = getBean(rs);
				beans.add(bean);
			}
		} catch (SQLException ex) {
			log.error("Problema con la lectura de los servicios extras...", ex);
			ex.printStackTrace(System.out);
		} finally {
			Conexion.close(rs);
			Conexion.close(ps);
			Conexion.close(conn);
		}
		return beans;
	}

	public int insert(Connection conn, ServiciosExtras bean)
	throws SQLException {
		int rows = -1;
		PreparedStatement ps = null;
		int idx = 1;
		try {
			ps = conn.prepareStatement(INSERT_SERVICIOSEXTRAS);
			
			ps.setString(idx++, bean.getFolioSalida());
			ps.setInt(idx++, bean.getIdServicioExtra());
			ps.setInt(idx++, bean.getCantidad());
			ps.setInt(idx++, bean.getIdUnidadManejo());
			ps.setString(idx++, bean.getObservacion());
			rows = ps.executeUpdate();
			log.info("Registro servicios extras agregado: " + bean.toString());
		} finally {
			Conexion.close(ps);
		}
		
		return rows;
	}
	
	public int insert(Connection conn, PreSalidaObservaciones bean)
	throws SQLException {
		int rows = -1;
		PreparedStatement ps = null;
		int idx = 1;
		
		try {
			ps = conn.prepareStatement(INSERT_SERVICIOSEXTRASOBS);

			ps.setString(idx++, bean.getFolioSalida());
			ps.setString(idx++, bean.getObservaciones());
			
			rows = ps.executeUpdate();
			log.info("Registro de observaciones agregado: " + bean.toString());
		} finally {
			Conexion.close(ps);
		}
		
		return rows;
	}

}
