package com.ferbo.clientes.commons.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

public abstract class JDBCHelper {
	
	protected Long toLong(Object valor) {
	    if (valor == null) return null;
	    if (valor instanceof Number) return ((Number) valor).longValue();
	    throw new IllegalArgumentException("Tipo inesperado para Long: " + valor.getClass());
	}

	protected Integer toInteger(Object valor) {
	    if (valor == null) return null;
	    if (valor instanceof Number) return ((Number) valor).intValue();
	    throw new IllegalArgumentException("Tipo inesperado para Integer: " + valor.getClass());
	}

	protected BigDecimal toBigDecimal(Object valor) {
	    if (valor == null) return null;
	    if (valor instanceof BigDecimal) return (BigDecimal) valor;
	    if (valor instanceof Number) return BigDecimal.valueOf(((Number) valor).doubleValue());
	    throw new IllegalArgumentException("Tipo inesperado para BigDecimal: " + valor.getClass());
	}

	protected LocalDate toLocalDate(Object valor) {
	    if (valor == null) return null;
	    if (valor instanceof java.sql.Date) return ((java.sql.Date) valor).toLocalDate();
	    if (valor instanceof java.sql.Timestamp) return ((java.sql.Timestamp) valor).toLocalDateTime().toLocalDate();
	    if (valor instanceof java.util.Date) {
	        return ((java.util.Date) valor).toInstant()
	                .atZone(ZoneId.systemDefault()).toLocalDate();
	    }
	    throw new IllegalArgumentException("Tipo inesperado para LocalDate: " + valor.getClass());
	}
}
