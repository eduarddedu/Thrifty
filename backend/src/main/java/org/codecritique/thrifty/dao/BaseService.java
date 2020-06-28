package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.BaseEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


@Configuration
public abstract class BaseService {
    protected EntityManager em;

    BaseService() {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        em = context.getBean(EntityManagerFactory.class).createEntityManager();
    }

    protected <T extends BaseEntity> void persist(T o) {
        em.getTransaction().begin();
        em.persist(o);
        em.getTransaction().commit();
    }

    protected void remove(Class<? extends BaseEntity> clazz, long id) {
        em.getTransaction().begin();
        em.remove(em.find(clazz, id));
        em.getTransaction().commit();
    }

}
