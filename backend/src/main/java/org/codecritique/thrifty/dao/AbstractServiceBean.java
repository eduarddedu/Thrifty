package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.config.JpaConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class AbstractServiceBean {
    protected EntityManager em;

    AbstractServiceBean() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(JpaConfig.class);
        EntityManagerFactory emf = context.getBean(EntityManagerFactory.class);
        em = emf.createEntityManager();
    }
}
