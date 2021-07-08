package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.codecritique.thrifty.Generator.categorySupplier;
import static org.codecritique.thrifty.Generator.stringSupplier;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest extends BaseControllerTest {

    @Test
    void shouldCreateCategory() throws Exception {
        createAndGetCategory();
    }

    @Test
    void shouldReturnBadRequestOnCreateCategoryWhenCategoryNameIsEmptyString() throws Exception {
        Category category = categorySupplier.get();
        category.setName("");
        mockMvc.perform(post(url(Category.class))
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
        mockMvc.perform(post(url(Category.class))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(duplicate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetCategories() throws Exception {
        mockMvc.perform(get(url(Category.class)))
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
        mockMvc.perform(get(url(Category.class, category.getId())))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnForbiddenOnCreateCategoryWhenCategoryAccountIdDoesNotEqualUserAccountId() throws Exception {
        Category category = categorySupplier.get();
        category.setAccountId(2);
        String json = mapper.writeValueAsString(category);
        mockMvc.perform(post(url(Category.class)).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "hacker@example.com")
    void shouldReturnForbiddenOnUpdateCategoryWhenCategoryAccountIdDoesNotEqualUserAccountId() throws Exception {
        //Category "Rent" in test-data.sql belongs to user "johndoe@example.com"
        String json = "{\"id\":2,\"name\":\"Rent\",\"description\":\"Rent\",\"accountId\":1}";

        mockMvc.perform(put(url(Category.class)).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "hacker@example.com")
    void shouldReturnForbiddenOnRemoveCategoryWhenCategoryAccountIdDoesNotEqualUserAccountId() throws Exception {
        String url = url(Category.class, 1L);
        mockMvc.perform(delete(url)
                .with(csrf()))
                .andExpect(status().isForbidden());
    }
}

