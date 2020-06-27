package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.BaseEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;


@Configuration
public abstract class BaseEntityService {
    protected EntityManager em;

    BaseEntityService() {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        em = context.getBean(EntityManagerFactory.class).createEntityManager();
    }

    protected <T extends BaseEntity> void addEntity(T o) {
        em.getTransaction().begin();
        em.persist(o);
        em.getTransaction().commit();
    }

    protected void removeEntity(Class<? extends BaseEntity> entityClass, int id) {
        em.getTransaction().begin();
        em.remove(em.find(entityClass, id));
        em.getTransaction().commit();
    }

    protected <T extends BaseEntity> List<T> getEntitiesSortedByName(Class<T> entityClass) {
        String table = entityClass.getSimpleName();
        String sql = "Select r from " + table + " r Order by r.name ";
        return em.createQuery(sql, entityClass).getResultList();
    }

    protected <T extends BaseEntity> List<T> getEntities(Class<T> entityClass) {
        String table = entityClass.getSimpleName();
        String sql = "Select r from " + table + " r";
        return em.createQuery(sql, entityClass).getResultList();
    }
}