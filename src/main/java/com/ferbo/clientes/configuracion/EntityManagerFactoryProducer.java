package com.ferbo.clientes.configuracion;

import com.ferbo.gestion.core.provider.DefaultEntityManagerProvider;
import com.ferbo.gestion.core.provider.EntityManagerProvider;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class EntityManagerFactoryProducer implements ServletContextListener
{
    private final static Logger log = LogManager.getLogger(EntityManagerFactoryProducer.class);
    
    @PersistenceContext
    private static EntityManagerProvider provider;
    
    @Override
    public synchronized void contextInitialized(ServletContextEvent sce) {
        log.info("Inicializando el proveedor de persistencia JPA...");
        try {
            String persistenceUnitName = "gestionClientes"; 
            
            provider = new DefaultEntityManagerProvider(persistenceUnitName);
            log.info("EntityManagerFactory creado exitosamente");
        } catch (Exception e) {
            log.error("Error crítico al inicializar la persistencia", e);
            throw new RuntimeException("No se puede iniciar el EntityManagerFactory", e);
        }
    }
    
    @Override
    public synchronized void contextDestroyed(ServletContextEvent sce) {
        log.info("Apagando contexto web...");
    }

    public synchronized static EntityManagerProvider getProvider() 
    {
        if (provider == null) {
            throw new IllegalStateException("El proveedor de persistencia no ha sido inicializado.");
        }
        
        return provider;
    }

}
