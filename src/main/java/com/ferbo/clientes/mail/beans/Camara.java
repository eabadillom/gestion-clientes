package com.ferbo.clientes.mail.beans;

public class Camara {
	private int id;
	private int planta;
	private String descripcion;
	private String numero; // en la base de datos se mapea como CAMARA_ABREV.

	public Camara() {
		id = -1;
		planta = -1;
		descripcion = "";
	}

	/**
	 * Devuelve el id (CAMARA_CVE) de la camara
	 * 
	 * @return CAMARA_CVE
	 */
	public int getId() {
		return id;
	}

	/**
	 * Establece el id de la Camara (CAMARA_CVE)
	 * 
	 * @param id
	 *            CAMARA_CVE
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Devuelve el id o planta_cve de la planat a la que pertenece la camara.
	 * 
	 * @return int PLANTA_CVE
	 */
	public int getPlanta() {
		return planta;
	}

	/**
	 * Establece el id o planta_cve de la planta a la que pertenece la camara.
	 * 
	 * @param planta
	 *            Objeto de la clase Planta que contiene todas las propiedades de la
	 *            Planta
	 */
	public void setPlanta(int planta) {
		this.planta = planta;
	}

	/**
	 * Devuelve la descripcion de la camara (nombre largo de la camara"
	 * 
	 * @return String con la descripcion de la camara
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Establece la descripcion de una camara
	 * 
	 * @param descripcion
	 *            Nombre largo que se le da a la camara. P. e: "Camara numero 30".
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Establece el numero (Abreviatura o nombre corto) de la camara.
	 * 
	 * @param n
	 */
	public void setNumero(String n) {
		this.numero = n;
	}

	/**
	 * Devuelve el numero (Abreviatura o nombre corto) de la camara.
	 * 
	 * @return String con el numero (Abreviatura o nombre corto) de la camara.
	 */
	public String getNumero() {
		return this.numero;
	}

}