package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.codecritique.thrifty.Generator.categorySupplier;
import static org.codecritique.thrifty.Generator.stringSupplier;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoriesControllerTest extends BaseControllerTest {

    @Test
    void shouldCreateCategory() throws Exception {
        createCategory();
    }

    @Test
    void shouldReturnBadRequestWhenCategoryNameIsEmptyString() throws Exception {
        Category category = categorySupplier.get();
        category.setName("");
        mockMvc.perform(post(Resource.CATEGORY.url)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(category)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenCategoryNameIsDuplicate() throws Exception {
        Category original = createCategory();
        Category duplicate = new Category();
        duplicate.setName(original.getName());
        duplicate.setDescription("description");
        mockMvc.perform(post(Resource.CATEGORY.url)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(duplicate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetCategories() throws Exception {
        mockMvc.perform(get(Resource.CATEGORY.url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        Category category = createCategory();
        category.setName(stringSupplier.get());
        category.setDescription(stringSupplier.get());
        assertEquals(category, updateAndGetEntity(category, Resource.CATEGORY));
    }

    @Test
    void shouldReflectCategoryUpdateOnRelatedExpenses() throws Exception {
        //setup
        Expense expense = createExpense();
        Category category = expense.getCategory();

        //exercise
        category.setName(stringSupplier.get());
        updateAndGetEntity(category, Resource.CATEGORY);

        //verify
        assertEquals(expense, getEntity(Resource.EXPENSE, expense.getId()));
    }

    @Test
    void shouldRemoveCategory() throws Exception {
        Category category = createCategory();
        mockMvc.perform(delete(Resource.CATEGORY.url + category.getId())
                .with(csrf()))
                .andExpect(status().isOk());
        //verify
        mockMvc.perform(get(Resource.CATEGORY.url + category.getId()))
                .andExpect(status().isNotFound());
    }
}

