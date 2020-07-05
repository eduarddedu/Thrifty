package org.codecritique.thrifty.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.codecritique.thrifty.jackson.JacksonConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest(classes = org.codecritique.thrifty.Application.class)
@ActiveProfiles("dev")
@AutoConfigureMockMvc
public abstract class BaseControllerTest {

    protected final ObjectMapper mapper;

    BaseControllerTest() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(JacksonConfig.class);
        mapper = ctx.getBean(ObjectMapper.class);
    }

    public Long parseEntityIdFromLocationHeader(String locationHeaderValue, String locationHeaderPattern) {
        Matcher matcher = Pattern.compile(locationHeaderPattern).matcher(locationHeaderValue);
        if (matcher.find())
            return Long.parseLong(matcher.group(1));
        else
            throw new RuntimeException("No entity id found!");
    }
}
