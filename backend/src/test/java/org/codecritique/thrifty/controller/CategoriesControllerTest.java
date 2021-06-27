package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

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
        createAndGetCategory();
    }

    @Test
    void shouldReturnForbiddenOnCreateCategoryWhenCategoryAccountIdDoesNotEqualUserAccountId() throws Exception {
        Category category = categorySupplier.get();
        category.setAccountId(100);
        String json = mapper.writeValueAsString(category);
        mockMvc.perform(post(getUrl(Category.class)).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnBadRequestOnCreateCategoryWhenCategoryNameIsEmptyString() throws Exception {
        Category category = categorySupplier.get();
        category.setName("");
        mockMvc.perform(post(getUrl(Category.class))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(category)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestOnCreateCategoryWhenCategoryNameIsDuplicate() throws Exception {
        Category original = createAndGetCategory();
        Category duplicate = new Category();
        duplicate.setName(original.getName());
        duplicate.setDescription("description");
        duplicate.setAccountId(1);
        mockMvc.perform(post(getUrl(Category.class))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(duplicate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetCategories() throws Exception {
        mockMvc.perform(get(getUrl(Category.class)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        Category category = createAndGetCategory();
        category.setName(stringSupplier.get());
        category.setDescription(stringSupplier.get());
        assertEquals(category, updateAndGetEntity(category));
    }

    @Test
    @WithMockUser(authorities = "100")
    void shouldReturnForbiddenOnUpdateCategoryWhenCategoryAccountIdDoesNotEqualUserAccountId() throws Exception {
        Category category = new Category();
        category.setAccountId(1);
        category.setName("Rent");
        category.setDescription("Rent"); // in test-data.sql
        String json = mapper.writeValueAsString(category);

        mockMvc.perform(put(getUrl(Category.class)).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReflectCategoryUpdateOnRelatedExpenses() throws Exception {
        //setup
        Expense expense = createAndGetExpense();
        Category category = expense.getCategory();

        //exercise
        category.setName(stringSupplier.get());
        updateAndGetEntity(category);

        //verify
        assertEquals(expense, getEntity(Expense.class, expense.getId()));
    }

    @Test
    void shouldRemoveCategory() throws Exception {
        Category category = createAndGetCategory();
        deleteEntity(Category.class, category.getId());
        //verify
        mockMvc.perform(get(getUrl(Category.class, category.getId())))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "100")
    void shouldReturnForbiddenOnRemoveCategoryWhenCategoryAccountIdDoesNotEqualUserAccountId() throws Exception {
        String url = getUrl(Category.class, 1L);
        mockMvc.perform(delete(url)
                .with(csrf()))
                .andExpect(status().isForbidden());
    }
}

