package com.ferbo.clientes.beans;

import java.io.Serializable;
import java.sql.Connection;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.ferbo.clientes.manager.ClienteContactoDAO;
import com.ferbo.clientes.model.ClienteContacto;
import com.ferbo.clientes.util.ClientesException;
import com.ferbo.clientes.util.Conexion;
import com.ferbo.clientes.util.SecurityUtil;

@Named(value = "changePwd")
@ViewScoped
public class ChangePasswordBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static Logger log = LogManager.getLogger(ChangePasswordBean.class);
	private FacesContext context;
	private HttpServletRequest request;
	
	private ClienteContacto cteContacto;
	private String currentPassword;
	private String newPassword;
	private String confirmPassword;
	private SecurityUtil security;
	
	public ChangePasswordBean() {
		security = new SecurityUtil();
	}
	
	@PostConstruct
	public void init() {
		log.info("Entrando a la pantalla (init)...");
		
		try {
			context = FacesContext.getCurrentInstance();
			request = (HttpServletRequest) context.getExternalContext().getRequest();
			cteContacto = (ClienteContacto) request.getSession(false).getAttribute("usuario");
			String mensaje = String.format("Usuario %s ingresa a Registro de orden de salida.", cteContacto.toString());
			log.info(mensaje);
		} catch(Exception ex) {
			log.error("Problema al ingresar a la pantalla de cambio de contraseña...", ex);
		}
	}
	
	public void cambiaPassword() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		Connection conn = null;
		ClienteContactoDAO cteContactoManager = null;
		ClienteContacto cteContacto = null;
		String currentPasswordSHA512 = null;
		String newPasswordSHA512 = null;
		int rows = -1;
		
		try {
			cteContactoManager = new ClienteContactoDAO();
			cteContacto = cteContactoManager.get(this.cteContacto.getUsuario());
			
			if(cteContacto.getUsuario().equals(this.cteContacto.getUsuario()) == false)
				throw new ClientesException("El usuario indicado es incorrecto.");
			
			if(this.currentPassword == null || "".equalsIgnoreCase(this.currentPassword.trim()))
				throw new ClientesException("Debe indicar su contraseña actual.");
			
			currentPasswordSHA512 = security.getSHA512(this.currentPassword);
			if(cteContacto.getPassword().equals(currentPasswordSHA512) == false)
				throw new ClientesException("La contraseña actual indicada es incorrecta.");
			
			if(this.newPassword == null || "".equalsIgnoreCase(this.newPassword.trim()))
				throw new ClientesException("Debe indicar su nueva contraseña");
			
			if(this.confirmPassword == null || "".equalsIgnoreCase(this.confirmPassword.trim()))
				throw new ClientesException("Debe confirmar su nueva contraseña");
			
			security.checkPassword(this.newPassword);
			
			newPasswordSHA512 = security.getSHA512(this.newPassword);
			
			cteContacto.setPassword(newPasswordSHA512);
			
			conn = Conexion.getConnection();
			
			rows = cteContactoManager.update(conn, cteContacto);
			conn.commit();
			
			log.info("Registros actualizados: " + rows);
			
			this.currentPassword = null;
			this.newPassword = null;
			this.confirmPassword = null;
			
			mensaje = "Su contraseña se actualizó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			
		} catch(ClientesException ex) {
			Conexion.rollback(conn);
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema con la emisión de salidas...", ex);
			Conexion.rollback(conn);
			mensaje = "Su solicitud no se pudo generar.\nFavor de comunicarse con el administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			Conexion.close(conn);
			message = new FacesMessage(severity, "Emisión de salida", mensaje);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update(":form:messages", ":form:currentPassword", ":form:newPassword", ":form:confirmPassword");
		}
	}
	
	public void validateNewPassword() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		
		try {
			if(this.newPassword == null)
				throw new ClientesException("Debe indicar una contraseña nueva.");
			
			if(this.confirmPassword == null)
				throw new ClientesException("Debe confirmar su contraseña nueva.");
			
			if(newPassword.equals(confirmPassword) == false)
				throw new ClientesException("Su nueva contraseña no coincide en los dos campos.");
			
			mensaje = "Su nueva contraseña coincide correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(ClientesException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			log.error("Problema con la emisión de salidas...", ex);
			mensaje = "Su solicitud no se pudo generar.\nFavor de comunicarse con el administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, "Emisión de salida", mensaje);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update(":form:messages");
		}
	}
	
	
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	
}
