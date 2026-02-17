package com.ferbo.clientes.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.model.ProductoPorCliente;

public class ProductoPorClienteDAO extends DAO{
	
	private static Logger log = LogManager.getLogger(ProductoPorClienteDAO.class);
	private static final String SELECT = "SELECT PROD_X_CTE_CVE, CTE_CVE, PRODUCTO_CVE"
			+ " FROM producto_por_cliente";
	
	private static synchronized ProductoPorCliente getBean(ResultSet rs) throws SQLException {
				
		ProductoPorCliente bean = new ProductoPorCliente();
		
		bean.setIdProductoPorCte(rs.getInt("PROD_X_CTE_CVE"));
		bean.setIdCliente(rs.getInt("CTE_CVE"));
		bean.setIdProducto(rs.getInt("PRODUCTO_CVE"));		
			
			return bean;
	}
	
	public List<ProductoPorCliente> getProductosPorCte(Connection con,Integer idCliente) throws SQLException{
		
		List<ProductoPorCliente> beans = new ArrayList<ProductoPorCliente>();
		ProductoPorCliente bean = new ProductoPorCliente();
		
		PreparedStatement ps = null;
		ResultSet         rs = null;
		String            sql = null;
				
		try {
			
			sql = SELECT + " WHERE CTE_CVE = ?";
			
			ps = con.prepareStatement(sql);			
			ps.setInt(1,idCliente);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				bean = getBean(rs);
				beans.add(bean);
			}
			
		}finally {
			close(ps);
			close(rs);
		}
		
		
		return beans;
		
	}

}
