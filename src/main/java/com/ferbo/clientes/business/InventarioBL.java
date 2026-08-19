package com.ferbo.clientes.business;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import com.ferbo.clientes.mail.beans.Planta;
import com.ferbo.clientes.manager.EmisionSalidasDAO;
import com.ferbo.clientes.model.Cliente;
import com.ferbo.clientes.model.Inventario;
import com.ferbo.clientes.util.ClientesException;
import com.ferbo.clientes.util.DateUtils;
import com.ferbo.clientes.util.DateUtilsException;

public class InventarioBL {

	private final static EmisionSalidasDAO emisionSalidasDAO = new EmisionSalidasDAO();

	public static List<Inventario> obtenerInventario(Connection conn, Cliente cliente, Planta planta)
			throws ClientesException {
		List<Inventario> respuesta = null;
		String cadenaBusqueda = null;
		Date fecha = new Date();
		if (cliente == null)
			throw new ClientesException("Debe indicar un cliente");

		if (planta == null)
			throw new ClientesException("Debe indicar una planta");
		
		respuesta = emisionSalidasDAO.getInventario(conn, fecha, cliente, planta.getId());
		
		for(Inventario inventario : respuesta) {
			
			cadenaBusqueda = String.format("Ingreso: %s - Folio: %s - Clave: %s - Producto: %s - Caducidad: %s - Contenedor: %s",
					getSafeDateString(inventario.getFechaIngreso()),
					inventario.getFolioCliente(),
					inventario.getCodigo(),
					inventario.getProducto(),
					getSafeDateString(inventario.getCaducidad()),
					inventario.getSap()
					
					);
			
			inventario.setCadena(cadenaBusqueda);
		}

		return respuesta;
	}
	
	private static String getSafeDateString(Date fecha) {
		String sFecha;
		try {
			sFecha = DateUtils.getString(fecha, DateUtils.FORMATO_DD_MM_YYYY);
		} catch (DateUtilsException e) {
			sFecha = null;
		}
		
		return sFecha;
	}

}
