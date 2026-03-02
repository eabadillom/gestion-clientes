package com.ferbo.clientes.commons.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author alberto
 * @param <T>
 * @param <ID>
 */
public interface BaseDAO<T, ID> 
{
    T toModel(ResultSet rs) throws SQLException;
    
    T findById(Connection conn, ID id) throws SQLException;
    
    List<T> findAll(Connection conn) throws SQLException;

    int save(Connection conn, T entity) throws SQLException;

    int update(Connection conn, T entity) throws SQLException;

    int delete(Connection conn, T entity) throws SQLException;
}
