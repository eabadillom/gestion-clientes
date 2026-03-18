package com.ferbo.clientes.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.mail.beans.Contacto;
import com.ferbo.clientes.mail.mngr.ContactoDAO;
import com.ferbo.clientes.manager.ClienteContactoDAO;
import com.ferbo.clientes.manager.ClienteDAO;
import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.model.ClienteContacto;
import com.ferbo.clientes.util.ClientesException;
import com.ferbo.clientes.util.Conexion;
import com.ferbo.clientes.util.SecurityUtil;

@Named(value = "mbLogin")
@ViewScoped
public class MbLogin implements Serializable {

	private static final long serialVersionUID = -4875305495150857361L;
	private Cliente cliente;
    private ClienteContacto clienteContacto;
    
    private FacesContext faceContext;
    private HttpServletRequest request;
    private HttpSession session;
    
    private static Logger log = LogManager.getLogger(MbLogin.class);
    
    public MbLogin() {
    	log.info("Iniciando MbLogin...");
    }
    
    @PostConstruct
    public void init() {
    	cliente = new Cliente();
    	clienteContacto = new ClienteContacto();
    	faceContext = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        session = request.getSession();
    }

    public void login() {
    	FacesMessage message = null;
    	ClienteContactoDAO clienteContactoManager = null;
    	ClienteDAO clienteManager = null;
    	ClienteContacto usuario = null;
    	Contacto contacto = null;
    	Connection conn = null;
    	
    	
    	try {
                faceContext = FacesContext.getCurrentInstance();
    		log.info("Autenticando al usuario {}", this.clienteContacto.getUsuario());
    		conn = Conexion.getConnection();
    		clienteContactoManager = new ClienteContactoDAO();
    		clienteManager = new ClienteDAO();
        	
        	usuario = clienteContactoManager.get(conn, clienteContacto.getUsuario());
        	
        	if(usuario == null) {
        		this.espera();
        		throw new ClientesException("Usuario o contraseña incorrecto.");
        	}
        	
        	log.debug("Buscando al usuario en la base de datos...");
        	contacto = ContactoDAO.get(conn, usuario.getIdContacto());
        	log.debug("Contacto: {}", contacto);
        	usuario.setContacto(contacto);
        	
        	if(usuario.getUsuario().equals(clienteContacto.getUsuario()) == false) {
        		this.espera();
        		throw new ClientesException("Usuario o contraseña incorrecto.");
        	}
        	
        	String password = clienteContacto.getPassword();
        	password = new SecurityUtil().getSHA512(password);
        	
        	if( usuario.getPassword().equals(password) == false) {
        		this.espera();
        		throw new ClientesException("Usuario o contraseña incorrecto.");
        	}
        	
        	if(usuario.getStatus().equalsIgnoreCase("L")) {
        		this.espera();
        		throw new ClientesException("Usuario bloqueado.");
        	}
        	
        	if(usuario.getStatus().equalsIgnoreCase("B")) {
        		this.espera();
        		throw new ClientesException("Usuario o contraseña incorrecto.");
        	}
        	
        	if(usuario.getStatus().equalsIgnoreCase("A") == false) {
        		this.espera();
        		throw new ClientesException("Usuario o contraseña incorrecto.");
        	}
        	
        	cliente = clienteManager.get(conn,  usuario.getIdCliente());
            session.setAttribute("idCliente", cliente.getIdCliente());
            session.setAttribute("cliente", cliente);
            session.setAttribute("usuario", usuario);
            log.info("Usuario autenticado correctamente: {}", usuario.getUsuario());
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso correcto", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            faceContext.getExternalContext().redirect("inventario.xhtml");
    	} catch(ClientesException ex) {
    		log.error("Problema con la validación del usuario: " + this.getClienteContacto().getUsuario(), ex);
    		clienteContacto = new ClienteContacto();
    		cliente = new Cliente();
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, ex.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
    	} catch(Exception ex) {
    		log.error("Problema con la validación del usuario: " + this.getClienteContacto().getUsuario(), ex);
    		clienteContacto = new ClienteContacto();
    		cliente = new Cliente();
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error en la validación del usuario.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
    	} finally {
    		Conexion.close(conn);
    	}
    }
    
    public void espera() throws InterruptedException {
		TimeUnit.SECONDS.sleep(3);
	}
    
    public void logout() {
    	ClienteContacto clienteContacto = null;
    	
    	try {
//    		clienteContacto = (ClienteContacto)session.getAttribute("usuario");
//    		log.info("El usuario intenta finalizar su sesión: " + clienteContacto.getUsuario());
    		session = request.getSession(false);
    		session.setAttribute("usuario", null);
    		session.setAttribute("idCliente", null);
    		session.invalidate();
    		faceContext.getExternalContext().redirect("login.xhtml");
    	} catch(Exception ex) {
    		log.error("Problema en el cierre de sesión del usuario...", ex);
    	} finally {
    		
    	}
    }

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public ClienteContacto getClienteContacto() {
		return clienteContacto;
	}

	public void setClienteContacto(ClienteContacto clienteContacto) {
		this.clienteContacto = clienteContacto;
	}
	
}
