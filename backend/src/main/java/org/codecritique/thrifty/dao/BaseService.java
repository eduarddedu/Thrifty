package org.codecritique.thrifty.dao;

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
        EntityManagerFactory emf = context.getBean(EntityManagerFactory.class);
        em = emf.createEntityManager();
    }
}
