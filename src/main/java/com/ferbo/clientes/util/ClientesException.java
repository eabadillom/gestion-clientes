package com.ferbo.clientes.util;

public class ClientesException extends Exception {

	private static final long serialVersionUID = 6403151730190731762L;
	
	public ClientesException() {
		super();
	}
	
	/**
	    * 
	    * @param message
	    *            mensaje
	    */
	   public ClientesException(String message) {
		super(message);
	   }

	   /**
	    * 
	    * @param cause
	    *            causa
	    */
	   public ClientesException(Throwable cause) {
		super(cause);
	   }

	   /**
	    * 
	    * @param message
	    *            mensaje
	    * @param cause
	    *            causa
	    */
	   public ClientesException(String message, Throwable cause) {
		super(message, cause);
	   }

}
