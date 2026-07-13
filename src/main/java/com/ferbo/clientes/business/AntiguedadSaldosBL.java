package com.ferbo.clientes.business;

import java.time.LocalDate;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.ferbo.clientes.dao.AntiguedadSaldosDAO;
import com.ferbo.clientes.dto.AntiguedadSaldosDTO;
import com.ferbo.clientes.util.ClientesException;
import com.ferbo.gestion.core.model.cliente.Cliente;

@Named
@RequestScoped
public class AntiguedadSaldosBL {
	
	@Inject
	private AntiguedadSaldosDAO saldoDAO;
	
	public AntiguedadSaldosDTO saldo(LocalDate fecha, Cliente cliente, String rfcEmisor) throws ClientesException {
		if(cliente == null)
			throw new ClientesException("Debe indicar un cliente.");
		if(fecha == null)
			throw new ClientesException("Debe indicar una fecha.");
		
		//el RFC del emisor se puede omitir de la consulta.
		
		return saldoDAO.getSaldo(fecha, cliente, rfcEmisor);
	}
	
	public List<AntiguedadSaldosDTO> desglose(LocalDate fecha, Cliente cliente, String rfcEmisor) throws ClientesException {
		if(cliente == null)
			throw new ClientesException("Debe indicar un cliente.");
		if(fecha == null)
			throw new ClientesException("Debe indicar una fecha.");
		
		//el RFC del emisor se puede omitir de la consulta.
		
		return saldoDAO.getDesglose(fecha, cliente, rfcEmisor);
	}
}
