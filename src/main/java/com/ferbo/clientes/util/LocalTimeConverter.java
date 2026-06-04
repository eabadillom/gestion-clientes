package com.ferbo.clientes.converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("localTimeConverter")
public class LocalTimeConverter implements Converter<LocalTime> 
{
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public LocalTime getAsObject(FacesContext context, UIComponent component, String value) 
    {
        try {
            if (value == null || value.trim().isEmpty()) {
                return null;
            }

            return LocalTime.parse(value, FORMATTER);
        } catch (Exception e) {
            throw new ConverterException(
                new FacesMessage("Hora inválida. Formato esperado HH:mm")
            );
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, LocalTime value) 
    {
        if (value == null) {
            return "";
        }

        return value.format(FORMATTER);
    }
}
