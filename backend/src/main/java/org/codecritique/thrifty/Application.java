package org.codecritique.thrifty;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("beans.xml");
		SpringApplication.run(Application.class, args);
	}

}
