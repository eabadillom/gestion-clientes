package com.ferbo.clientes.manager;

import com.ferbo.clientes.commons.dao.BaseDAO;
import com.ferbo.clientes.model.ServicioSalida;
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
public class ServicioSalidaDAO extends DAO implements BaseDAO<ServicioSalida, Integer>
{
    private static Logger log = LogManager.getLogger(ServicioSalidaDAO.class);
    
    private static final String SELECT = "select ss.cd_srv_salida, ss.cd_salida, ss.servicio_cve, ss.unidad_de_manejo_cve, ss.nu_cantidad from servicios_salida ss ";
    private static final String INSERT = "insert into servicios_salida (cd_salida, servicio_cve, unidad_de_manejo_cve, nu_cantidad) values(?, ?, ?, ?)";
    private static final String UPDATE = "update servicios_salida set nu_cantidad = ? ";
    
    @Override
    public synchronized ServicioSalida toModel(ResultSet rs ) throws SQLException 
    {
        ServicioSalida bean = new ServicioSalida();

        bean.setIdSalida(rs.getInt("cd_salida"));
        bean.setIdServicio(rs.getInt("servicio_cve"));
        bean.setIdUnidadDeManejo(rs.getInt("unidad_de_manejo_cve"));
        bean.setCantidad(rs.getInt("nu_cantidad"));
            
        return bean;
    }
    
    @Override
    public ServicioSalida findById(Connection conn, Integer id) throws SQLException {
        ServicioSalida bean = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            String SELECT_POR_ID = SELECT + "where ss.cd_srv_salida = ?";
            ps = conn.prepareStatement(SELECT_POR_ID);
            log.debug(SELECT);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                bean = toModel(rs);
            } else {
                log.warn("No se encontró el detalle salida: {}", id);
                throw new SQLException("No se encontró la salida");
            }
        } catch (SQLException ex) {
            log.error("Problema con la lectura de las salida detalle...", ex);
        } finally {
            close(rs);
            close(ps);
        }
        
        return bean;
    }
    
    public ServicioSalida findByParametros(Connection conn, Integer idSalida, Integer idServicio, Integer idUnidadDeManejo)
    {
        ServicioSalida bean = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            String SELECT_POR_ID = SELECT + "where ss.cd_salida = ? and ss.servicio_cve = ? and ss.unidad_de_manejo_cve = ?";
            ps = conn.prepareStatement(SELECT_POR_ID);
            log.debug(SELECT);
            ps.setInt(1, idSalida);
            ps.setInt(2, idServicio);
            ps.setInt(3, idUnidadDeManejo);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                bean = toModel(rs);
            } else {
                log.warn("No se encontró el detalle salida: {}", idSalida);
                throw new SQLException("No se encontró la salida");
            }
        } catch (SQLException ex) {
            log.error("Problema con la lectura de las salida detalle...", ex);
        } finally {
            close(rs);
            close(ps);
        }
        
        return bean;
    }

    @Override
    public List<ServicioSalida> findAll(Connection conn) throws SQLException {
        List<ServicioSalida> beans = null;
        ServicioSalida bean = null;
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
            log.error("Problema con la lectura de las salida detalle...", ex);
        } finally {
            close(rs);
            close(ps);
        }
        
        return beans;
    }
    
    public List<ServicioSalida> findByIdSalida(Connection conn, Integer idSalida)
    {
        List<ServicioSalida> beans = null;
        ServicioSalida bean = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            String SELECT_POR_ID = SELECT + "where ss.cd_salida = ?";
            
            ps = conn.prepareStatement(SELECT_POR_ID);
            ps.setInt(1, idSalida);
            log.debug(SELECT);
            rs = ps.executeQuery();
            beans = new ArrayList<>();
            
            while (rs.next()) {
                bean = toModel(rs);
                beans.add(bean);
            }
        } catch (SQLException ex) {
            log.error("Problema con la lectura de las salida detalle...", ex);
        } finally {
            close(rs);
            close(ps);
        }
        
        return beans;
    }
    
    @Override
    public int save(Connection conn, ServicioSalida bean) throws SQLException 
    {
        int rows = -1;
        PreparedStatement ps = null;
        int idx = 1;
        try {
            ps = conn.prepareStatement(INSERT);

            ps.setInt(idx++, bean.getIdSalida());
            ps.setInt(idx++, bean.getIdServicio());
            ps.setInt(idx++, bean.getIdUnidadDeManejo());                
            ps.setInt(idx++, bean.getCantidad());

            rows = ps.executeUpdate();
            log.info("Registro de salida detalle agregado: {}", bean.toString());
        } finally {
            close(ps);
        }

        return rows;
    }
    
    @Override
    public int update(Connection conn, ServicioSalida bean) throws SQLException 
    {
        int rows = -1;
        PreparedStatement ps = null;
        int idx = 1;
        
        try {
            String UPDATE_BY_ID = UPDATE + "where cd_salida = ? and servicio_cve = ? and unidad_de_manejo_cve = ?";
            ps = conn.prepareStatement(UPDATE_BY_ID);

            ps.setInt(idx++, bean.getCantidad());
            
            setInteger(ps, idx++, bean.getIdSalida());
            setInteger(ps, idx++, bean.getIdServicio());
            setInteger(ps, idx++, bean.getIdUnidadDeManejo());
            
            rows = ps.executeUpdate();
            log.info("Registro de salida actualizado: {}", bean.toString());
        } finally {
            close(ps);
        }

        return rows;
    }

    @Override
    public int delete(Connection conn, ServicioSalida entity) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
