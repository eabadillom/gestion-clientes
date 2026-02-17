package com.ferbo.clientes.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.model.ClienteContacto;

@Named(value = "sidebarBean")
@SessionScoped
public class SideBarBean implements Serializable {

	private static final long serialVersionUID = -2302339226402372523L;
	private static Logger log = LogManager.getLogger(SideBarBean.class);
	
	private FacesContext faceContext;
    private HttpServletRequest request;
    private HttpSession session;
    
    private Cliente cliente;
    private ClienteContacto clienteContacto;
    
	@PostConstruct
    public void init() {
		try {
			faceContext = FacesContext.getCurrentInstance();
			request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
			session = request.getSession(false);
			
			cliente = (Cliente) session.getAttribute("cliente");
			clienteContacto = (ClienteContacto) session.getAttribute("usuario");
			
			log.info("Cliente conectado: {}", this.cliente.getNombre());
			log.info("Usuario conectado: {} {} {}", this.clienteContacto.getContacto().getNombre(), this.clienteContacto.getContacto().getApellido1(), this.clienteContacto.getContacto().getApellido2());
		} catch(Exception ex) {
			log.error("Problema para obtener los datos de sesión del usuario...", ex);
		}
    }
    
	public void logout() {
    	try {
    		clienteContacto = (ClienteContacto)session.getAttribute("usuario");
    		log.info("El usuario intenta finalizar su sesión: " + clienteContacto.getUsuario());
    		
    		session.setAttribute("usuario", null);
    		session.setAttribute("idCliente", null);
    		session.invalidate();
    		faceContext.getExternalContext().redirect("login.xhtml");
    	} catch(Exception ex) {
    		log.error("Problema en el cierre de sesión del usuario...", ex);
    	} finally {
    		
    	}
    }

	public ClienteContacto getClienteContacto() {
		return clienteContacto;
	}

	public void setClienteContacto(ClienteContacto clienteContacto) {
		this.clienteContacto = clienteContacto;
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}
