package com.ferbo.clientes.mail.mngr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.mail.beans.Camara;

public class CamaraDAO extends DAO {
	private static final String SELECT = "SELECT camara_cve, planta_cve, camara_ds, camara_abrev FROM camara ";
	
	private static Logger log = LogManager.getLogger(CamaraDAO.class);
	
	public static Camara getCamara(Connection conn, int idPlanta, int idCamara)
	throws SQLException {
		Camara cam = null;
		
		PreparedStatement psCamara = null;
		ResultSet rsCamara = null;
		String sqlCamara = null;
		
		try{
			sqlCamara = SELECT + " WHERE planta_cve = ? and camara_cve = ? ";
			psCamara = conn.prepareStatement(sqlCamara);
			psCamara.setInt(1, idPlanta);
			psCamara.setInt(2, idCamara);
			log.debug("Preparando SELECT para la tabla CAMARA...");
			log.debug(sqlCamara);
			rsCamara = psCamara.executeQuery();
			
			if(rsCamara.next()) {
				cam = new Camara();
				cam.setPlanta(rsCamara.getInt("planta_cve"));
				cam.setId(rsCamara.getInt("camara_cve"));
				cam.setDescripcion(rsCamara.getString("camara_ds"));
				cam.setNumero(rsCamara.getString("camara_abrev"));
			}
			log.debug("El SELECT para la tabla CAMARA termino satisfactoriamente...");
		} finally {
			close(psCamara);
			close(rsCamara);
		}
		
		return cam;
	}
	
	public static Camara getCamara(Connection conn, int idCamara)
	throws SQLException {
		Camara cam = null;
		
		PreparedStatement psCamara = null;
		ResultSet         rsCamara = null;
		String            sqlCamara = null;
		
		try{
			sqlCamara = SELECT + "WHERE camara_cve = ? ";
			psCamara = conn.prepareStatement(sqlCamara);
			psCamara.setInt(1, idCamara);
			log.debug("Preparando SELECT para la tabla CAMARA...");
			log.debug(sqlCamara);
			rsCamara = psCamara.executeQuery();
			
			if(rsCamara.next()) {
				cam = new Camara();
				cam.setPlanta(rsCamara.getInt("planta_cve"));
				cam.setId(rsCamara.getInt("camara_cve"));
				cam.setDescripcion(rsCamara.getString("camara_ds"));
				cam.setNumero(rsCamara.getString("camara_abrev"));
			}
			log.debug("El SELECT para la tabla CAMARA termino satisfactoriamente...");
		} finally {
			close(psCamara);
			close(rsCamara);
		}
		
		return cam;
	}
	
	public static Camara[] getCamaras(Connection conn, int idPlanta)
			throws SQLException {
		Camara            camaras[] = null;
		Camara            camara       = null;
		PreparedStatement psCamara  = null;
		ResultSet         rsCamara  = null;
		String            sqlCamara = null;
		ArrayList<Camara> alCamaras = null;
		
		try{
			sqlCamara = SELECT + "WHERE planta_cve = ?  ORDER BY camara_ds ";
			psCamara = conn.prepareStatement(sqlCamara);
			psCamara.setInt(1, idPlanta);
			log.debug("Preparando SELECT para la tabla CAMARA...");
			log.debug(sqlCamara);
			rsCamara = psCamara.executeQuery();
			
			alCamaras = new ArrayList<Camara>();
			while(rsCamara.next()) {
				camara = new Camara();
				camara.setPlanta(rsCamara.getInt("planta_cve"));
				camara.setId(rsCamara.getInt("camara_cve"));
				camara.setDescripcion(rsCamara.getString("camara_ds"));
				camara.setNumero(rsCamara.getString("camara_abrev"));
				alCamaras.add(camara);
			}
			camaras = new Camara[alCamaras.size()];
			camaras = (Camara[]) alCamaras.toArray(camaras);
			
			log.debug("El SELECT para la tabla CAMARA termino satisfactoriamente...");
		} finally {
			close(psCamara);
			close(rsCamara);
		}
		
		return camaras;
	}
	
	public static Camara[] getCamaras(Connection conn)
			throws SQLException {
		Camara            camaras[] = null;
		Camara            camara       = null;
		PreparedStatement psCamara  = null;
		ResultSet         rsCamara  = null;
		String            sqlCamara = null;
		ArrayList<Camara> alCamaras = null;
		
		try{
			sqlCamara = SELECT + "ORDER BY planta_cve, camara_cve ";
			psCamara = conn.prepareStatement(sqlCamara);
			log.debug("Preparando SELECT para la tabla CAMARA...");
			log.debug(sqlCamara);
			rsCamara = psCamara.executeQuery();
			
			alCamaras = new ArrayList<Camara>();
			while(rsCamara.next()) {
				camara = new Camara();
				camara.setPlanta(rsCamara.getInt("planta_cve"));
				camara.setId(rsCamara.getInt("camara_cve"));
				camara.setDescripcion(rsCamara.getString("camara_ds"));
				camara.setNumero(rsCamara.getString("camara_abrev"));
				alCamaras.add(camara);
			}
			
			camaras = new Camara[alCamaras.size()];
			camaras = (Camara[]) alCamaras.toArray(camaras);
			
			log.debug("El SELECT para la tabla CAMARA termino satisfactoriamente...");
		} finally {
			close(psCamara);
			close(rsCamara);
		}
		
		return camaras;
	}
	
	public static int update(Connection conn, Camara camara) throws SQLException{
		int rows = -1;
		PreparedStatement psUpdate  = null;
		String            strUpdate = null;
		
		try {
			
			strUpdate = "UPDATE camara SET camara_abrev = ? , camara_ds = ? , planta_cve = ? WHERE camara_cve = ?";
			log.debug("Preparando sentencia UPDATE para la tabla CAMARA...");
			psUpdate = conn.prepareStatement(strUpdate);
			log.debug(strUpdate);
			psUpdate.setString(1, camara.getNumero());
			log.trace("camara_abrev: " + camara.getNumero());
			psUpdate.setString(2, camara.getDescripcion());
			log.trace("camara_ds: " + camara.getDescripcion());
			psUpdate.setInt(3, camara.getPlanta());
			log.trace("planta_cve: " + camara.getPlanta());
			psUpdate.setInt(4, camara.getId());
			log.trace("camara_cve: " + camara.getId());
			
			rows = psUpdate.executeUpdate();
			
			log.debug("La sentencia UPDATE para la tabla CAMARA termino satisfactoriamente.");
			
		} finally {
			close(psUpdate);
		}
		
		return rows;
	}
	
	public static synchronized int insert(Connection conn, Camara camara)
	throws SQLException {
		int rows = -1;
		
		PreparedStatement psInsert  = null;
		String            strInsert = null;
		
		try {
			
			strInsert = "INSERT INTO camara (planta_cve, camara_ds, camara_abrev) "
					+ "VALUES (?, ?, ?) ";
			log.debug("Preparando sentencia INSERT para la tabla CAMARA...");
			psInsert = conn.prepareStatement(strInsert);
			log.debug(strInsert);
			psInsert.setInt(1, camara.getPlanta());
			log.trace("planta_cve" + camara.getPlanta());
			psInsert.setString(2, camara.getDescripcion());
			log.trace("camara_ds: " + camara.getDescripcion());
			psInsert.setString(3, camara.getNumero());
			log.trace("camara_abrev: " + camara.getNumero());
			
			rows = psInsert.executeUpdate();
			
			log.debug("Registros agregados: " + rows);
			
			log.debug("La sentencia INSERT para la tabla CAMARA termino satisfactoriamente.");
			
		} finally {
			close(psInsert);
		}
		
		return rows;
	}
	
	public static synchronized int getMaxIdCamara(Connection conn)
	throws SQLException {
		int idCamara = -1;
		
		PreparedStatement psSelect  = null;
		ResultSet         rsSelect  = null;
		String            strSelect = null;
		
		try {
			strSelect = "SELECT MAX(camara_cve) AS camara_cve FROM camara";
			log.debug("Preparando sentencia SELECT para la tabla CAMARA...");
			psSelect = conn.prepareStatement(strSelect);
			log.debug(strSelect);
			rsSelect = psSelect.executeQuery();
			
			if(rsSelect.next()) {
				idCamara = rsSelect.getInt("camara_cve");
			}
			
			log.debug("La sentencia SELECT para la tabla CAMARA termino satisfactoriamente.");
			
		} finally {
			close(psSelect);
			close(rsSelect);
		}
		
		return idCamara;
	}
	
	public static int delete(Connection conn, Camara camara) throws SQLException{
		int rows = -1;
		PreparedStatement psDelete  = null;
		String            strDelete = null;
		
		try {
			
			strDelete = "DELETE FROM camara WHERE camara_cve = ?";
			log.debug("Preparando sentencia DELETE para la tabla CAMARA...");
			psDelete = conn.prepareStatement(strDelete);
			log.debug(strDelete);
			psDelete.setInt(1, camara.getId());
			log.trace("camara_cve: " + camara.getId());
			
			rows = psDelete.executeUpdate();
			
			log.debug("La sentencia DELETE para la tabla CAMARA termino satisfactoriamente.");
			
		} finally {
			close(psDelete);
		}
		
		return rows;
	}
}