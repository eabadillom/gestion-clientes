package com.ferbo.clientes.mail.mngr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ferbo.clientes.mail.beans.ClienteContacto;

public class ClienteContactoDAO extends DAO {
	private static final String SELECT = "SELECT id_cliente, id_contacto, st_habilitado, nb_usuario, nb_password, st_usuario, fh_alta, fh_cad_passwd, fh_ult_acceso, id, st_facturacion, st_inventario FROM cliente_contacto ";
	private static final String INSERT = "INSERT INTO cliente_contacto (id_cliente, id_contacto, st_habilitado, nb_usuario, nb_password, st_usuario, fh_alta, fh_cad_passwd, fh_ult_acceso, st_facturacion, st_inventario) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	private static final String UPDATE = "UPDATE cliente_contacto SET id_cliente = ?, id_contacto = ?, st_habilitado = ?, nb_usuario = ?, nb_password = ?, st_usuario = ?, fh_alta = ?, fh_cad_passwd = ?, fh_ult_acceso = ?, st_facturacion = ?, st_inventario = ? WHERE id = ? ";
	//private static final String DELETE = "";
	
	private static synchronized ClienteContacto getBean(ResultSet rs)
	throws SQLException {
		ClienteContacto bean = new ClienteContacto();
		
		bean.setIdCliente(rs.getInt("id_cliente"));
		bean.setIdContacto(rs.getInt("id_contacto"));
		bean.setHabilitado(rs.getBoolean("st_habilitado"));
		bean.setUsuario(getTrim(rs.getString("nb_usuario")));
		bean.setPassword(getTrim(rs.getString("nb_password")));
		bean.setStatusUsuario(getTrim(rs.getString("st_usuario")));
		bean.setFechaAlta(rs.getDate("fh_alta"));
		bean.setFechaCaducidad(rs.getDate("fh_cad_passwd"));
		bean.setFechaUltimoAcceso(rs.getDate("fh_ult_acceso"));
		bean.setId(getInteger(rs, "id"));
		bean.setStatusFacturacion(rs.getBoolean("st_facturacion"));
		bean.setStatusInventario(rs.getBoolean("st_inventario"));
		
		return bean;
	}
	
	public static ClienteContacto get(Connection conn, Integer idCliente, Integer idContacto)
	throws SQLException {
		ClienteContacto bean = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		int idx = 1;
		
		try {
			sql = SELECT + " WHERE id_cliente = ? AND id_contacto = ? ";
			ps = conn.prepareStatement(sql);
			ps.setInt(idx++, idCliente);
			ps.setInt(idx++, idContacto);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				bean = getBean(rs);
			}
			
		} finally {
			close(rs);
			close(ps);
		}
		
		return bean;
	}
	
	public static ClienteContacto[] get(Connection conn, Integer idCliente)
	throws SQLException {
		ClienteContacto[] beans = null;
		List<ClienteContacto> alBeans = null;
		ClienteContacto bean = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		int idx = 1;
		
		try {
			sql = SELECT + " WHERE id_cliente = ? ";
			ps = conn.prepareStatement(sql);
			ps.setInt(idx++, idCliente);
			
			rs = ps.executeQuery();
			alBeans = new ArrayList<ClienteContacto>();
			while(rs.next()) {
				bean = getBean(rs);
				alBeans.add(bean);
			}
			
			beans = new ClienteContacto[alBeans.size()];
			beans = (ClienteContacto[]) alBeans.toArray(beans);
			
		} finally {
			close(rs);
			close(ps);
		}
		
		return beans;
	}

	public static int insert(Connection conn, ClienteContacto bean)
	throws SQLException {
		int rows = -1;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int idx = 1;
		
		try {
			ps = conn.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
			
			ps.setInt(idx++, bean.getIdCliente());
			ps.setInt(idx++, bean.getIdContacto());
			ps.setBoolean(idx++, bean.isHabilitado());
			ps.setString(idx++, getTrim(bean.getUsuario()));
			ps.setString(idx++, getTrim(bean.getPassword()));
			ps.setString(idx++, getTrim(bean.getStatusUsuario()));
			ps.setDate(idx++, getSqlDate(bean.getFechaAlta()));
			ps.setDate(idx++, getSqlDate(bean.getFechaCaducidad()));
			ps.setDate(idx++, getSqlDate(bean.getFechaUltimoAcceso()));
			ps.setBoolean(idx++, bean.getStatusFacturacion());
			ps.setBoolean(idx++, bean.getStatusInventario());
			
			rows = ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			
			if(rs.next()) {
				bean.setId(rs.getInt(1));
			}
			
		} finally {
                        close(rs);
			close(ps);
		}
		
		return rows;
	}

	public static int update(Connection conn, ClienteContacto cteContacto)
	throws SQLException {
		int rows = -1;
		PreparedStatement ps = null;
		int idx = 1;
		
		try {
			ps = conn.prepareStatement(UPDATE);
			ps.setInt(idx++, cteContacto.getIdCliente());
			ps.setInt(idx++, cteContacto.getIdContacto());
			ps.setBoolean(idx++, cteContacto.isHabilitado());
			ps.setString(idx++, getTrim(cteContacto.getUsuario()));
			ps.setString(idx++, getTrim(cteContacto.getPassword()));
			ps.setString(idx++, getTrim(cteContacto.getStatusUsuario()));
			ps.setDate(idx++, getSqlDate(cteContacto.getFechaAlta()));
			ps.setDate(idx++, getSqlDate(cteContacto.getFechaCaducidad()));
			ps.setDate(idx++, getSqlDate(cteContacto.getFechaUltimoAcceso()));
			ps.setBoolean(idx++, cteContacto.getStatusFacturacion());
			ps.setBoolean(idx++, cteContacto.getStatusInventario());
			
			setInteger(ps, idx++, cteContacto.getId());
			
			rows = ps.executeUpdate();
			
		} finally {
			close(ps);
		}
		
		return rows;
	}
}
