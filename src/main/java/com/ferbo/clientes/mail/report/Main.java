package com.ferbo.clientes.mail.report;

import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.clientes.util.Conexion;

public class Main {
	private static Logger log = LogManager.getLogger(Main.class);
	public static void main(String[] args) {
		Connection conn = null;
		SendMailOrdenSalida ordenSalidaBO = null;
		String folio = null;
		try {
			folio = "CA0007";
			conn = Conexion.getConnection();
			ordenSalidaBO = new SendMailOrdenSalida(conn);
			ordenSalidaBO.exec(folio);
		} catch(Exception ex) {
			log.error("Problema para procesar la impresion de la orden de salida.", ex);
		} finally {
			Conexion.close(conn);
		}
	}
}
