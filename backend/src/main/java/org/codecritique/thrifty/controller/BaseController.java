package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.config.JpaConfig;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public abstract class BaseController {
    protected EntityManager em;

    BaseController() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(JpaConfig.class);
        EntityManagerFactory emf = context.getBean(EntityManagerFactory.class);
        em = emf.createEntityManager();
    }

}
