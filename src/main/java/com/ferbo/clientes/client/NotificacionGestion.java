
package com.ferbo.clientes.client;

import com.ferbo.clientes.dto.NotificacionDTO;
import com.ferbo.clientes.util.DataSourceManager;
import com.ferbo.tools.exception.SystemException;
import com.ferbo.tools.exception.ValidationException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sun.faces.config.ConfigManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Properties;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import javax.naming.NamingException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class NotificacionGestion extends Thread {
    
    private static final Logger log = LogManager.getLogger(NotificacionGestion.class);
    
    private static final Properties properties = new Properties();

    private final String servlet = "/notificacion-ordenes";
    
    private NotificacionDTO notificacion;
    
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
            new JsonDeserializer<LocalDateTime>() {
        @Override
        public LocalDateTime deserialize(
                JsonElement json,
                Type type,
                JsonDeserializationContext context) {

            return LocalDateTime.parse(json.getAsString());
        }
    }).registerTypeAdapter(
                    LocalDateTime.class,
                    new JsonSerializer<LocalDateTime>() {
                @Override
                public JsonElement serialize(
                        LocalDateTime fecha,
                        Type type,
                        JsonSerializationContext context) {

                    return new JsonPrimitive(fecha.toString());
                }
            }).create();
    
    public NotificacionGestion(NotificacionDTO notificacion) throws NamingException {
        this.notificacion = notificacion;
    }
    
    @Override
    public void run() {
        this.enviar(notificacion);
    }
    
    public NotificacionDTO enviar (NotificacionDTO notificacion) throws SystemException {
    
        if (notificacion == null) {
            throw new ValidationException("La notificación no puede ser vacía");
        }
        
        try {
            
            String basePath = DataSourceManager.getJndiParameter("gestion/path");

            String contexto = getProperty("contexto.gestion");

            String urlString = basePath.concat(contexto).concat(servlet);

            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpPost request = new HttpPost(urlString);

            request.addHeader("Content-Type", "application/json");
            StringEntity params = new StringEntity(gson.toJson(notificacion));
            request.setEntity(params);

            CloseableHttpResponse response = httpClient.execute(request);
        
            int statusCode = response.getStatusLine().getStatusCode();
            
            HttpEntity entity = response.getEntity();
            NotificacionDTO acuseConocimiento = new NotificacionDTO();
            if (entity != null && statusCode == 200) {
                String jsonString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                acuseConocimiento = gson.fromJson(jsonString, NotificacionDTO.class);
            } else {
                throw new SystemException( statusCode +  " - Hubo un problema al contactar con el servidor");
            }
            
            return acuseConocimiento;
            
        } catch (UnsupportedEncodingException ex) {
            log.warn("Error al momento de extraer el json de la respuesta: {}", ex.getMessage(), ex);
            throw new SystemException("Se ha producido un error al enviar la notificación al almacén.", ex);
        } catch (IOException ex) {
            log.warn("Error al momeno de hacer la peticion a gestión: {}", ex.getMessage(), ex);
            throw new SystemException("Se ha producido un error al enviar la notificación al almacén.", ex);
        }
    }
    
    static {
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new SystemException("No se pudo encontrar el archivo de configuración. Contacte con el administrador del sistema.");
            } else {
                properties.load(input);
            }
        } catch (IOException ex) {
            log.error("Error al momento de leer el archivo de propiedades del proyecto: {}", ex.getMessage(), ex);
            throw new SystemException("Hubo un problema al cargar la configuración del sistema. Contacte con el administrador del sistema.");
        }
    }

    private static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setNotificacion(NotificacionDTO notificacion) {
        this.notificacion = notificacion;
    }
    
    
}
