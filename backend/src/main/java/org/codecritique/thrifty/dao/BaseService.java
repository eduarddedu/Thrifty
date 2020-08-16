package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.BaseEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


@Configuration
public abstract class BaseService {
    protected static EntityManagerFactory emf;
    protected EntityManager em;

    static {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        emf = context.getBean(EntityManagerFactory.class);
    }

    BaseService() {
        em = emf.createEntityManager();
    }

    protected void persist(BaseEntity o) {
        em.getTransaction().begin();
        em.persist(o);
        em.getTransaction().commit();
    }

    protected BaseEntity find(Class<? extends BaseEntity> entityClass, long id) {
        return em.find(entityClass, id);
    }

    protected void update(BaseEntity entity) {
        BaseEntity oldEntity = em.find(entity.getClass(), entity.getId());
        if (oldEntity != null && !oldEntity.equals(entity)) {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        }
    }

    protected void remove(Class<? extends BaseEntity> entityClass, long id) {
        em.getTransaction().begin();
        BaseEntity entity = em.find(entityClass, id);
        if (entity != null)
            em.remove(entity);
        em.getTransaction().commit();
    }

    @Override
    protected void finalize() {
        em.close();
    }

}
