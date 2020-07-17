package org.codecritique.thrifty.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.codecritique.thrifty.TestUtils.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = org.codecritique.thrifty.Application.class)
@ActiveProfiles("dev")
@AutoConfigureMockMvc
public abstract class BaseControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    protected final static String EXPENSE_RESOURCE_PATH = "/rest-api/expenses";
    protected final static String EXPENSE_LOCATION_HEADER_PATTERN = "http://localhost" + EXPENSE_RESOURCE_PATH + "/(\\d+)";
    protected final static String LABEL_RESOURCE_PATH = "/rest-api/labels";
    protected final static String LABEL_LOCATION_HEADER_PATTERN = "http://localhost" + LABEL_RESOURCE_PATH + "/(\\d+)";
    protected final static String CATEGORY_RESOURCE_PATH = "/rest-api/categories";
    protected final static String CATEGORY_LOCATION_HEADER_PATTERN = "http://localhost" + CATEGORY_RESOURCE_PATH + "/(\\d+)";

    protected final ObjectMapper mapper;

    BaseControllerTest() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(JacksonConfig.class);
        mapper = ctx.getBean(ObjectMapper.class);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    protected Expense createExpense() throws Exception {
        Expense expense = expenseSupplier.get();

        /*
         * Null values are ignored when serializing to json, but not when de-serializing.
         * Thus when reading a json string into an Expense instance, we get a mapping exception.
         * As a workaround we create an Expense instance with a non-null category field.
         */

        expense.setCategory(createCategory());
        String json = mapper.writeValueAsString(expense);
        ResultActions actions = mockMvc.perform(post(EXPENSE_RESOURCE_PATH).contentType(MediaType.APPLICATION_JSON).content(json));
        long id = parseEntityIdFromLocationHeader(actions.andReturn().getResponse().getHeader("Location"),
                EXPENSE_LOCATION_HEADER_PATTERN);
        json = mockMvc.perform(get(EXPENSE_RESOURCE_PATH + "/" + id)).andReturn().getResponse().getContentAsString();
        return mapper.readValue(json, Expense.class);
    }

    protected Category createCategory() throws Exception {
        Category category = categorySupplier.get();
        String json = mapper.writeValueAsString(category);
        ResultActions actions = mockMvc.perform(post(CATEGORY_RESOURCE_PATH).contentType(MediaType.APPLICATION_JSON).content(json));

        String locationHeaderValue = actions.andReturn().getResponse().getHeader("Location");
        long id = parseEntityIdFromLocationHeader(locationHeaderValue, CATEGORY_LOCATION_HEADER_PATTERN);
        actions = mockMvc.perform(get(CATEGORY_RESOURCE_PATH + "/" + id));
        return mapper.readValue(actions.andReturn().getResponse().getContentAsString(), Category.class);
    }

    protected Label createLabel() throws Exception {
        Label label = labelSupplier.get();
        String json = mapper.writeValueAsString(label);
        ResultActions actions = mockMvc.perform(post(LABEL_RESOURCE_PATH).contentType(MediaType.APPLICATION_JSON).content(json));
        actions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.matchesPattern(LABEL_LOCATION_HEADER_PATTERN)));

        String locationHeaderValue = actions.andReturn().getResponse().getHeader("Location");
        long labelId = parseEntityIdFromLocationHeader(locationHeaderValue, LABEL_LOCATION_HEADER_PATTERN);
        actions = mockMvc.perform(get(LABEL_RESOURCE_PATH + "/" + labelId))
                .andDo(print())
                .andExpect(jsonPath("$.name").value(label.getName()));
        return mapper.readValue(actions.andReturn().getResponse().getContentAsString(), Label.class);
    }

    private Long parseEntityIdFromLocationHeader(String locationHeaderValue, String locationHeaderPattern) {
        Matcher matcher = Pattern.compile(locationHeaderPattern).matcher(locationHeaderValue);
        if (matcher.find())
            return Long.parseLong(matcher.group(1));
        else
            throw new RuntimeException("No entity id found!");
    }
}
