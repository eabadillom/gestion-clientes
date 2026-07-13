package com.ferbo.clientes.business;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.ferbo.clientes.dao.ClienteDAOExt;
import com.ferbo.clientes.util.ClientesException;
import com.ferbo.gestion.core.model.cliente.Cliente;

@Named
@RequestScoped
public class ClienteBL {
	
	@Inject
	private ClienteDAOExt clienteDAO;
	
	/**Buscar entidad Cliente proporcionando su ID de BBDD.
	 * @param id Primary Key del cliente.
	 * @return Si el id de cliente se encuentra registrado, devolverá al cliente.
	 * @throws ClientesException Devuelve esta excepción si el cliente no se encuentra en la BBDD.
	 */
	public Cliente buscar(Integer id) throws ClientesException {
		final Cliente cliente;
		cliente = clienteDAO.buscarPorId(id)
				.orElseThrow(() -> new ClientesException("Cliente no encontrado."));
		return cliente;
	}
}
