package com.ferbo.clientes.business;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import com.ferbo.clientes.mail.beans.Planta;
import com.ferbo.clientes.manager.EmisionSalidasDAO;
import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.model.Inventario;
import com.ferbo.clientes.util.ClientesException;

public class InventarioBL {

	private final static EmisionSalidasDAO emisionSalidasDAO = new EmisionSalidasDAO();

	public static List<Inventario> obtenerInventario(Connection conn, Cliente cliente, Planta planta)
			throws ClientesException {
		Date fecha = new Date();
		if (cliente == null)
			throw new ClientesException("Debe indicar un cliente");

		if (planta == null)
			throw new ClientesException("Debe indicar una planta");

		return emisionSalidasDAO.getInventario(conn, fecha, cliente, planta.getId());
	}

}
