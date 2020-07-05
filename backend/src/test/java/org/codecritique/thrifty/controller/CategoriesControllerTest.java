package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Label;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.codecritique.thrifty.TestUtils.categorySupplier;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoriesControllerTest extends BaseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final String resourcePath = "/rest-api/categories";
    private final String locationHeaderPattern = "http://localhost" + resourcePath + "/(\\d+)";
    

    @Test
    void storeCategory() throws Exception {
        Category category = categorySupplier.get();
        String json = mapper.writeValueAsString(category);
        ResultActions actions = mockMvc.perform(post(resourcePath).contentType(MediaType.APPLICATION_JSON).content(json));
        actions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.matchesPattern(locationHeaderPattern)));

        String locationHeaderValue = actions.andReturn().getResponse().getHeader("Location");
        long categoryId = parseEntityIdFromLocationHeader(locationHeaderValue, locationHeaderPattern);
        mockMvc.perform(get(resourcePath + "/" + categoryId))
                .andDo(print())
                .andExpect(jsonPath("$.description").value(category.getDescription()));
    }


    @Test
    void getCategories() throws Exception {
        storeCategory();
        mockMvc.perform(get(resourcePath))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void updateCategory() throws Exception {
        String json = mapper.writeValueAsString(categorySupplier.get());
        ResultActions actions = mockMvc.perform(post(resourcePath)
                .contentType(MediaType.APPLICATION_JSON).content(json));
        long categoryId = parseEntityIdFromLocationHeader(actions.andReturn().getResponse().getHeader("Location"),
                locationHeaderPattern);
        json = mockMvc.perform(get(resourcePath + "/" + categoryId))
                .andReturn().getResponse().getContentAsString();
        Category category = mapper.readValue(json, Category.class);
        //exercise
        category.setName("Baz");
        mockMvc.perform(put(resourcePath).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(category)))
                .andExpect(status().isOk());
        //verify
        mockMvc.perform(get(resourcePath + "/" + category.getId())).andExpect(jsonPath("$.name").value("Baz"));
    }
}

