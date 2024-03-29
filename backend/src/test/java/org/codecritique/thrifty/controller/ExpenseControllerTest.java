package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.codecritique.thrifty.Suppliers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExpenseControllerTest extends BaseControllerTest {

    @Test
    void shouldCreateExpense() throws Exception {
        createAndGetExpense();
    }

    @Test
    void shouldReturnBadRequestOnCreateExpenseWhenCategoryIsNull() throws Exception {
        Expense expense = expenses.get();
        assertNull(expense.getCategory());
        String json = mapper.writeValueAsString(expense);
        mockMvc.perform(post(url(Expense.class))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnExpenses() throws Exception {
        mockMvc.perform(get(url(Expense.class)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void shouldUpdateExpenseAmount() throws Exception {
        Expense expense = createAndGetExpense();
        expense.setAmount(amounts.get());
        assertEquals(expense, updateAndGetEntity(expense));
    }

    @Test
    void shouldUpdateExpenseCategory() throws Exception {
        Expense expense = createAndGetExpense();
        Category category = createAndGetCategory();
        expense.setCategory(category);
        assertEquals(expense, updateAndGetEntity(expense));
    }

    @Test
    void shouldUpdateExpenseCreatedOn() throws Exception {
        Expense expense = createAndGetExpense();
        expense.setCreatedOn(dates.get());
        assertEquals(expense, updateAndGetEntity(expense));
    }

    @Test
    void shouldRemoveExpense() throws Exception {
        Expense expense = createAndGetExpense();
        deleteEntity(Expense.class, expense.getId());
        mockMvc.perform(get(url(Expense.class, expense.getId())))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = "hacker@example.com")
    void shouldReturnForbiddenOnRemoveExpenseWhenExpenseAccountIdDoesNotEqualUserAccountId() throws Exception {
        mockMvc.perform(delete(url(Expense.class, 1L))
                .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "hacker@example.com")
    void shouldReturnForbiddenOnCreateExpenseWhenExpenseAccountIdDoesNotEqualUserAccountId() throws Exception {
        Expense expense = expenses.get();
        assertEquals(1, expense.getAccountId());
        String json = mapper.writeValueAsString(expense);
        mockMvc.perform(post(url(Expense.class)).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isForbidden());
    }
}