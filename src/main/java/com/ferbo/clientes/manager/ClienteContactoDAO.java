package com.ferbo.clientes.manager;

import static com.ferbo.clientes.util.Conexion.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.model.ClienteContacto;
import com.ferbo.clientes.util.Conexion;
import com.ferbo.clientes.util.SecurityUtil;

public class ClienteContactoDAO extends DAO {

	private static Logger log = LogManager.getLogger(ClienteContactoDAO.class);

	private static final String SELECT = "SELECT id_cliente, id_contacto, st_habilitado, nb_usuario, nb_password, st_usuario, fh_alta, fh_cad_passwd, fh_ult_acceso, st_facturacion, st_inventario FROM cliente_contacto " ;
	private static final String SELECT_CLIENTE_CONTACTO = "SELECT id_cliente FROM cliente_contacto WHERE st_habilitado = 1 AND nb_usuario = ? AND nb_password = ?;";
	private static final String SELECT_CLIENTE = "SELECT CTE_CVE, CTE_NOMBRE, COD_UNICO FROM cliente WHERE habilitado = 1 AND CTE_CVE = ?;";
	
	public static synchronized ClienteContacto getBean(ResultSet rs) 
	throws SQLException {
		ClienteContacto bean = new ClienteContacto();
		
		bean.setIdCliente(rs.getInt("id_cliente"));
		bean.setIdContacto(rs.getInt("id_contacto"));
		bean.setHabilitado(rs.getBoolean("st_habilitado"));
		bean.setUsuario(getTrim(rs.getString("nb_usuario")));
		bean.setPassword(getTrim(rs.getString("nb_password")));
		bean.setStatus(getTrim(rs.getString("st_usuario")));
		bean.setFhAlta(rs.getDate("fh_alta"));
		bean.setFhCaducaPwd(rs.getDate("fh_cad_passwd"));
		bean.setFhUltimoAcceso(rs.getDate("fh_ult_acceso"));
		bean.setStatusFacturacion(rs.getBoolean("st_facturacion"));
		bean.setStatusInventario(rs.getBoolean("st_inventario"));
		
		return bean;
	}

	public ClienteContacto login(ClienteContacto clienteContacto){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		SecurityUtil securityUtil = null;
		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(SELECT_CLIENTE_CONTACTO);
			preparedStatement.setString(1, clienteContacto.getUsuario());

			securityUtil = new SecurityUtil();

			String passwordHash = securityUtil.getSHA512(clienteContacto.getPassword());

			preparedStatement.setString(2, passwordHash);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				clienteContacto = new ClienteContacto();
				clienteContacto.setIdCliente(resultSet.getInt("id_cliente"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace(System.out);
		} finally {
			Conexion.close(resultSet);
			Conexion.close(preparedStatement);
			Conexion.close(connection);
		}
		return clienteContacto;
	}
	
	public ClienteContacto get(String usuario) {
		ClienteContacto bean = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		int idx = 1;
		
		try {
			conn = Conexion.dsConexion();
			sql = SELECT + " WHERE nb_usuario = ? ";
			ps = conn.prepareStatement(sql);
			ps.setString(idx++, usuario);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				bean = getBean(rs);
			}
			
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			Conexion.close(rs);
			Conexion.close(ps);
		}
		
		
		return bean;
	}

	public Cliente getCliente(ClienteContacto clienteContacto) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ClienteContacto clienteContacto2 = login(clienteContacto);
		Cliente cliente = null;
		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(SELECT_CLIENTE);
			preparedStatement.setInt(1, clienteContacto2.getIdCliente());
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				cliente = new Cliente();
				cliente.setIdCliente(resultSet.getInt("CTE_CVE"));
				cliente.setNombre(resultSet.getString("CTE_NOMBRE"));
				cliente.setCodigoUnico(resultSet.getString("COD_UNICO"));
			}
		} catch (SQLException ex) {
			log.error("Problema para obtener el cliente del contacto " + clienteContacto.getUsuario(), ex);
		} finally {
			Conexion.close(resultSet);
			Conexion.close(preparedStatement);
			Conexion.close(connection);
		}
		return cliente;
	}

	public int update(Connection conn, ClienteContacto bean)
	throws SQLException {
		int rows = -1;
		PreparedStatement ps = null;
		int idx = 1;
		
		
		try {
			String sql = "UPDATE cliente_contacto "
					+ "SET st_habilitado = ?,"
					+ "nb_usuario = ?, "
					+ "nb_password = ?, "
					+ "st_usuario = ?, "
					+ "fh_alta = ?, "
					+ "fh_cad_passwd = ?, "
					+ "fh_ult_acceso = ?, "
					+ "st_facturacion = ?, "
					+ "st_inventario = ? "
					+ "WHERE id_cliente = ? "
					+ "AND id_contacto = ? "
					;
			
			ps = conn.prepareStatement(sql);
			ps.setBoolean(idx++, bean.getHabilitado());
			ps.setString(idx++, getTrim(bean.getUsuario()));
			ps.setString(idx++, getTrim(bean.getPassword()));
			ps.setString(idx++, getTrim(bean.getStatus()));
			ps.setDate(idx++, getSqlDate(bean.getFhAlta()));
			ps.setDate(idx++, getSqlDate(bean.getFhCaducaPwd()));
			ps.setDate(idx++, getSqlDate(bean.getFhUltimoAcceso()));
			ps.setBoolean(idx++, bean.getStatusFacturacion());
			ps.setBoolean(idx++, bean.getStatusInventario());
			
			setInteger(ps, idx++, bean.getIdCliente());
			setInteger(ps, idx++, bean.getIdContacto());
			
			rows = ps.executeUpdate();
			
		} finally {
			Conexion.close(ps);
		}
		
		return rows;
	}

}
