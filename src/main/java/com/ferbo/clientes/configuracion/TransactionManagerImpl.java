package com.ferbo.clientes.configuracion;

import java.util.function.Consumer;
import java.util.function.Function;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;

import com.ferbo.gestion.core.config.TransactionManager;
import com.ferbo.gestion.core.provider.EntityManagerProvider;

@Dependent
public class TransactionManagerImpl implements TransactionManager
{
    private EntityManagerProvider getProvider() {
        return EntityManagerFactoryProducer.getProvider();
    }
    
    @Override
    @Transactional()
    public <T> T executeRead(Function<EntityManager, T> action) 
    {
        EntityManager em = getProvider().getEntityManager();
        try {
            return action.apply(em);
        } finally {
            getProvider().close(em);
        }
    }

    @Override
    @Transactional()
    public <T> T executeWrite(Function<EntityManager, T> action) 
    {
        EntityManager em = getProvider().getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T result = action.apply(em);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e; 
        } finally {
            getProvider().close(em);
        }
    }

    @Override
    @Transactional()
    public void executeVoid(Consumer<EntityManager> action) 
    {
        EntityManager em = getProvider().getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            action.accept(em);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            getProvider().close(em);
        }
    }
    
}
