package com.ferbo.clientes.business;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.mail.beans.ConstanciaDeposito;
import com.ferbo.clientes.mail.mngr.ConstanciaDepositoDAO;
import com.ferbo.clientes.util.ClientesException;
import com.ferbo.clientes.util.Conexion;
import com.ferbo.gestion.reports.jasper.KardexJR;
import com.ferbo.gestion.tools.GestionException;

public class KardexBL {
	private static final Logger log = LogManager.getLogger(KardexBL.class);
	
	public byte[] buscarPDF(Integer idCliente, String folioCliente)
	throws ClientesException, GestionException{
		byte[] bytes = null;
		Connection conn = null;
		ConstanciaDeposito entrada = null;
		KardexJR jr = null;
		
		String logoPath = null;
		File   fileLogoPath = null;
		
		try {
			if(idCliente == null)
				throw new ClientesException("Debe indicar un cliente.");
			
			if(folioCliente == null)
				throw new ClientesException("Debe indicar un folio de ingreso.");
			
			if(folioCliente.trim().equalsIgnoreCase(""))
				throw new ClientesException("Debe indicar un folio de ingreso.");
			
			conn = Conexion.getConnection();
			
			entrada = ConstanciaDepositoDAO.getConstanciaDeposito(conn, folioCliente.trim());
			
			if(entrada == null)
				throw new ClientesException("La entrada solicitada no existe.");
			
			//En el caso de que la entrada no pertenezca al cliente, por seguridad
			//se informa en la interfaz de usuario que la entrada no existe.
			if(entrada.getIdCliente() != idCliente) {
				log.warn("El usuario intenta acceder a una constancia que no le pertenece: {}", folioCliente);
				throw new ClientesException("La entrada solicitada no existe.");
			}
			
			logoPath = "/images/logo.png";
            fileLogoPath = new File( getClass().getResource(logoPath).getFile());
			
			jr = new KardexJR(conn, fileLogoPath.getPath());
			
			bytes = jr.getPDF(folioCliente);
			
			
			
			
		} catch(SQLException ex) {
			log.error("Problema para obtener buscar la constancia de depósito {}...\n{}", folioCliente, ex);
		} finally {
			Conexion.close(conn);
		}
		
		
		return bytes;
	}
	
	public byte[] buscarXLSX(Integer idCliente, String folioCliente)
	throws ClientesException, GestionException{
		byte[] bytes = null;
		Connection conn = null;
		ConstanciaDeposito entrada = null;
		KardexJR jr = null;
		
		String logoPath = null;
		File   fileLogoPath = null;
		
		try {
			if(idCliente == null)
				throw new ClientesException("Debe indicar un cliente.");
			
			if(folioCliente == null)
				throw new ClientesException("Debe indicar un folio de ingreso.");
			
			if(folioCliente.trim().equalsIgnoreCase(""))
				throw new ClientesException("Debe indicar un folio de ingreso.");
			
			conn = Conexion.getConnection();
			
			entrada = ConstanciaDepositoDAO.getConstanciaDeposito(conn, folioCliente.trim());
			
			if(entrada == null)
				throw new ClientesException("La entrada solicitada no existe.");
			
			//En el caso de que la entrada no pertenezca al cliente, por seguridad
			//se informa en la interfaz de usuario que la entrada no existe.
			if(entrada.getIdCliente() != idCliente) {
				log.warn("El usuario intenta acceder a una constancia que no le pertenece: {}", folioCliente);
				throw new ClientesException("La entrada solicitada no existe.");
			}
			
			logoPath = "/images/logo.png";
            fileLogoPath = new File( getClass().getResource(logoPath).getFile());
			
			jr = new KardexJR(conn, fileLogoPath.getPath());
			
			bytes = jr.getXLSX(folioCliente);
			
		} catch(SQLException ex) {
			log.error("Problema para obtener buscar la constancia de depósito {}...\n{}", folioCliente, ex);
			throw new ClientesException("Ocurrió un problema al procesar el kardex.", ex);
		} finally {
			Conexion.close(conn);
		}
		
		return bytes;
	}
}