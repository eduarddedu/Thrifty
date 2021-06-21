package org.codecritique.thrifty.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.codecritique.thrifty.MockMvcTest;
import org.codecritique.thrifty.entity.BaseEntity;
import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.codecritique.thrifty.jackson.JacksonConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.util.UriTemplate;

import static org.codecritique.thrifty.Generator.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "johndoe@example.com")
public abstract class BaseControllerTest extends MockMvcTest {

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

        Resource resource = Resource.resolveEntityClass(entity.getClass());

        String url = mockMvc.perform(post(resource.url).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern(resource.url + "*"))
                .andReturn().getResponse().getHeader("Location");

        UriTemplate template = new UriTemplate(resource.url + "{id}");
        long id = Long.parseLong(template.match(url).get("id"));

        return getEntity(resource, id);
    }

    protected BaseEntity updateAndGetEntity(BaseEntity entity, Resource resource) throws Exception {
        String json = mapper.writeValueAsString(entity);

        mockMvc.perform(put(resource.url).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
        return getEntity(resource, entity.getId());
    }

    protected BaseEntity getEntity(Resource resource, Long id) throws Exception {
        String url = id == null ? resource.url : resource.url + id;
        String json = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return mapper.readValue(json, resource.klass);
    }

    protected void deleteEntity(Resource resource, long id) throws Exception {
        mockMvc.perform(delete(resource.url + id).with(csrf())).andExpect(status().isOk());
    }

}
