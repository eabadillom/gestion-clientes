package com.ferbo.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogOutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(LogOutServlet.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogOutServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		log.info("Cerrando la sesión del usuario...");
		HttpSession session = request.getSession(false);
		log.info(request.getContextPath());
		
		try {
			if(session != null)
				session.invalidate();
			
		} catch(IllegalStateException ex) {
			log.error("Problema con el cierre de la sesión...", ex);
		}
		
		response.sendRedirect(request.getContextPath());
		log.info("Sesión terminada.");
	}
}
