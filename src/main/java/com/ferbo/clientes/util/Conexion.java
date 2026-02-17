package com.ferbo.clientes.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Conexion {
	private static Logger logger = LogManager.getLogger(Conexion.class);
	
	public static synchronized Connection dsConexion() throws SQLException {
		Connection conn = null;
		Context initContext = null;
		Context envContext = null;
		DataSource ds = null;

		try {

			try {
				logger.debug("Buscando conexion a base de datos...");
				initContext = new InitialContext();
				ds = (DataSource) initContext.lookup("jdbc/gestion");
			} catch (NameNotFoundException inEx) {
				envContext = (Context) initContext.lookup("java:/comp/env");
				ds = (DataSource) envContext.lookup("jdbc/gestion");
			} catch (NamingException e) {
				logger.error(e.getLocalizedMessage(), e);
			}

			if (ds != null) {
				conn = ds.getConnection();
				conn.setAutoCommit(false);
			}
		} catch (NamingException e) {
			logger.error(e.getLocalizedMessage(), e);
		} finally {
			logger.debug(conn);
		}
		
		return conn;
	}

	public static Connection getConnection() throws SQLException {
		Connection con = null;
		con = dsConexion();
		return con;
	}

	public static void close(ResultSet resultset) {
		try {
			if (resultset != null)
				resultset.close();
			resultset = null;
			logger.debug("El objeto ResultSet se cerro satisfactoriamente");
		} catch (SQLException ex) {
			logger.error("Error al cerrar un ResultSet: ", ex);
		}
	}

	public static void close(Statement statement) {
		try {
			if (statement != null)
				statement.close();
			statement = null;
			logger.debug("El objeto Statement se cerro satisfactoriamente.");
		} catch (SQLException ex) {
			logger.error("Error al cerrar un Statement: ", ex);
		}
	}

	public static void close(PreparedStatement statement) {
		try {
			if (statement != null)
				statement.close();
			statement = null;
			logger.debug("El objeto PreparedStatement se cerro satisfactoriamente.");
		} catch (SQLException ex) {
			logger.error("Error al cerrar un PreparedStatement: ", ex);
		}
	}

	public static synchronized void close(Connection connection) {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException ex) {
			logger.error("Problema al cerrar el objeto Connection.", ex);
		} catch (Exception ex) {
			logger.error("Problema general al cerrar el objeto Connection.", ex);
		} finally {
			connection = null;
		}
	}

	public static void rollback(Connection conn) {
		try {
			if (conn != null)
				conn.rollback();
		} catch (SQLException ex) {
			logger.error("Problema al realizar rollback de la conexion.", ex);
		}
	}

}
