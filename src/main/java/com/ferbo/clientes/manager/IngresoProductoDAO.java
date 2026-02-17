package com.ferbo.clientes.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.model.Ingreso;
import com.ferbo.clientes.model.IngresoProducto;

public class IngresoProductoDAO extends DAO {
	
	private static Logger log = LogManager.getLogger(IngresoProducto.class);
	
	private static final String SELECT = "SELECT id_ingreso_producto, cantidad, id_unidad_medida, peso, id_planta, no_tarimas, lote, pedimento, contenedor, fecha_Caducidad, otro, id_ingreso, id_producto\n"
			+ "FROM ingreso_producto ";
	
	public static IngresoProducto getBean(ResultSet rs)throws SQLException{
		
		IngresoProducto bean = new IngresoProducto();
		
		bean.setCantidad(rs.getInt("cantidad"));
		bean.setIdUnidadMedida(rs.getInt("id_unidad_medida"));
		bean.setPeso(rs.getBigDecimal("peso"));
		bean.setIdPlanta(rs.getInt("id_planta"));
		bean.setNoTarimas(rs.getBigDecimal("no_tarimas"));
		bean.setLote(rs.getString("lote"));
		bean.setPedimento(rs.getString("pedimento"));
		bean.setContenedor(rs.getString("contenedor"));
		bean.setFechaCaducidad(rs.getDate("fecha_caducidad"));
		bean.setOtro(rs.getString("otro"));
		bean.setIdIngreso(rs.getInt("id_ingreso"));
		bean.setIdProducto(rs.getInt("id_producto"));
		
		return bean;
	}
	

	public static int insert(Connection conn, IngresoProducto ingresoProducto) throws SQLException{
		
		int registros = -1;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		
		int idx = 1;
		
		try {
			
			sql = "INSERT INTO ingreso_producto "
					+ "(cantidad, id_unidad_medida, peso, id_planta, no_tarimas, lote, pedimento, contenedor, fecha_Caducidad, otro, id_ingreso, id_producto) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			
			ps = conn.prepareStatement(sql);
			
			ps.setInt(idx++, ingresoProducto.getCantidad());
			ps.setInt(idx++, ingresoProducto.getIdUnidadMedida());
			ps.setBigDecimal(idx++, ingresoProducto.getPeso());
			ps.setInt(idx++, ingresoProducto.getIdPlanta());
			ps.setBigDecimal(idx++, ingresoProducto.getNoTarimas());
			ps.setString(idx++, ingresoProducto.getLote());
			ps.setString(idx++, ingresoProducto.getPedimento());
			ps.setString(idx++, ingresoProducto.getContenedor());
			ps.setDate(idx++, getSqlDate(ingresoProducto.getFechaCaducidad()));
			ps.setString(idx++, ingresoProducto.getOtro());
			ps.setInt(idx++, ingresoProducto.getIdIngreso());
			ps.setInt(idx++, ingresoProducto.getIdProducto());
			
			registros = ps.executeUpdate();			
			
			
		} finally {
			close(ps);
			close(rs);
		}
		
		
		return registros;
		
	}
	
	
	public static List<IngresoProducto> getIngresosProducto(Connection conn, Ingreso ingreso) throws SQLException {
		
		IngresoProducto bean = new IngresoProducto();
		List<IngresoProducto> beans = new ArrayList<IngresoProducto>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			
			sql = SELECT + " WHERE id_ingreso = ? ";
			
			ps = conn.prepareStatement(sql);
			
			ps.setInt(1, ingreso.getIdIngreso());
			
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
