package com.ferbo.clientes.mail.business;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.mail.beans.ClienteContacto;
import com.ferbo.clientes.mail.beans.Contacto;
import com.ferbo.clientes.mail.beans.Mail;
import com.ferbo.clientes.mail.beans.MedioContacto;
import com.ferbo.clientes.mail.mngr.ClienteContactoDAO;
import com.ferbo.clientes.mail.mngr.ContactoDAO;
import com.ferbo.clientes.mail.mngr.MailDAO;
import com.ferbo.clientes.mail.mngr.MedioContactoDAO;
import com.ferbo.clientes.util.Conexion;
import com.ferbo.clientes.util.SecurityUtil;

public class ContactosBL {
	private static Logger log = LogManager.getLogger(ContactosBL.class);
	
	private Integer idCliente = null;
	
	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}
	
	public ContactosBL(Integer idCliente) {
		this.idCliente = idCliente;
	}
	
	public ClienteContacto[] getClienteContactos(Integer idCliente) {
		ClienteContacto[] clienteContactos = null;
		Connection conn = null;
		
		try {
			conn = Conexion.getConnection();
			
			clienteContactos = this.getClienteContactos(conn, idCliente);
			
		} catch(Exception ex) {
			
		} finally {
			Conexion.close(conn);
		}
		
		
		return clienteContactos;
	}

	/**Obtiene el listado de contactos del cliente indicado por idCliente.
	 * @param conn Conexión a la base de datos.
	 * @param idCliente Id del cliente (cte_cve)
	 * @return
	 * @throws SQLException
	 */
	public ClienteContacto[] getClienteContactos(Connection conn, Integer idCliente) throws SQLException {
		ClienteContacto[] clienteContactos = null;
		clienteContactos = ClienteContactoDAO.get(conn, idCliente);
		
		for(ClienteContacto clienteContacto : clienteContactos) {
			Contacto contacto = this.getContacto(conn, clienteContacto.getIdContacto());
			clienteContacto.setContacto(contacto);
		}
		
		return clienteContactos;
	}
	
	/**Obtiene el ccontacto del cliente especificado por idContacto.
	 * El cliente se indica desde el constructor de esta clase con idCliente.
	 * @param conn
	 * @param idContacto
	 * @return
	 * @throws SQLException
	 */
	public ClienteContacto getClienteContacto(Connection conn, Integer idContacto) throws SQLException {
		ClienteContacto clienteContacto = null;
		clienteContacto = ClienteContactoDAO.get(conn, idCliente, idContacto);
		Contacto contacto = this.getContacto(idContacto);
		clienteContacto.setContacto(contacto);
		return clienteContacto;
	}
	
	public Contacto getContacto(Integer idContacto) throws SQLException {
		Contacto contacto = null;
		Connection conn = null;
		
		try {
			conn = Conexion.getConnection();
			contacto = this.getContacto(conn, idContacto);
		} catch(Exception ex) {
			log.error("Problema para obtener los datos del contacto...", ex);
		} finally {
			Conexion.close(conn);
		}
		
		return contacto;
	}
	
	public Contacto getContacto(Connection conn, Integer idContacto) throws SQLException {
		Contacto contacto = null;
		MedioContacto[] mediosContacto = null;
		contacto = ContactoDAO.get(conn, idContacto);
		mediosContacto = MedioContactoDAO.get(conn, idContacto);
		contacto.setMediosContacto(mediosContacto);
		return contacto;
	}

	public int addContacto(Contacto contacto) {
		int rows = -1;
		Connection conn = null;
		
		try {
			conn = Conexion.getConnection();
			
			rows = this.addContacto(conn, contacto, null, null);
			
			conn.commit();
		} catch(Exception ex) {
			log.error("Error al agregar el nuevo contacto", ex);
			try {
				conn.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			Conexion.close(conn);
		}
		
		return rows ;
	}

	public int addContacto(Connection conn, Contacto contacto, String usuario, String password)
	throws SQLException {
		int rows = -1;
		SecurityUtil seguridadBO = new SecurityUtil();
		
		rows = ContactoDAO.insert(conn, contacto);
		log.info("Contacto agregado correctamente: " + contacto);
		
		ClienteContacto clienteContacto = new ClienteContacto();
		clienteContacto.setIdCliente(this.idCliente);
		clienteContacto.setIdContacto(contacto.getIdContacto());
		clienteContacto.setHabilitado(true);
		clienteContacto.setUsuario(usuario);
		clienteContacto.setStatusUsuario("A");
		clienteContacto.setPassword(seguridadBO.getSHA512(password));
		clienteContacto.setFechaAlta(new Date());
		
		rows = ClienteContactoDAO.insert(conn, clienteContacto);
		log.info("Contacto asociado al cliente correctamente: " + clienteContacto);
		
		return rows;
	}

	public MedioContacto[] getMediosContacto(Integer idContacto) {
		MedioContacto[] medios = null;
		Connection conn = null;
		
		try {
			conn = Conexion.getConnection();
			
			medios = this.getMediosContacto(conn, idContacto);
		} catch(Exception ex) {
			log.error("Problema para obtener los medios de contacto...", ex);
		} finally {
			Conexion.close(conn);
		}
		
		return medios;
	}

	public MedioContacto[] getMediosContacto(Connection conn, Integer idContacto)
	throws SQLException {
		MedioContacto[] medios = null;
		medios = MedioContactoDAO.get(conn, idContacto);
		return medios;
	}
	
	public int addMail(Mail email) {
		int rows = -1;
		Connection conn = null;
		
		try {
			conn = Conexion.getConnection();
			
			rows = this.addMail(conn, email);
			conn.commit();
		} catch(Exception ex) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			Conexion.close(conn);
		}
		return rows;
	}

	public int addMail(Connection conn, Mail email)
	throws SQLException {
		int rows = -1;
		rows = MailDAO.insert(conn, email);
		return rows;
	}

	public int update(Connection conn, ClienteContacto cteContacto)
	throws SQLException{
		int rows = -1;
		rows = ClienteContactoDAO.update(conn, cteContacto);
		return rows;
	}

	
	
}
