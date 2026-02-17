package com.ferbo.clientes.beans;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@RequestScoped
public class MbMessages {
	public void infoLogin() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Acceso Exitoso!!!"));
    }
    
    public void warnLogin() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Verifique su usuario y/o contraseña."));
    }
    
    public void errorLogin() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Acceso Denegado.\nFavor de contactar al Administrador."));
    }
}
