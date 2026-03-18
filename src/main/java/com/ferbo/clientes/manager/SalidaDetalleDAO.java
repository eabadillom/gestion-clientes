package com.ferbo.clientes.manager;

import com.ferbo.clientes.commons.dao.BaseDAO;
import com.ferbo.clientes.model.SalidaDetalle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class SalidaDetalleDAO extends DAO implements BaseDAO<SalidaDetalle, Integer>
{
    private static Logger log = LogManager.getLogger(SalidaDetalleDAO.class);
    
    private static final String SELECT = "select sd.cd_salida_det, sd.cd_salida, sd.partida_cve, sd.nu_cantidad, sd.ct_peso_aprox from salida_detalle sd ";
    private static final String INSERT = "insert into salida_detalle (cd_salida, partida_cve, nu_cantidad, ct_peso_aprox) values(?, ?, ?, ?)";
    private static final String UPDATE = "update salida_detalle set cd_salida = ?, partida_cve = ?, nu_cantidad = ?, ct_peso_aprox = ? ";
    
    @Override
    public synchronized SalidaDetalle toModel(ResultSet rs ) throws SQLException 
    {
        SalidaDetalle bean = new SalidaDetalle();

        bean.setIdSalidaDetalle(rs.getInt("cd_salida_det"));
        bean.setIdSalida(rs.getInt("cd_salida"));
        bean.setIdPartida(rs.getInt("partida_cve"));
        bean.setCantidad(rs.getInt("nu_cantidad"));
        bean.setPesoAprox(rs.getBigDecimal("ct_peso_aprox"));
            
        return bean;
    }
    
    @Override
    public SalidaDetalle findById(Connection conn, Integer id) throws SQLException {
        SalidaDetalle bean = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            String SELECT_POR_ID = SELECT + "where sd.cd_salida_det = ?";
            ps = conn.prepareStatement(SELECT_POR_ID);
            log.debug(SELECT);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                bean = toModel(rs);
            } else {
                log.warn("No se encontró el detalle salida: {}", id);
                throw new SQLException("No se encontró el detalle salida");
            }
        } catch (SQLException ex) {
            log.error("Problema con la lectura de las salidas...", ex);
        } finally {
            close(rs);
            close(ps);
        }
        
        return bean;
    }

    @Override
    public List<SalidaDetalle> findAll(Connection conn) throws SQLException {
        List<SalidaDetalle> beans = null;
        SalidaDetalle bean = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = conn.prepareStatement(SELECT);
            log.debug(SELECT);
            rs = ps.executeQuery();
            beans = new ArrayList<>();
            
            while (rs.next()) {
                bean = toModel(rs);
                beans.add(bean);
            }
        } catch (SQLException ex) {
            log.error("Problema con la lectura de las salidas...", ex);
        } finally {
            close(rs);
            close(ps);
        }
        
        return beans;
    }
    
    public List<SalidaDetalle> findIdSalida(Connection conn, Integer idSalida){
        List<SalidaDetalle> beans = null;
        SalidaDetalle bean = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            String SELECT_POR_IDSALIDA = SELECT + "where sd.cd_salida = ?";
            ps = conn.prepareStatement(SELECT_POR_IDSALIDA);
            log.debug(SELECT_POR_IDSALIDA);
            ps.setInt(1, idSalida);
            rs = ps.executeQuery();
            beans = new ArrayList<>();
            
            while (rs.next()) {
                bean = toModel(rs);
                beans.add(bean);
            }
        } catch (SQLException ex) {
            log.error("Problema con la lectura de las salidas...", ex);
        } finally {
            close(rs);
            close(ps);
        }
        
        return beans;
    }
    
    @Override
    public int save(Connection conn, SalidaDetalle bean) throws SQLException 
    {
        int rows = -1;
        PreparedStatement ps = null;
        int idx = 1;
        try {
            ps = conn.prepareStatement(INSERT);

            ps.setInt(idx++, bean.getIdSalida());
            ps.setInt(idx++, bean.getIdPartida());
            ps.setInt(idx++, bean.getCantidad());                
            ps.setBigDecimal(idx++, bean.getPesoAprox());

            rows = ps.executeUpdate();
            log.info("Registro de salida agregado: {}", bean.toString());
        } finally {
            close(ps);
        }

        return rows;
    }
    
    @Override
    public int update(Connection conn, SalidaDetalle bean) throws SQLException 
    {
        int rows = -1;
        PreparedStatement ps = null;
        int idx = 1;
        
        try {
            String UPDATE_BY_ID = UPDATE + "where cd_salida_det = ?";
            ps = conn.prepareStatement(UPDATE_BY_ID);

            ps.setInt(idx++, bean.getIdSalidaDetalle());
            ps.setInt(idx++, bean.getIdSalida());
            ps.setInt(idx++, bean.getIdPartida());
            ps.setInt(idx++, bean.getCantidad());
            ps.setBigDecimal(idx++, bean.getPesoAprox());                
            
            setInteger(ps, idx++, bean.getIdSalidaDetalle());

            rows = ps.executeUpdate();
            log.info("Registro de salida actualizado: {}", bean.toString());
        } finally {
            close(ps);
        }

        return rows;
    }

    @Override
    public int delete(Connection conn, SalidaDetalle entity) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
