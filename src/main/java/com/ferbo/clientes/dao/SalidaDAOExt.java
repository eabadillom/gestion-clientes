package com.ferbo.clientes.dao;

import java.time.LocalDate;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.gestion.core.config.TransactionManager;
import com.ferbo.gestion.core.dao.inventario.salida.orden.SalidaDAO;
import com.ferbo.gestion.core.model.cliente.Cliente;
import com.ferbo.gestion.core.model.inventario.salida.orden.Salida;
import com.ferbo.gestion.core.model.inventario.salida.orden.SalidaDetalle;
import com.ferbo.gestion.core.model.inventario.salida.orden.StatusSalida;

@Named
@Dependent
public class SalidaDAOExt extends SalidaDAO {
    private static Logger log = LogManager.getLogger(SalidaDAOExt.class);
    
    @Inject
    public SalidaDAOExt(TransactionManager manager) {
        super(manager);
    }

    public List<Salida> buscarPorClientePeriodo(Integer idCliente, LocalDate fechaInicio, LocalDate fechaFin) {
        return transactManager.executeRead(em -> {
            String sql = "SELECT s FROM Salida s WHERE s.cliente.id = :idCliente AND s.fechaRegistro BETWEEN :fInicio AND :fFinal";
            
            List<Salida> listSalidas= em.createQuery(sql, Salida.class)
                .setParameter("idCliente", idCliente)
                .setParameter("fInicio", fechaInicio)
                .setParameter("fFinal", fechaFin)
                .getResultList();
            
            for(Salida auxSalida : listSalidas){
                log.debug(auxSalida.getDetalles());
                for(SalidaDetalle auxSalidaDetalle : auxSalida.getDetalles()){
                    log.debug(auxSalidaDetalle.getPartida());
                    log.debug(auxSalidaDetalle.getPartida().getUnidadProducto());
                    log.debug(auxSalidaDetalle.getPartida().getUnidadProducto().getUnidadManejo());
                    log.debug(auxSalidaDetalle.getPartida().getUnidadProducto().getProducto());
                }
            }
            
            return listSalidas;
        });
    }
    
    /**Busca las ordenes de retiro del cliente que tengan status ENVIADA, a partir de la fecha indicada.
     * @param cliente Cliente al que pertenecen las ordenes de retiro.
     * @param fecha Todas las ordenes de retiro que sean del día o programadas a futuro.
     * @param status Status de la orden de retiro (E: Enviada, A: Aceptada, C: Cancelada).
     * @return Lista de las ordenes de retiro.
     */
    public List<Salida> buscarPorStatus(Cliente cliente, LocalDate fecha, StatusSalida status) {
    	return transactManager.executeRead(em -> {
    		String query = "SELECT s FROM Salida s WHERE s.cliente = :cliente AND s.fechaRegistro >= :fecha AND s.status = :status";
    		List<Salida> modelList = em.createQuery(query, Salida.class)
    				.setParameter("cliente", cliente)
    				.setParameter("fecha", fecha)
    				.setParameter("status", status)
    				.getResultList();
    		
    		return modelList;
    	});
    }
}
