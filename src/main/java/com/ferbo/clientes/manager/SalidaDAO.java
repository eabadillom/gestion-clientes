package com.ferbo.clientes.manager;

import com.ferbo.clientes.commons.dao.BaseDAO;
import com.ferbo.clientes.model.Salida;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class SalidaDAO extends DAO implements BaseDAO<Salida, Integer>
{
    private static Logger log = LogManager.getLogger(SalidaDAO.class);
    
    private static final String SELECT = "select sl.cd_salida, sl.cd_folio_salida, sl.cliente_cve, sl.id_contacto, sl.cd_status, sl.nb_nombre_transportista, sl.nb_placas_transporte,\n" +
        "sl.nb_observaciones, sl.fh_salida, sl.tm_salida, sl.fh_registro, sl.fh_modificacion from salida sl ";
    private static final String INSERT = "insert into salida (cd_folio_salida, cliente_cve, id_contacto, cd_status, nb_nombre_transportista, nb_placas_transporte, nb_observaciones, fh_salida, tm_salida, fh_registro) \n" +
        "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_WITHOUT_COMMENTS = "insert into salida (cd_folio_salida, cliente_cve, id_contacto, cd_status, nb_nombre_transportista, nb_placas_transporte, fh_salida, tm_salida, fh_registro) \n" +
        "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "update salidas set cd_folio_salida = ?, cliente_cve = ?, id_contacto = ?, cd_status = ?, nb_nombre_transportista = ?, nb_placas_transporte= ?, nb_observaciones = ?, fh_salida = ?, tm_salida = ?, fh_registro = ?, fh_modificacion = ? ";
    
    @Override
    public synchronized Salida toModel(ResultSet rs ) throws SQLException 
    {
        Salida bean = new Salida();

        bean.setIdSalida(rs.getInt("cd_salida"));
        bean.setFolioSalida(rs.getString("cd_folio_salida"));
        bean.setIdCliente(rs.getInt("cliente_cve"));
        bean.setIdContacto(rs.getInt("id_contacto"));
        bean.setIdStatus(rs.getInt("cd_status"));
        bean.setNombreTransportista(rs.getNString("nb_nombre_transportista"));
        bean.setPlacasTransporte(rs.getString("nb_placas_transporte"));
        bean.setObservaciones(rs.getNString("nb_observaciones"));
        bean.setFechaSalida(rs.getDate("fh_salida"));
        bean.setHoraSalida(rs.getTime("tm_salida"));
        bean.setFechaRegistro(rs.getDate("fh_registro"));
        bean.setFechaModificacion(rs.getDate("fh_modificacion"));
            
        return bean;
    }
    
    @Override
    public Salida findById(Connection conn, Integer id) throws SQLException {
        Salida bean = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            String SELECT_POR_ID = SELECT + "where sl.cd_salida = ?";
            
            ps = conn.prepareStatement(SELECT_POR_ID);
            log.debug(SELECT_POR_ID);
            ps.setInt(1, id);
            
            rs = ps.executeQuery();
            
            if(rs.next()) {
                bean = toModel(rs);
            } else {
                log.warn("No se encontró la salida con el id: {}", id);
                throw new SQLException("No se encontró la salida con el folio dado");
            }
        } finally {
            close(rs);
            close(ps);
        }
        
        return bean;
    }
    
    public Salida findSalidasByFolio(Connection conn, String folio) throws SQLException 
    {
        Salida bean = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            String SELECT_POR_FOLIO = SELECT + "where sl.cd_folio_salida = ?";
            
            ps = conn.prepareStatement(SELECT_POR_FOLIO);
            log.debug(SELECT_POR_FOLIO);
            ps.setString(1, folio);
            
            rs = ps.executeQuery();
            
            if(rs.next()) {
                bean = toModel(rs);
            } else {
                log.warn("No se encontró la salida con el folio: {}", folio);
                throw new SQLException("No se encontró la salida con el folio dado");
            }
        } finally {
            close(rs);
            close(ps);
        }
        
        return bean;
    }

    @Override
    public List<Salida> findAll(Connection conn) {
        List<Salida> beans = null;
        Salida bean = null;
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
    
    public List<Salida> findSalidasByFecha(Connection conn, Date fecha) 
    {
        List<Salida> beans = null;
        Salida bean = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            String SELECT_POR_FECHA = SELECT + "where sl.fh_registro > ?";
            
            ps = conn.prepareStatement(SELECT_POR_FECHA);
            log.debug(SELECT_POR_FECHA);
            ps.setDate(0, getSqlDate(fecha));
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
    public int save(Connection conn, Salida entity) throws SQLException {
        int rows = -1;
        PreparedStatement ps = null;
        int idx = 1;
        try {
            if(entity.getObservaciones() == null || entity.getObservaciones().isEmpty()){
                ps = conn.prepareStatement(INSERT_WITHOUT_COMMENTS);

                ps.setString(idx++, entity.getFolioSalida());
                ps.setInt(idx++, entity.getIdCliente());
                ps.setInt(idx++, entity.getIdContacto());
                ps.setInt(idx++, entity.getIdStatus());
                ps.setString(idx++, entity.getNombreTransportista());                
                ps.setString(idx++, entity.getPlacasTransporte());
                ps.setDate(idx++, getSqlDate(entity.getFechaSalida()));
                ps.setTimestamp(idx++, new java.sql.Timestamp(entity.getHoraSalida().getTime()));
                ps.setDate(idx++, getSqlDate(entity.getFechaRegistro()));
            } else{
                ps = conn.prepareStatement(INSERT);

                ps.setString(idx++, entity.getFolioSalida());
                ps.setInt(idx++, entity.getIdCliente());
                ps.setInt(idx++, entity.getIdContacto());
                ps.setInt(idx++, entity.getIdStatus());
                ps.setString(idx++, entity.getNombreTransportista());                
                ps.setString(idx++, entity.getPlacasTransporte());
                ps.setString(idx++, entity.getObservaciones());
                ps.setDate(idx++, getSqlDate(entity.getFechaSalida()));
                ps.setTimestamp(idx++, new java.sql.Timestamp(entity.getHoraSalida().getTime()));
                ps.setDate(idx++, getSqlDate(entity.getFechaRegistro()));
            }
            rows = ps.executeUpdate();
            log.info("Registro de salida agregado: {}", entity.toString());
        } finally {
            close(ps);
        }

        return rows;
    }
    
    @Override
    public int update(Connection conn, Salida bean) throws SQLException 
    {
        int rows = -1;
        PreparedStatement ps = null;
        int idx = 1;
        
        try {
            String UPDATE_BY_ID = UPDATE + "where cd_salida = ?";
            ps = conn.prepareStatement(UPDATE_BY_ID);

            ps.setString(idx++, bean.getFolioSalida());
            ps.setInt(idx++, bean.getIdCliente());
            ps.setInt(idx++, bean.getIdContacto());
            ps.setInt(idx++, bean.getIdStatus());
            ps.setString(idx++, bean.getNombreTransportista());                
            ps.setString(idx++, bean.getPlacasTransporte());
            ps.setString(idx++, bean.getObservaciones());
            ps.setDate(idx++, getSqlDate(bean.getFechaSalida()));
            ps.setTimestamp(idx++, new java.sql.Timestamp((bean.getHoraSalida().getTime())));
            ps.setDate(idx++, getSqlDate(bean.getFechaRegistro()));
            ps.setDate(idx++, getSqlDate(bean.getFechaModificacion()));

            setInteger(ps, idx++, bean.getIdCliente());

            rows = ps.executeUpdate();
            log.info("Registro de salida actualizado: {}", bean.toString());
        } finally {
            close(ps);
        }

        return rows;
    }

    @Override
    public int delete(Connection conn, Salida entity) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
