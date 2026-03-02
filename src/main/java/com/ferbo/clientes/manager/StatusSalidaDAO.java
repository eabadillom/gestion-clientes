package com.ferbo.clientes.manager;

import com.ferbo.clientes.commons.dao.BaseDAO;
import com.ferbo.clientes.model.StatusSalida;
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
public class StatusSalidaDAO extends DAO implements BaseDAO<StatusSalida, Integer>
{
    private static Logger log = LogManager.getLogger(StatusSalidaDAO.class);
    
    private static final String SELECT = "select sts.cd_status, sts.nb_descripcion, sts.nb_clave from status_salida sts ";
    
    @Override
    public synchronized StatusSalida toModel(ResultSet rs) throws SQLException 
    {
        StatusSalida bean = new StatusSalida();

        bean.setIdEstatus(rs.getInt("cd_status"));
        bean.setDescripcion(rs.getString("nb_descripcion"));
        bean.setClave(rs.getString("nb_clave"));
            
        return bean;
    }
    
    @Override
    public StatusSalida findById(Connection conn, Integer id) {
        StatusSalida bean = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            String SELECT_BY_CLAVE = SELECT + "where sts.cd_salida = ?";
            
            ps = conn.prepareStatement(SELECT_BY_CLAVE);
            log.debug(SELECT_BY_CLAVE);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if(rs.next()) {
                bean = toModel(rs);
            } else {
                log.warn("No se encontró status_salida con  el id: {}", id);
                throw new SQLException("No se encontró status con la clave");
            }
        } catch (SQLException ex) {
            log.error("Problema con la lectura del status de la salida...", ex);
        } finally {
            close(rs);
            close(ps);
        }
        
        return bean;
    }
    
    public StatusSalida findStatusByClave(Connection conn, String clave) 
    {
        StatusSalida bean = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            String SELECT_BY_CLAVE = SELECT + "where sts.nb_clave = ?";
            
            ps = conn.prepareStatement(SELECT_BY_CLAVE);
            log.debug(SELECT_BY_CLAVE);
            ps.setString(1, clave);
            rs = ps.executeQuery();
            
            if(rs.next()) {
                bean = toModel(rs);
            } else {
                log.warn("No se encontró status_salida con clave: {}", clave);
                throw new SQLException("No se encontró status con la clave");
            }
        } catch (SQLException ex) {
            log.error("Problema con la lectura del status de la salida...", ex);
        } finally {
            close(rs);
            close(ps);
        }
        
        return bean;
    }

    @Override
    public List<StatusSalida> findAll(Connection conn) {
        List<StatusSalida> beans = null;
        StatusSalida bean = null;
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
            log.error("Problema con la lectura de los status de salida...", ex);
        } finally {
            close(rs);
            close(ps);
        }
        
        return beans;
    }

    @Override
    public int save(Connection conn, StatusSalida entity) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int update(Connection conn, StatusSalida entity) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int delete(Connection conn, StatusSalida entity) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
