package com.ferbo.clientes.business;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.dao.AntiguedadSaldosDAO;
import com.ferbo.clientes.dto.AntiguedadSaldosDTO;
import com.ferbo.clientes.util.ClientesException;
import com.ferbo.gestion.core.model.cliente.Cliente;
import com.ferbo.gestion.core.model.inventario.salida.orden.Salida;

@Named
@RequestScoped
public class CandadoSalidaBL {
	
	private static Logger log = LogManager.getLogger(CandadoSalidaBL.class);
	
	@Inject
	private AntiguedadSaldosDAO saldoDAO;
	
	@Inject
	private SalidaBL salidaBO;
	
	public void validarAdeudo(Cliente cliente) throws AdeudoVencidoException {
		AntiguedadSaldosDTO saldo = null;
		BigDecimal saldoVencido = null;
		Integer salidasAutorizadas = null;
		Integer totalOrdenesEnviadas = null;
		Boolean isSaldoVencido = Boolean.FALSE;
		Boolean isHabilitarSalida = Boolean.FALSE;
		LocalDate fecha = null;
		List<Salida> ordenesEnviadas = null;
		AdeudoVencidoException exception = null;
		
		try {
			if(cliente == null)
				throw new ClientesException("Debe indicar un cliente.");
			
			fecha = LocalDate.now();
			salidasAutorizadas = cliente.getCandadoSalida().getNumSalidas();
			
			saldo = saldoDAO.getSaldo(LocalDate.now(), cliente, null);
			saldoVencido = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
					.add(saldo.getAtraso8dias())
					.add(saldo.getAtraso15dias())
					.add(saldo.getAtraso30dias())
					.add(saldo.getAtraso60dias())
					.add(saldo.getAtrasoMayor60dias());
			
			isSaldoVencido = saldoVencido.compareTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) > 0;
			isHabilitarSalida = cliente.getCandadoSalida().getHabilitado();
			
			ordenesEnviadas = salidaBO.buscarEnviadas(cliente, fecha);
			totalOrdenesEnviadas = ordenesEnviadas.size();
			
			if(isSaldoVencido && isHabilitarSalida && totalOrdenesEnviadas < salidasAutorizadas) {
				log.info("El cliente tiene un adeudo, pero tiene {} salidas permitidas.", salidasAutorizadas);
				return;
			}
			
			if(isSaldoVencido.booleanValue() == false) {
				log.info("El cliente no tiene adeudos. Puede retirar su mercancía.");
				return;
			}
			
			exception = new AdeudoVencidoException("No puede solicitar más ordenes de retiro debido a que presenta un adeudo vencido. Favor de contactar a Facturación.");
			exception.setImporte(saldoVencido);
			exception.setSalidasPermitidas(salidasAutorizadas);
			exception.setSalidasSolicitadas(totalOrdenesEnviadas);
			throw exception;
		
		} catch(AdeudoVencidoException ex){
			throw ex;
		} catch(NoResultException ex) {
			log.info("El cliente no presenta adeudos.");
		} catch(Exception ex) {
			log.error("Problema para consultar la información del cliente...", ex);
		}
	}

}
