package com.ferbo.clientes.dao;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import com.ferbo.gestion.core.config.TransactionManager;
import com.ferbo.gestion.core.dao.cliente.ClienteDAO;

@Named
@Dependent
public class ClienteDAOExt extends ClienteDAO {

	@Inject
	public ClienteDAOExt(TransactionManager manager) {
		super(manager);
	}
}
