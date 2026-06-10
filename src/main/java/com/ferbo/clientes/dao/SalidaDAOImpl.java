package com.ferbo.clientes.dao;

import com.ferbo.gestion.core.config.TransactionManager;
import com.ferbo.gestion.core.dao.inventario.salida.orden.SalidaDAO;
import com.ferbo.gestion.core.model.inventario.salida.orden.Salida;
import com.ferbo.gestion.core.model.inventario.salida.orden.SalidaDetalle;
import java.time.LocalDate;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SalidaDAOImpl extends SalidaDAO
{
    private static Logger log = LogManager.getLogger(SalidaDAOImpl.class);
    
    public SalidaDAOImpl(TransactionManager transactManager) {
        super(transactManager);
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
    
}
