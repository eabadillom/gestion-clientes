package com.ferbo.clientes.mail.beans;

public class Planta {
	private int id = -1;
	private String descripcion = "";
	private String numero = "";
	private String sufijo = "";
	private String codigo = "";
	private Integer idUsuario = null;

	/**Obtiene el id (planta_cve) de la planta.
	 * @return planta_cve
	 * */
	public int getId() {
		return (this.id);
	}

	/**Establece el id (planta_cve) de la planta.
	 * @param id (planta_cve)
	 * */
	public void setId(int id) {
		this.id = id;
	}

	/**Obtiene el nombre completo de la planta (Descripcion).
	 * @return Descripcion.
	 * */
	public String getDescripcion() {
		return (this.descripcion);
	}

	/**Establece el nombre completo de la planta (Descripcion.
	 * @param Nombre completo (Descripcion).
	 * */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	/**Obtiene el numero (nombre corto o abreviatura) de la planta.
	 * @return numero (abreviatura).
	 * */
	public String getNumero()
	{
		return this.numero;
	}
	
	/**Establece el numero (nombre corto o abreviatura) de la planta.
	 * @param Numero (abreviatura) de la planta.
	 * */
	public void setNumero(String num)
	{
		this.numero = num;
	}
	
	/**Obtiene el sufijo de la planta para los folios de las constancias de deposito.
	 * @return sufijo para la constancia de deposito.
	 * */
	public String getSufijo()
	{
		return this.sufijo;
	}
	
	/**Establece el sufijo de la planta para los folios de las constancias de deposito.
	 * @param sufijo para la constancia de deposito.
	 * */
	public void setSufijo(String suf)
	{
		if(suf != null)
		this.sufijo = suf.toUpperCase();
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

}
