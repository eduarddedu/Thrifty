package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.codecritique.thrifty.Generator.categorySupplier;
import static org.codecritique.thrifty.Generator.stringSupplier;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoriesControllerTest extends BaseControllerTest {

    @Test
    void testCreateCategory() throws Exception {
        createCategory();
    }

    @Test
    void testCreateCategoryBadRequest() throws Exception {
        Category category = categorySupplier.get();
        category.setName(null);
        String json = mapper.writeValueAsString(category);
        mockMvc.perform(post(Resource.CATEGORIES.url)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetCategories() throws Exception {
        createCategory();
        mockMvc.perform(get(Resource.CATEGORIES.url))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testUpdateCategory() throws Exception {
        Category category = createCategory();
        //exercise
        category.setName(stringSupplier.get());
        category.setDescription(stringSupplier.get());
        //verify
        assertEquals(category, updateEntity(category, Resource.CATEGORIES));
    }

    @Test
    void testUpdateCategoryPropagatesToRelatedExpenses() throws Exception {
        //setup
        Expense expense = createExpense();
        Category category = expense.getCategory();

        //exercise
        category.setName(stringSupplier.get());
        updateEntity(category, Resource.CATEGORIES);

        //verify
        assertEquals(expense, getEntity(Resource.EXPENSES, expense.getId()));
    }

    //@Test
    void testRemoveCategory() throws Exception {
        Category category = createCategory();
        mockMvc.perform(delete(Resource.CATEGORIES.url + category.getId()))
                .andExpect(status().isOk());
        //verify
        mockMvc.perform(get(Resource.CATEGORIES.url + category.getId()))
                .andExpect(status().isNotFound());
    }
}

