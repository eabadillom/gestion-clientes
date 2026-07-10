package com.ferbo.clientes.business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.ferbo.clientes.dao.SalidaDAOExt;
import com.ferbo.clientes.dao.StatusSalidaDAOExt;
import com.ferbo.gestion.core.model.cliente.Cliente;
import com.ferbo.gestion.core.model.inventario.salida.orden.Salida;
import com.ferbo.gestion.core.model.inventario.salida.orden.StatusSalida;

@Named
@RequestScoped
public class SalidaBL {
	
	@Inject
	private StatusSalidaDAOExt statusDAO;
	
	@Inject
	private SalidaDAOExt salidaDAO;
	
	public List<Salida> buscarEnviadas(Cliente cliente, LocalDate fecha) {
		List<Salida> ordenesEnviadas = null;
		StatusSalida enviadas = null;
		
		try {
			enviadas = statusDAO.buscarPorClave("E");
			ordenesEnviadas = salidaDAO.buscarPorStatus(cliente, fecha, enviadas);
		} catch(Exception ex) {
			ordenesEnviadas = new ArrayList<Salida>();
		}
		
		return ordenesEnviadas;
	}

}
