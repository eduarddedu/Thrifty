package org.codecritique.thrifty.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.codecritique.thrifty.entity.BaseEntity;
import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.codecritique.thrifty.jackson.JacksonConfig;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.codecritique.thrifty.Generator.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = org.codecritique.thrifty.Application.class)
@ActiveProfiles("dev")
@AutoConfigureMockMvc
public abstract class BaseControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    protected enum Resource {
        EXPENSES("expenses"),
        CATEGORIES("categories"),
        LABELS("labels");

        Resource(String path) {
            url = url + path + "/";
        }

        String url = "http://localhost:8080/rest-api/";
    }

    protected final ObjectMapper mapper;

    BaseControllerTest() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(JacksonConfig.class);
        mapper = ctx.getBean(ObjectMapper.class);
    }

    protected Expense createExpense() throws Exception {
        Expense expense = expenseSupplier.get();
        expense.setCategory(createCategory());
        return (Expense) createEntity(expense);
    }

    protected Category createCategory() throws Exception {
        return (Category) createEntity(categorySupplier.get());
    }

    protected Label createLabel() throws Exception {
        return (Label) createEntity(labelSupplier.get());
    }

    protected BaseEntity createEntity(BaseEntity entity) throws Exception {
        String json = mapper.writeValueAsString(entity);

        Resource resource = entity instanceof Expense ? Resource.EXPENSES : entity instanceof Category ? Resource.CATEGORIES : Resource.LABELS;

        ResultActions actions = mockMvc.perform(post(resource.url)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.matchesPattern(resource.url + "\\d+")));

        String url = actions.andReturn().getResponse().getHeader("Location");
        long id = parseEntityIdFromLocationHeader(url, resource);

        json = mockMvc.perform(get(resource.url + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return mapper.readValue(json, findEntityClassForResource(resource));
    }

    protected BaseEntity updateEntity(BaseEntity entity, Resource resource) throws Exception {
        String json = mapper.writeValueAsString(entity);

        mockMvc.perform(put(resource.url)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        json = mockMvc.perform(get(resource.url + entity.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return mapper.readValue(json, findEntityClassForResource(resource));
    }

    protected BaseEntity getEntity(Resource resource, long id) throws Exception {
        String json = mockMvc.perform(get(resource.url + id))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return mapper.readValue(json, findEntityClassForResource(resource));
    }

    protected void deleteEntity(Resource resource, long id) throws Exception {
        mockMvc.perform(delete(resource.url + id)).andExpect(status().isOk());
    }

    private Class<? extends BaseEntity> findEntityClassForResource(Resource resource) {
        switch (resource) {
            case EXPENSES:
                return Expense.class;
            case CATEGORIES:
                return Category.class;
            case LABELS:
                return Label.class;
            default:
                throw new RuntimeException("No class for resource: " + resource);
        }
    }

    private Long parseEntityIdFromLocationHeader(String locationHeaderValue, Resource resource) {
        Matcher matcher = Pattern.compile(resource.url + "(\\d+)").matcher(locationHeaderValue);
        if (matcher.find())
            return Long.parseLong(matcher.group(1));
        else
            throw new RuntimeException("No entity id found at location: " + locationHeaderValue);
    }
}
