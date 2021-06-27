package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.codecritique.thrifty.Generator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExpensesControllerTest extends BaseControllerTest {

    @Test
    void shouldCreateExpense() throws Exception {
        createExpense();
    }

    @Test
    @WithMockUser(authorities = "100")
    void shouldReturnForbiddenOnCreateExpenseWhenExpenseAccountIdDoesNotEqualUserAccountId() throws Exception {
        Expense expense = expenseSupplier.get();
        assertEquals(1, expense.getAccountId());
        String json = mapper.writeValueAsString(expense);
        mockMvc.perform(post(Resource.EXPENSE.url).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnBadRequestOnCreateExpenseWhenCategoryIsNull() throws Exception {
        Expense expense = expenseSupplier.get();
        assertNull(expense.getCategory());
        String json = mapper.writeValueAsString(expense);
        mockMvc.perform(post(Resource.EXPENSE.url)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnExpenses() throws Exception {
        createExpense();
        mockMvc.perform(get(Resource.EXPENSE.url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void shouldUpdateExpenseAmount() throws Exception {
        Expense expense = createExpense();
        expense.setAmount(expenseAmountSupplier.get());
        assertEquals(expense, updateAndGetEntity(expense, Resource.EXPENSE));
    }

    @Test
    void shouldUpdateExpenseCategory() throws Exception {
        Expense expense = createExpense();
        Category category = createCategory();
        expense.setCategory(category);
        assertEquals(expense, updateAndGetEntity(expense, Resource.EXPENSE));
    }

    @Test
    void shouldUpdateExpenseCreatedOn() throws Exception {
        Expense expense = createExpense();
        expense.setCreatedOn(dateSupplier.get());
        assertEquals(expense, updateAndGetEntity(expense, Resource.EXPENSE));
    }

    @Test
    void shouldRemoveExpense() throws Exception {
        Expense expense = createExpense();
        deleteEntity(Resource.EXPENSE, expense.getId());
        mockMvc.perform(get(Resource.EXPENSE.url + expense.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "100")
    void shouldReturnForbiddenOnRemoveExpenseWhenExpenseAccountIdDoesNotEqualUserAccountId() throws Exception {
        mockMvc.perform(delete(Resource.EXPENSE.url + 1)
                .with(csrf()))
                .andExpect(status().isForbidden());
    }
}