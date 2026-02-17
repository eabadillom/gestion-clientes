/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ferbo.clientes.model;

/**
 *
 * @author Ernesto Ramirez eralrago@gmail.com
 */
import java.util.Date;

import com.ferbo.clientes.mail.beans.Contacto;

public class ClienteContacto {
    
	private Integer idCliente = -1;
	private Integer idContacto = -1;
	private Boolean habilitado = null;
    private String usuario = null;
    private String password = null;
    private String status = "A";
    private Date fhAlta = null;
    private Date fhCaducaPwd = null;
    private Date fhUltimoAcceso = null;
    private Boolean statusFacturacion = null;
    private Boolean statusInventario = null;
    private Contacto contacto = null;
    
	public Integer getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}
	public Integer getIdContacto() {
		return idContacto;
	}
	public void setIdContacto(Integer idContacto) {
		this.idContacto = idContacto;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getFhAlta() {
		return fhAlta;
	}
	public void setFhAlta(Date fhAlta) {
		this.fhAlta = fhAlta;
	}
	public Date getFhCaducaPwd() {
		return fhCaducaPwd;
	}
	public void setFhCaducaPwd(Date fhCaducaPwd) {
		this.fhCaducaPwd = fhCaducaPwd;
	}
	public Date getFhUltimoAcceso() {
		return fhUltimoAcceso;
	}
	public void setFhUltimoAcceso(Date fhUltimoAcceso) {
		this.fhUltimoAcceso = fhUltimoAcceso;
	}
	
	public Contacto getContacto() {
		return contacto;
	}
	public void setContacto(Contacto contacto) {
		this.contacto = contacto;
	}
	
	@Override
	public String toString() {
		return "ClienteContacto [idCliente=" + idCliente + ", idContacto=" + idContacto + ", usuario=" + usuario
				+ ", password=" + password + ", status=" + status + ", fhAlta=" + fhAlta + ", fhCaducaPwd="
				+ fhCaducaPwd + ", fhUltimoAcceso=" + fhUltimoAcceso + "]";
	}
	public Boolean getHabilitado() {
		return habilitado;
	}
	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
	}
	public Boolean getStatusFacturacion() {
		return statusFacturacion;
	}
	public void setStatusFacturacion(Boolean statusFacturacion) {
		this.statusFacturacion = statusFacturacion;
	}
	public Boolean getStatusInventario() {
		return statusInventario;
	}
	public void setStatusInventario(Boolean statusInventario) {
		this.statusInventario = statusInventario;
	}

    
}
