package com.ferbo.clientes.util;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StringTools {
	
	private static Logger log = LogManager.getLogger(StringTools.class);
	private static final Locale LOCALE_ES = Locale.forLanguageTag("es");
	
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
	
    /**
     * Determines whether the given text contains AT LEAST ONE of the specified terms.
     * The comparison is case insensitive.
     *
     * @param text  the text to search within
     * @param terms one or more terms to search for within the text
     * @return TRUE if at least one term was found in the text, FALSE otherwise
     */
    public static Boolean containsAnyTerm(String text, String... terms) {
        if (text == null || text.isEmpty() || terms == null || terms.length == 0) {
            return Boolean.FALSE;
        }
        
        String normalizedText = text.toLowerCase(LOCALE_ES);

        for (String term : terms) {
            if (term != null && !term.isEmpty()
                    && normalizedText.contains(term.toLowerCase(LOCALE_ES))) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    /**
     * Determines whether the given text contains ALL of the specified terms.
     * The comparison is case insensitive.
     *
     * @param text  the text to search within
     * @param terms one or more terms to search for within the text
     * @return TRUE if all terms were found in the text, FALSE if at least one was not found
     */
    public static Boolean containsAllTerms(String text, String... terms) {
        if (text == null || text.isEmpty() || terms == null || terms.length == 0) {
            return Boolean.FALSE;
        }
        
        String normalizedText = text.toLowerCase(LOCALE_ES);

        for (String term : terms) {
        	
            if (term == null || term.isEmpty()
                    || !normalizedText.contains(term.toLowerCase(LOCALE_ES))) {
                return Boolean.FALSE;
            }
        }

        return Boolean.TRUE;
    }

}
