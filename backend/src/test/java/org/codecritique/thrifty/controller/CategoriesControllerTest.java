package org.codecritique.thrifty.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.codecritique.thrifty.TestUtil.nameSupplier;
import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;

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
        category.setName(nameSupplier.get());
        category.setDescription(nameSupplier.get());
        //verify
        assertEquals(category, updateEntity(category, Resource.CATEGORIES));
    }

    @Test
    void testUpdateCategoryPropagatesToRelatedExpenses() throws Exception {
        //setup
        Expense expense = createExpense();
        Category category = expense.getCategory();

        //exercise
        category.setName(nameSupplier.get());
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

