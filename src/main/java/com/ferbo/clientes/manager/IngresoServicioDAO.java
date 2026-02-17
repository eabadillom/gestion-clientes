package com.ferbo.clientes.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ferbo.clientes.model.IngresoServicio;

public class IngresoServicioDAO extends DAO {

	public static int insert(Connection conn, IngresoServicio ingresoServicio)throws SQLException{
		
		int registros = -1;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		int idx = 1;
		
		try {
			
			sql = "INSERT INTO ingreso_servicio "
					+ "(id_servicio, cantidad, id_unidad, id_ingreso) "
					+ "VALUES(?, ?, ?, ?);";
			
			ps = conn.prepareStatement(sql);
			
			ps.setInt(idx++, ingresoServicio.getIdServicio());
			ps.setBigDecimal(idx++, ingresoServicio.getCantidad());
			ps.setInt(idx++, ingresoServicio.getIdUnidad());
			ps.setInt(idx++, ingresoServicio.getIdIngreso());
			
			registros = ps.executeUpdate();
			
		} finally {
			close(ps);
			close(rs);
		}
		
		return registros;
		
	}
	
}
