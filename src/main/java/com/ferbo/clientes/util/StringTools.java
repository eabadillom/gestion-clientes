package com.ferbo.clientes.util;

public class StringTools {
	
	public static synchronized Boolean containsIgnoreCase(String text, String query) {
		Boolean result = null;
		if(text == null)
			return false;
		
		if(query == null)
			return false;
		
		if(text.toLowerCase().contains(query.toLowerCase()))
			result = Boolean.TRUE;
		else
			result = Boolean.FALSE;
		
		return result;
	}

}
