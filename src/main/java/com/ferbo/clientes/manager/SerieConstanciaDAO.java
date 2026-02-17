package com.ferbo.clientes.manager;

import static com.ferbo.clientes.util.Conexion.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.model.SerieConstancia;
import com.ferbo.clientes.util.Conexion;

public class SerieConstanciaDAO extends DAO{
	
	private static Logger log = LogManager.getLogger(ClienteDAO.class);
	
	private static final String SELECT = "SELECT id_cliente, tp_serie, nu_serie, id_planta "
										+ "FROM serie_constancia ";
	private static final String UPDATE = "UPDATE serie_constancia SET nu_serie = ? WHERE id_cliente = ? AND tp_serie = ? AND id_planta = ? ";

	public SerieConstancia getBean(ResultSet rs) throws SQLException{
		SerieConstancia bean = new SerieConstancia();
		
		bean.setIdCliente(rs.getInt("id_cliente"));
		bean.setNumero(rs.getInt("nu_serie"));
		bean.setTipoSerie(rs.getString("tp_serie"));
		bean.setIdPlanta(getInteger(rs, "id_planta"));
		
		return bean;
		
	}
	
	public SerieConstancia getSerie(Connection conn, Integer idCliente, Integer idPlanta, String tipoSerie) {
		SerieConstancia bean = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		int idx = 1;
		try {
			sql = SELECT.concat("WHERE id_cliente = ? AND id_planta = ? AND tp_serie = ? ");
			
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			setInteger(ps, idx++, idCliente);
			setInteger(ps, idx++, idPlanta);
			ps.setString(idx++, tipoSerie);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				bean = this.getBean(rs);
			}
		} catch (SQLException ex) {
			log.error("Problema con la lectura de serie de constancia...",ex);
			ex.printStackTrace(System.out);
		} finally {
			Conexion.close(rs);
			Conexion.close(ps);
		}
		return bean;
	}
	
	public SerieConstancia getSerie(Connection connection,Cliente cliente, String tipoSerie)throws SQLException {
		SerieConstancia serieConstancia = null;		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String sql;
		int idx = 1;
		
		try {
			sql = SELECT + " WHERE id_cliente = ? AND tp_serie = ?";
			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(idx++, cliente.getIdCliente());
			preparedStatement.setString(idx++,tipoSerie);
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				serieConstancia = getBean(resultSet); 
			}
		} finally {
			Conexion.close(resultSet);
			Conexion.close(preparedStatement);			
		}
		return serieConstancia;
	}

	public void update(Connection connection, SerieConstancia serieConstancia ) throws SQLException {
		PreparedStatement ps = null;
		int idx = 1;
        try {
        	ps = connection.prepareStatement(UPDATE);
        	setInteger(ps, idx++, serieConstancia.getNumero());
        	ps.setString(idx++, serieConstancia.getTipoSerie());
        	setInteger(ps, idx++, serieConstancia.getIdPlanta());
        	ps.executeUpdate();
        } finally {
			Conexion.close(ps);
        }
	}
}
