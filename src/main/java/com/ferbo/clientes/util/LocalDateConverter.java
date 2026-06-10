package com.ferbo.clientes.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("localDateConverter")
public class LocalDateConverter implements Converter<LocalDate> 
{
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public String getAsString(FacesContext context, UIComponent component, LocalDate value) 
    {
        return value == null ? "" : value.format(FORMATTER);
    }

    @Override
    public LocalDate getAsObject(FacesContext context, UIComponent component, String value) 
    {
        return value == null || value.isEmpty() ? null : LocalDate.parse(value, FORMATTER);
    }
}
