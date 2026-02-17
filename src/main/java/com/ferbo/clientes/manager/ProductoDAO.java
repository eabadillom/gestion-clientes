package com.ferbo.clientes.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.model.Producto;

public class ProductoDAO extends DAO {
	
	private static Logger log = LogManager.getLogger(ProductoDAO.class);
	
	private static final String SELECT = "SELECT PRODUCTO_CVE, PRODUCTO_DS, NUMERO_PROD, categoria"
			+ " FROM producto";
	
	public Producto getBean(ResultSet rs) throws SQLException {
		
		Producto producto = new Producto();
		
		producto.setId(rs.getInt("PRODUCTO_CVE"));
		producto.setProducto_ds(rs.getString("PRODUCTO_DS"));
		producto.setNumero_prod(rs.getString("NUMERO_PROD"));
		producto.setCategoria(rs.getInt("categoria"));
		
		return producto;
		
	}
	
	public Producto getProductos(Connection con, Integer idProducto) throws SQLException{
		
		Producto bean = new Producto();		
		
		PreparedStatement ps = null;
		ResultSet         rs = null;
		String            sql = null;
		
		try {
			
			sql = SELECT + " WHERE PRODUCTO_CVE = ?";
			
			ps = con.prepareStatement(sql);
			
			ps.setInt(1, idProducto);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				bean = getBean(rs);
				
			}
			
		} finally {
			close(ps);
			close(rs);
		}
		
		return bean;		
	}
	
	

}
