package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoriesControllerTest extends BaseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateCategory() throws Exception {
        createCategory();
    }

    @Test
    void testGetCategories() throws Exception {
        createCategory();
        mockMvc.perform(get(CATEGORY_RESOURCE_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testUpdateCategory() throws Exception {
        Category category = createCategory();

        //exercise
        category.setName("Baz");
        mockMvc.perform(put(CATEGORY_RESOURCE_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(category)))
                .andExpect(status().isOk());
        //verify
        mockMvc.perform(get(CATEGORY_RESOURCE_PATH + "/" + category.getId())).andExpect(jsonPath("$.name").value("Baz"));
    }

    @Test
    void testDeleteCategory() throws Exception {
        Category category = createCategory();
        mockMvc.perform(delete(CATEGORY_RESOURCE_PATH + "/" + category.getId()))
                .andExpect(status().isOk());
        //verify
        mockMvc.perform(get(CATEGORY_RESOURCE_PATH + "/" + category.getId()))
                .andExpect(status().isNotFound());
    }
}

