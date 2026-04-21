package com.ferbo.clientes.business;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.mail.beans.ConstanciaDeposito;
import com.ferbo.clientes.mail.mngr.ConstanciaDepositoDAO;
import com.ferbo.clientes.util.ClientesException;
import com.ferbo.clientes.util.Conexion;

public class ConstanciaDepositoBL {
	private static final Logger log = LogManager.getLogger(ConstanciaDepositoBL.class);
	
	private final Integer idCliente;
	
	public ConstanciaDepositoBL(Integer idCliente) {
		this.idCliente = idCliente;
	}
	
	
	public Optional<ConstanciaDeposito> buscar(String folioCliente) {
		Optional<ConstanciaDeposito> optional;
		ConstanciaDeposito entrada;
		Connection conn = null;
		
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
			
			optional = Optional.of(entrada);
		} catch(ClientesException ex) {
			log.warn("Problema para obtener la constancia de deposito {}: {}", folioCliente, ex.getMessage());
			optional = Optional.empty();
		} catch(SQLException ex) {
			log.warn("Problema para obtener la constancia de deposito...", ex);
			optional = Optional.empty();
		} catch(Exception ex) {
			log.error("Problema para obtener la constancia de deposito...", ex);
			optional = Optional.empty();
		} finally {
			Conexion.close(conn);
		}
		
		return optional;
	}
}
