package com.ferbo.clientes.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

/**
 *
 * @author alberto
 */
public final class FacesUtils 
{
    public static void addMessage(FacesMessage.Severity severity, String title, String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, title, msg));
    }
    
    public static void addDynamicDialogMessage(FacesMessage.Severity severity, String title, String msg){
        PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(severity, title, msg));
    }
    
    public static void requireNonNull(Object obj, String mensaje) throws ClientesException {
        if (obj == null) {
            throw new ClientesException(mensaje);
        }
    }
    
    public static <T> T requireNonNullWithReturn(T obj, String mensaje) throws ClientesException {
        if (obj == null) {
            throw new ClientesException(mensaje);
        }
        return obj;
    }

    public static String normalizar(String valor) {
        return valor == null ? "" : valor.trim().toLowerCase();
    }
    
}
