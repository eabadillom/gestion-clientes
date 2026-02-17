package com.ferbo.clientes.mail.beans;

public class Adjunto {
	public static final String TP_ARCHIVO_PDF = "application/pdf";
	public static final String TP_ARCHIVO_GENERICO = "application/octet-stream";
	String nombreArchivo = null;
	String tipoArchivo = null;
	byte[] contenido = null;
	int tamanio = 0;
	
	
	
	public Adjunto(String nombreArchivo, String tipoArchivo, byte[] contenido) {
		super();
		this.nombreArchivo = nombreArchivo;
		this.tipoArchivo = tipoArchivo;
		this.contenido = contenido;
		
		if(contenido != null)
			tamanio = contenido.length;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getTipoArchivo() {
		return tipoArchivo;
	}
	public void setTipoArchivo(String tipoArchivo) {
		this.tipoArchivo = tipoArchivo;
	}
	public byte[] getContenido() {
		return contenido;
	}
	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}
	public int getTamanio() {
		return tamanio;
	}
	public void setTamanio(Integer tamanio) {
		this.tamanio = tamanio;
	}
	
}
