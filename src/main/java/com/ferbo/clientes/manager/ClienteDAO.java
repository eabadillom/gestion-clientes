/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ferbo.clientes.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.model.Cliente;

/**
 *
 * @author Ernesto Ramirez eralrago@gmail.com
 */
public class ClienteDAO extends DAO {

	private static Logger log = LogManager.getLogger(ClienteDAO.class);

    private static final String SELECT = "SELECT CTE_CVE, CTE_NOMBRE, CTE_RFC, numero_cte, cte_mail, habilitado, COD_UNICO, tp_persona, cd_regimen, cd_uso_cfdi, cd_metodo_pago, cd_forma_pago, nb_regimen_capital, uuid FROM cliente ";
    private static final String INSERT = "INSERT INTO cliente "
            + "(cte_cve, cte_nombre, cte_rfc, numero_cte, cte_mail, habilitado) "
            + "VALUES "
            + "(?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE cliente SET "
            + "cte_nombre = ?, cte_rfc = ?, numero_cte = ?, cte_mail = ?, habilitado = ? "
            + "WHERE cte_cve = ? ";
    
    private static final String SELECT_LOGIN = "SELECT * FROM cliente WHERE habilitado = 1 AND CTE_NOMBRE = ? AND CTE_RFC = ?";

    public void llenaBean(ResultSet rs, Cliente cliente) throws SQLException {
        cliente.setIdCliente(rs.getInt("cte_cve"));
        cliente.setNombre(rs.getString("cte_nombre"));
        cliente.setRFC(rs.getString("cte_rfc"));
        cliente.setNumero(rs.getString("numero_cte"));
        cliente.setEmail(rs.getString("cte_mail"));
        cliente.setHabilitado(rs.getBoolean("habilitado"));
        cliente.setCodigoUnico(rs.getString("COD_UNICO"));
        cliente.setTipoPersona(rs.getString("tp_persona"));
        cliente.setClaveRegimen(rs.getString("cd_regimen"));
        cliente.setClaveUsoCFDI(rs.getString("cd_uso_cfdi"));
        cliente.setClaveMetodoPago(rs.getString("cd_metodo_pago"));
        cliente.setClaveFormaPago(rs.getString("cd_forma_pago"));
        cliente.setRegimenCapital(rs.getString("nb_regimen_capital"));
    }

    public int getMaxId(Connection conn)
            throws SQLException {
        int maxId = -1;
        String sql = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            sql = "SELECT max(cte_cve) as cte_cve FROM cliente";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                maxId = rs.getInt("cte_cve");
            }
        } finally {
            close(rs);
            close(ps);
        }
        return maxId;
    }

    public int insert(Connection conn, Cliente bean)
            throws SQLException {
        int rows = -1;
        PreparedStatement ps = null;
        int idx = 1;
        int idCliente = -1;
        try {
            idCliente = getMaxId(conn);
            bean.setIdCliente(++idCliente);
            bean.setHabilitado(true);
            ps = conn.prepareStatement(INSERT);
            ps.setInt(idx++, bean.getIdCliente());
            ps.setString(idx++, bean.getNombre());
            ps.setString(idx++, bean.getRFC());
            ps.setString(idx++, bean.getNumero());
            ps.setString(idx++, bean.getEmail());
            ps.setBoolean(idx++, bean.isHabilitado());
            rows = ps.executeUpdate();
        } finally {
            close(ps);
        }
        return rows;
    }

    public int update(Connection conn, Cliente bean)
            throws SQLException {
        int rows = -1;
        PreparedStatement ps = null;
        int idx = 1;
        try {
            ps = conn.prepareStatement(UPDATE);
            ps.setString(idx++, getTrim(bean.getNombre()));
            ps.setString(idx++, getTrim(bean.getRFC()));
            ps.setString(idx++, getTrim(bean.getNumero()));
            ps.setString(idx++, getTrim(bean.getEmail()));
            ps.setBoolean(idx++, bean.isHabilitado());
            ps.setInt(idx++, bean.getIdCliente());
            rows = ps.executeUpdate();
        } finally {
            close(ps);
        }
        return rows;
    }

    public Cliente[] get(Connection conn) throws SQLException {
        Cliente[] clientes = null;
        PreparedStatement psSelect = null;
        ResultSet rsSelect = null;
        String sql = null;
        ArrayList<Cliente> alClientes = null;
        Cliente cliente = null;
        try {
            sql = SELECT + " ORDER BY cte_nombre";
            psSelect = conn.prepareStatement(sql);
            log.debug("Preparando sentencia SELECT para la tabla CLIENTE...");
            log.debug(sql);
            rsSelect = psSelect.executeQuery();
            alClientes = new ArrayList<Cliente>();
            while (rsSelect.next()) {
                cliente = new Cliente();
                llenaBean(rsSelect, cliente);
                alClientes.add(cliente);
            }
            clientes = new Cliente[alClientes.size()];
            clientes = (Cliente[]) alClientes.toArray(clientes);
            log.debug("La sentencia SELECT para la tabla CLIENTE termino satisfactoriamente.");
        } finally {
            close(psSelect);
            close(rsSelect);
        }
        return clientes;
    }

    public Cliente get(Connection conn, int idCliente) throws SQLException {
        Cliente cliente = null;
        PreparedStatement psSelect = null;
        ResultSet rsSelect = null;
        String sql = null;
        try {
            sql = SELECT + " WHERE cte_cve = ?";
            psSelect = conn.prepareStatement(sql);
            log.debug("Preparando sentencia SELECT para la tabla CLIENTE...");
            log.debug(sql);
            psSelect.setInt(1, idCliente);
            log.trace("cte_cve: " + idCliente);
            rsSelect = psSelect.executeQuery();
            if (rsSelect.next()) {
                cliente = new Cliente();
                llenaBean(rsSelect, cliente);
            }
            if (cliente == null) {
                log.warn("El idCliente no fue encontrado: " + idCliente);
            }
            log.debug("La sentencia SELECT para la tabla CLIENTE termino satisfactoriamente.");
        } finally {
            close(psSelect);
            close(rsSelect);
        }
        return cliente;
    }

    public Cliente get(Connection conn, String numeroCliente) throws SQLException {
        Cliente cliente = null;
        PreparedStatement psSelect = null;
        ResultSet rsSelect = null;
        String strSelect = null;
        try {
            strSelect = SELECT + " WHERE numero_cte = ?";
            psSelect = conn.prepareStatement(strSelect);
            log.debug("Preparando sentencia SELECT para la tabla CLIENTE...");
            log.debug(strSelect);
            psSelect.setString(1, numeroCliente);
            log.trace("numero_cte: " + numeroCliente);
            rsSelect = psSelect.executeQuery();
            if (rsSelect.next()) {
                cliente = new Cliente();
                llenaBean(rsSelect, cliente);
            }
            if (cliente == null) {
                log.warn("El idCliente no fue encontrado: " + numeroCliente);
            }
            log.debug("La sentencia SELECT para la tabla CLIENTE termino satisfactoriamente.");
        } finally {
            close(psSelect);
            close(rsSelect);
        }
        return cliente;
    }

    public int disable(Connection conn, int idCliente, boolean habilitado)
            throws SQLException {
        int rows = -1;
        String sqlUpdate = null;
        PreparedStatement psUpdate = null;
        try {
            sqlUpdate = "UPDATE cliente SET habilitado = ? WHERE cte_cve = ? ";
            psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setBoolean(1, habilitado);
            psUpdate.setInt(2, idCliente);
            rows = psUpdate.executeUpdate();
        } finally {
            close(psUpdate);
        }
        return rows;
    }

    public String[] getRFC(Connection conn)
            throws SQLException {
        String[] rfcList = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        ArrayList<String> alRFCList = null;
        String rfc = null;
        try {
            sql = "SELECT CTE_RFC FROM cliente WHERE LTRIM(RTRIM(CTE_RFC)) != '' AND CLIENTE.habilitado = 1 ORDER BY CTE_RFC ";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            alRFCList = new ArrayList<String>();
            while (rs.next()) {
                rfc = rs.getString("CTE_RFC");
                alRFCList.add(rfc);
            }
            rfcList = new String[alRFCList.size()];
            rfcList = (String[]) alRFCList.toArray(rfcList);
        } finally {
            close(ps);
            close(rs);
        }
        return rfcList;
    }

    public Cliente[] findBy(Connection conn, String numeroCte, String nombreCte, String RFC, boolean isHabilitado)
            throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        Cliente[] clientes = null;
        Cliente cliente = null;
        int idx = 1;
        List<Cliente> alClientes = null;

        try {
            sql = SELECT + "WHERE habilitado = ? ";
            if (numeroCte != null && numeroCte.trim().length() > 0) {
                sql += "AND numero_cte LIKE ? ";
            }
            if (nombreCte != null && nombreCte.trim().length() > 0) {
                sql += "AND cte_nombre LIKE ? ";
            }
            if (RFC != null && RFC.trim().length() > 0) {
                sql += "AND cte_rfc LIKE ? ";
            }
            ps = conn.prepareStatement(sql);
            ps.setBoolean(idx++, isHabilitado);
            if (numeroCte != null && numeroCte.trim().length() > 0) {
                ps.setString(idx++, "%" + numeroCte + "%");
            }
            if (nombreCte != null && nombreCte.trim().length() > 0) {
                ps.setString(idx++, "%" + nombreCte + "%");
            }
            if (RFC != null && RFC.trim().length() > 0) {
                ps.setString(idx++, "%" + RFC + "%");
            }
            rs = ps.executeQuery();
            alClientes = new ArrayList<Cliente>();
            while (rs.next()) {
                cliente = new Cliente();
                llenaBean(rs, cliente);
                alClientes.add(cliente);
            }
            clientes = new Cliente[alClientes.size()];
            clientes = (Cliente[]) alClientes.toArray(clientes);
        } finally {
            close(ps);
            close(rs);
        }
        return clientes;
    }

    public Cliente login(Cliente cliente, Connection connection) throws SQLException {
    	PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(SELECT_LOGIN);
            preparedStatement.setString(1, cliente.getNombre());
            preparedStatement.setString(2, cliente.getRFC());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                cliente = new Cliente();
                cliente.setIdCliente(resultSet.getInt("cte_cve"));
                cliente.setNombre(resultSet.getString("cte_nombre"));
                cliente.setRFC(resultSet.getString("cte_rfc"));
                cliente.setNumero(resultSet.getString("numero_cte"));
                cliente.setEmail(resultSet.getString("cte_mail"));
                cliente.setHabilitado(resultSet.getBoolean("habilitado"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            close(resultSet);
			close(preparedStatement);
        }
		return cliente;
    }

}
