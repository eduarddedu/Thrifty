package org.codecritique.thrifty;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import java.util.TimeZone;

@Configuration
public class AppConfig {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Bean
    @Profile("prod")
    public EntityManagerFactory entityManagerFactory() {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        return context.getBean(EntityManagerFactory.class);
    }
}
