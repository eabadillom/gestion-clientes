package com.ferbo.clientes.util;

public class ClientesException extends Exception {

	private static final long serialVersionUID = 6403151730190731762L;

	public ClientesException() {
		super();
	}

	public ClientesException(String message) {
		super(message);
	}

	public ClientesException(Throwable cause) {
		super(cause);
	}

	public ClientesException(String message, Throwable cause) {
		super(message, cause);
	}
}
