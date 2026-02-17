package com.ferbo.clientes.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.model.PreSalida;
import com.ferbo.clientes.util.Conexion;

public class PreSalidaDAO {

	private static Logger log = LogManager.getLogger(PreSalidaDAO.class);

	private static final String INSERT_PRE = "INSERT INTO pre_salida (cd_folio_salida, st_estado, fh_salida, tm_salida, nb_placa_tte, nb_operador_tte, partida_cve, folio, nu_cantidad, id_contacto) "
			+ "                               values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	public void guardar(Connection connection, List<PreSalida> listaPresalida)
	throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = connection.prepareStatement(INSERT_PRE, PreparedStatement.RETURN_GENERATED_KEYS);
			
			for (PreSalida preSalida : listaPresalida) {
				int idx = 1;
				ps.setString(idx++, preSalida.getCd_folio_salida());
				ps.setString(idx++, preSalida.getSt_estado());
				ps.setDate(idx++, new java.sql.Date (preSalida.getFh_salida().getTime()));
				ps.setTimestamp(idx++, new java.sql.Timestamp (preSalida.getTm_salida().getTime()));
				ps.setString(idx++, preSalida.getNb_placa_tte());
				ps.setString(idx++, preSalida.getNb_operador_tte());
				ps.setInt(idx++, preSalida.getPartida_cve());
				ps.setInt(idx++, preSalida.getFolio());
				ps.setInt(idx++, preSalida.getNu_cantidad());
				ps.setInt(idx++, preSalida.getIdContacto());
				
				ps.executeUpdate();
				rs = ps.getGeneratedKeys();
				if (rs.next()) {
					preSalida.setId(rs.getInt(1));
				}
				log.info("Registro pre-salida almacenado: " + preSalida.toString());
			}
		} finally {
			Conexion.close(rs);
			Conexion.close(ps);
 		}
	}
}
