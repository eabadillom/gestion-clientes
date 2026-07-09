package com.ferbo.clientes.dao;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import com.ferbo.gestion.core.config.TransactionManager;
import com.ferbo.gestion.core.dao.inventario.salida.orden.StatusSalidaDAO;


@Named
@Dependent
public class StatusSalidaDAOExt extends StatusSalidaDAO {

	@Inject
	public StatusSalidaDAOExt(TransactionManager manager) {
		super(manager);
	}
}
