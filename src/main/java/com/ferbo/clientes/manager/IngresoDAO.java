package com.ferbo.clientes.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.model.Ingreso;

public class IngresoDAO extends DAO{
	
	private static Logger log = LogManager.getLogger(Ingreso.class);
	
	private static final String SELECT = "SELECT id_ingreso, folio, fecha_hora, id_cliente, transportista, placas, observaciones, id_contacto, status "
			+ "FROM ingreso ";
	
	
	public static Ingreso getBean(ResultSet rs)throws SQLException {
		
		Ingreso bean = new Ingreso();
		
		bean.setIdIngreso(rs.getInt("id_ingreso"));
		bean.setFolio(rs.getString("folio"));
		bean.setFechaHora(rs.getTimestamp("fecha_hora"));
		bean.setIdCliente(rs.getInt("id_cliente"));
		bean.setTransportista(rs.getString("transportista"));
		bean.setPlacas(rs.getString("placas"));
		bean.setObservaciones(rs.getString("observaciones"));
		bean.setIdContacto(rs.getInt("id_contacto"));
		bean.setStatus(rs.getInt("status"));
		
		return bean;
	}

	
	public static int insert(Connection conn,Ingreso ingreso)throws SQLException{
		
		int registros = -1;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		int idx = 1;
		Integer pk = null;
		
		try {
			
			sql = "INSERT INTO ingreso "
					+ "(folio, fecha_hora, id_cliente, transportista, placas, observaciones, id_contacto ,status) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
			
			ps = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
			
			ps.setString(idx++, ingreso.getFolio()); 					
			
			Instant instance = getInstant(new Timestamp(ingreso.getFechaHora().getTime()));					
			
			Timestamp time = getTimestamp(instance);
			
			ps.setTimestamp(idx++, time);
			ps.setInt(idx++, ingreso.getIdCliente());
			ps.setString(idx++, ingreso.getTransportista());
			ps.setString(idx++, ingreso.getPlacas());
			ps.setString(idx++, ingreso.getObservaciones());
			ps.setInt(idx++, ingreso.getIdContacto());
			ps.setInt(idx++, ingreso.getStatus());
			
			registros = ps.executeUpdate();
			
			rs = ps.getGeneratedKeys();
			
			if(rs.next()) {			
				pk = rs.getInt(1);
			}
			
			ingreso.setIdIngreso(pk);
			
		} finally {
			close(rs);
			close(ps);
		}
		
		return registros;
	}
	
	
	public static List<Ingreso> getIngresosFolio(Connection conn, String folio)throws SQLException{
		
		List<Ingreso> beans = new ArrayList<Ingreso>();
		Ingreso bean = new Ingreso();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			
			sql = SELECT + " WHERE folio = ? " ;
			
			ps = conn.prepareStatement(sql);
			
			ps.setString(1, folio);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				bean = getBean(rs);
				beans.add(bean);
			}
			
		} finally {
			close(ps);
			close(rs);
		}
		
		return beans;
	}
	
}
