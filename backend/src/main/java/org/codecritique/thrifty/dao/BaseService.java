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

    static {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        emf = context.getBean(EntityManagerFactory.class);
    }

    protected void persist(BaseEntity o) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(o);
        em.getTransaction().commit();
        em.close();
    }

    protected BaseEntity find(Class<? extends BaseEntity> entityClass, long id) {
        EntityManager em = emf.createEntityManager();
        BaseEntity baseEntity = em.find(entityClass, id);
        em.close();
        return baseEntity;
    }

    protected void update(BaseEntity entity) {
        EntityManager em = emf.createEntityManager();
        BaseEntity oldEntity = em.find(entity.getClass(), entity.getId());
        if (oldEntity != null && !oldEntity.equals(entity)) {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        }
        em.close();
    }

    protected void remove(Class<? extends BaseEntity> entityClass, long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        BaseEntity entity = em.find(entityClass, id);
        if (entity != null)
            em.remove(entity);
        em.getTransaction().commit();
        em.close();
    }

}
