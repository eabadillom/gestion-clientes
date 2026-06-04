package com.ferbo.clientes.util;

enum StatusClass 
{
    QUALIFIED, //Verde
    UNQUALIFIED, //Rojo
    PROPOSAL, //Melon
    NEGOTIATION,//Amarillo
    NEW, //Azul
    RENEWAL, //Morado 
    CANCELLED; // Gris
}

enum StatusText 
{
    ENVIADA,
    APROBADA,
    RECHAZADA,
    CANCELADA;
}

public class ManageStatus 
{
    public ManageStatus() 
    {
    }
    
    public String getStatusClass(String statusClass) 
    {
        String mensaje = "";
        
        switch(statusClass)
        {
            case "E":
                mensaje = StatusClass.PROPOSAL.toString().toLowerCase();
                break;
            case "A":
                mensaje = StatusClass.QUALIFIED.toString().toLowerCase();
                break;
            case "R":
                mensaje = StatusClass.UNQUALIFIED.toString().toLowerCase();
                break;
            case "C":
                mensaje = StatusClass.CANCELLED.toString().toLowerCase();
                break;
        }
        return mensaje;
    }
    
    public String getStatusText(String statusText) 
    {
        String mensaje = "";
        switch(statusText)
        {
            case "E":
                mensaje = StatusText.ENVIADA.toString().toLowerCase();
                break;
            case "A":
                mensaje = StatusText.APROBADA.toString().toLowerCase();
                break;
            case "R":
                mensaje = StatusText.RECHAZADA.toString().toLowerCase();
                break;
            case "C":
                mensaje = StatusText.CANCELADA.toString().toLowerCase();
                break;
        }
        return mensaje;
    }
    
}
