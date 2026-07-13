package com.ferbo.clientes.business;

import com.ferbo.clientes.configuracion.TransactionManagerImpl;
import com.ferbo.clientes.dao.SalidaDAOExt;
import com.ferbo.gestion.core.config.TransactionManager;
import com.ferbo.gestion.core.model.inventario.salida.orden.Salida;
import java.time.LocalDate;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsultaSalidasBL 
{
    private final static Logger log = LogManager.getLogger(ConsultaSalidasBL.class);
    
    private final TransactionManager transactManager = new TransactionManagerImpl();
    
    private final SalidaDAOExt salidaDAO = new SalidaDAOExt(transactManager);
    
    public Salida salidaPorFolio(Integer idSalida)
    {
        return salidaDAO.buscarPorId(idSalida).orElseThrow(() -> new RuntimeException("No se encontro la salida con ese identificador"));
    }
    
    public List<Salida> buscarPorClientePeriodo(Integer idCliente, LocalDate fechaIni, LocalDate fechaFin)
    {
        return salidaDAO.buscarPorClientePeriodo(idCliente, fechaIni, fechaFin);
    }
    
}
