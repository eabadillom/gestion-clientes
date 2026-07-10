package com.ferbo.clientes.mail.report;

import com.ferbo.clientes.configuracion.TransactionManagerImpl;
import com.ferbo.clientes.mail.beans.Correo;
import com.ferbo.clientes.util.MailHelper;
import com.ferbo.gestion.core.config.TransactionManager;
import com.ferbo.gestion.core.dao.catalogo.almacen.PlantaDAO;
import com.ferbo.gestion.core.dao.cliente.ClienteDAO;
import com.ferbo.gestion.core.dao.cliente.contacto.ClienteContactoDAO;
import com.ferbo.gestion.core.dao.cliente.contacto.MedioContactoDAO;
import com.ferbo.gestion.core.model.catalogo.almacen.Camara;
import com.ferbo.gestion.core.model.catalogo.almacen.Planta;
import com.ferbo.gestion.core.model.cliente.Cliente;
import com.ferbo.gestion.core.model.cliente.contacto.ClienteContacto;
import com.ferbo.gestion.core.model.cliente.contacto.Mail;
import com.ferbo.gestion.core.model.cliente.contacto.MedioContacto;
import com.ferbo.gestion.core.model.inventario.entrada.ConstanciaDeposito;
import com.ferbo.gestion.core.model.inventario.entrada.Partida;
import com.ferbo.gestion.core.model.inventario.salida.orden.Salida;
import com.ferbo.gestion.core.model.inventario.salida.orden.SalidaDetalle;
import com.ferbo.gestion.core.model.sistema.Usuario;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SendMailCancelacionOrdenSalida extends Thread {
	private static Logger log = LogManager.getLogger(SendMailCancelacionOrdenSalida.class);
	private Cliente cliente = null;
	private Salida salidaSelected = null;

	private final TransactionManager transactManager = new TransactionManagerImpl();
	private final PlantaDAO plantaDAO = new PlantaDAO(transactManager);
	private final ClienteDAO clienteDAO = new ClienteDAO(transactManager);
	private final ClienteContactoDAO clienteContactoDAO = new ClienteContactoDAO(transactManager);
	private final MedioContactoDAO medioContactoDAO = new MedioContactoDAO(transactManager);

	@Override
	public void run() {
		try {
			this.exec();
		} catch (Exception ex) {
			log.error("Problema para el envío de la orden de salida por correo electrónico...", ex);
		}
	}

	public void exec() {
		try {
			List<SalidaDetalle> listSalidaDetalle = salidaSelected.getDetalles();
			List<Integer> listPlantas = new ArrayList<Integer>();

			for (SalidaDetalle salidaDetalle : listSalidaDetalle) {
				Partida partida = salidaDetalle.getPartida();
				ConstanciaDeposito constanciaDeposito = partida.getConstanciaDeposito();

				if (cliente == null)
					cliente = clienteDAO.obtenerPorId(constanciaDeposito.getCliente().getId());

				Camara camara = partida.getCamara();
				Planta planta = camara.getPlanta();

				if (listPlantas.contains(planta.getId())) {
					continue;
				}

				listPlantas.add(planta.getId());
			}

			for (Integer idPlanta : listPlantas) {
				this.processCancelacion(salidaSelected.getFolio(), idPlanta);
			}
		} catch (Exception ex) {
			log.error("Problema general para cancelar la orden de salida...", ex);
		}
	}

	private void processCancelacion(String folio, Integer idPlanta) {
		String mailInventarioHTML = null;
		File mailInventarioFile = null;
		FileReader mailInventarioReader = null;
		BufferedReader reader = null;
		StringBuilder sb = null;
		String subject = null;

		log.info(String.format("Iniciando envío de correo para la cancelación de la orden de salida %s, idPlanta %d",
				folio, idPlanta));

		try {
			subject = String.format("Orden de salida %s (CANCELACIÓN)", folio);
			log.info("Preparando para ejecutar el correo de la cancelación...");

			mailInventarioHTML = "/mail/mailCancelarOrdenSalida.html";
			mailInventarioFile = new File(getClass().getResource(mailInventarioHTML).getFile());
			log.info("Ruta html mail: " + mailInventarioFile.getPath());
			if (mailInventarioFile.exists() == false)
				log.error("El archivo no existe: " + mailInventarioFile.getPath());

			mailInventarioReader = new FileReader(mailInventarioFile);
			reader = new BufferedReader(mailInventarioReader);
			sb = new StringBuilder();
			String linea = null;
			while ((linea = reader.readLine()) != null) {
				sb.append(linea);
			}

			String variable = "${folio}";
			int inicio = sb.indexOf(variable);
			if (inicio != -1) {
				sb.replace(inicio, inicio + variable.length(), folio);
			}

			String html = sb.toString();
			MailHelper mailUtil = new MailHelper();

			List<ClienteContacto> cteContactos = this.clienteContactoDAO.obtenerPorIdCliente(this.cliente);
			Correo buzon = null;
			log.info("Leyendo contactos del cliente...");
			for (ClienteContacto cteContacto : cteContactos) {
				log.info("Contacto: " + cteContacto.getContacto().getNombre() + " "
						+ cteContacto.getContacto().getPrimerApellido());
				if (cteContacto.getHabilitado() == false)
					continue;

				List<MedioContacto> medios = medioContactoDAO.buscarPorIdContacto(cteContacto);
				for (MedioContacto medio : medios) {
					if (medio.getMail().getId() == null)
						continue;

					Mail mail = medio.getMail();
					log.info("Mail: " + mail.getMail());

					buzon = new Correo(mail.getMail(),
							cteContacto.getContacto().getNombre() + " " + cteContacto.getContacto().getPrimerApellido()
									+ " " + cteContacto.getContacto().getSegundoApellido());
					mailUtil.addTo(buzon);
				}

			}

			Planta planta = plantaDAO.buscarPorId(idPlanta)
					.orElseThrow(() -> new RuntimeException("No se encontro la planta con ese identificador"));
			Usuario responsablePlanta = null;
			if (planta.getUsuario() != null) {
				responsablePlanta = planta.getUsuario();
			}

			if (responsablePlanta != null) {
				buzon = new Correo(responsablePlanta.getMail(), responsablePlanta.getNombre() + " "
						+ responsablePlanta.getApellido1() + " " + responsablePlanta.getApellido2());
				mailUtil.addCC(buzon);
			}

			mailUtil.setMailBody(html);
			mailUtil.setSubject(subject);
			mailUtil.sendJndiMailMessage(MailHelper.JNDI_MAIL_INVENTARIO);
		} catch (Exception ex) {
			log.error("Problema para enviar el correo electronico para la cancelación de la orden de salida " + folio,
					ex);
		}
	}

	public Salida getSalidaSelected() {
		return salidaSelected;
	}

	public void setSalidaSelected(Salida salidaSelected) {
		this.salidaSelected = salidaSelected;
	}

}
