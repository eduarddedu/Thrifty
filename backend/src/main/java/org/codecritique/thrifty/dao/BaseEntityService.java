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
        EntityManagerFactory emf = context.getBean(EntityManagerFactory.class);
        em = emf.createEntityManager();
    }

    protected  <T extends BaseEntity> void addEntity(T o) {
        em.getTransaction().begin();
        em.persist(o);
        em.getTransaction().commit();
    }

    protected <T extends BaseEntity> T getEntity(Class<T> klazz, int id) {
        return em.find(klazz, id);
    }

    protected void removeEntity(Class<?> entityClass, int id) {
        em.getTransaction().begin();
        em.remove(em.find(entityClass, id));
        em.getTransaction().commit();
    }

    protected <T extends BaseEntity> List<? extends T> getEntitiesSortedByName(Class<T> klazz) {
        String table = klazz.getSimpleName();
        String sql = "Select r from " + table + " r Order by r.name ";
        return em.createQuery(sql, klazz).getResultList();
    }
}
