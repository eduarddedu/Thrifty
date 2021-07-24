package org.codecritique.thrifty.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.codecritique.thrifty.MockMvcTest;
import org.codecritique.thrifty.entity.*;
import org.codecritique.thrifty.jackson.JacksonConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.util.UriTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.codecritique.thrifty.Suppliers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(value = "johndoe@example.com")
public abstract class BaseControllerTest extends MockMvcTest {
    private final static Map<Class<? extends BaseEntity>, String> mapEntityClassToUrl = new HashMap<>();
    private final static String BASE_URL = "http://localhost:8080/rest-api/";
    protected final ObjectMapper mapper;

    static {
        mapEntityClassToUrl.put(Expense.class, BASE_URL.concat("expenses/"));
        mapEntityClassToUrl.put(Category.class, BASE_URL.concat("categories/"));
        mapEntityClassToUrl.put(Label.class, BASE_URL.concat("labels/"));
        mapEntityClassToUrl.put(Account.class, BASE_URL.concat("account/"));
    }

    BaseControllerTest() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(JacksonConfig.class);
        mapper = ctx.getBean(ObjectMapper.class);
    }

    protected Expense createAndGetExpense() throws Exception {
        Expense expense = expenses.get();
        expense.setCategory(createAndGetCategory());
        return createAndGetEntity(expense);
    }

    protected Category createAndGetCategory() throws Exception {
        return createAndGetEntity(categories.get());
    }

    protected Label createAndGetLabel() throws Exception {
        return createAndGetEntity(labels.get());
    }

    @SuppressWarnings("unchecked")
    protected <T extends BaseEntity> T createAndGetEntity(T entity) throws Exception {
        String json = mapper.writeValueAsString(entity);
        String url = mapEntityClassToUrl.get(entity.getClass());

        String location = mockMvc.perform(post(url).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern(url + "*"))
                .andReturn().getResponse().getHeader("Location");

        UriTemplate template = new UriTemplate(url + "{id}");
        assert location != null;
        long id = Long.parseLong(template.match(location).get("id"));

        return (T) getEntity(entity.getClass(), id);
    }


    @SuppressWarnings("unchecked")
    protected <T extends BaseEntity> T updateAndGetEntity(T entity) throws Exception {
        String json = mapper.writeValueAsString(entity);
        String url = mapEntityClassToUrl.get(entity.getClass());
        mockMvc.perform(put(url).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
        return (T) getEntity(entity.getClass(), entity.getId());
    }

    protected <T extends BaseEntity> T getEntity(Class<T> klass, Long id) throws Exception {
        String url = id == null ? url(klass) : url(klass, id);
        String json = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return mapper.readValue(json, klass);
    }

    protected void deleteEntity(Class<? extends BaseEntity> klass, long id) throws Exception {
        String url = mapEntityClassToUrl.get(klass) + id;
        mockMvc.perform(delete(url).with(csrf())).andExpect(status().isOk());
    }

    protected String url(Class<? extends BaseEntity> klass) {
        return mapEntityClassToUrl.get(klass);
    }

    protected String url(Class<? extends BaseEntity> klass, long id) {
        return mapEntityClassToUrl.get(klass).concat(id + "");
    }

}
