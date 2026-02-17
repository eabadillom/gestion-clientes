package com.ferbo.clientes.util;

public class LoadPropertiesException extends Exception{
	
	/**
	 *
	 */
   private static final long serialVersionUID = -8874890513230067507L;

   /**
	 *
	 */
   public LoadPropertiesException() {
	super();
   }

   /**
    * 
    * @param message
    *            mensaje
    */
   public LoadPropertiesException(String message) {
	super(message);
   }

   /**
    * 
    * @param cause
    *            causa
    */
   public LoadPropertiesException(Throwable cause) {
	super(cause);
   }

   /**
    * 
    * @param message
    *            mensaje
    * @param cause
    *            causa
    */
   public LoadPropertiesException(String message, Throwable cause) {
	super(message, cause);
   }
}