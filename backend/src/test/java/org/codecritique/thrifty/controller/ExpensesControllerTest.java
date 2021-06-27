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
        createAndGetExpense();
    }

    @Test
    @WithMockUser(authorities = "100")
    void shouldReturnForbiddenOnCreateExpenseWhenExpenseAccountIdDoesNotEqualUserAccountId() throws Exception {
        Expense expense = expenseSupplier.get();
        assertEquals(1, expense.getAccountId());
        String json = mapper.writeValueAsString(expense);
        mockMvc.perform(post(getUrl(Expense.class)).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnBadRequestOnCreateExpenseWhenCategoryIsNull() throws Exception {
        Expense expense = expenseSupplier.get();
        assertNull(expense.getCategory());
        String json = mapper.writeValueAsString(expense);
        mockMvc.perform(post(getUrl(Expense.class))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnExpenses() throws Exception {
        createAndGetExpense();
        mockMvc.perform(get(getUrl(Expense.class)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void shouldUpdateExpenseAmount() throws Exception {
        Expense expense = createAndGetExpense();
        expense.setAmount(expenseAmountSupplier.get());
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
        expense.setCreatedOn(dateSupplier.get());
        assertEquals(expense, updateAndGetEntity(expense));
    }

    @Test
    void shouldRemoveExpense() throws Exception {
        Expense expense = createAndGetExpense();
        deleteEntity(Expense.class, expense.getId());
        mockMvc.perform(get(getUrl(Expense.class, expense.getId())))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "100")
    void shouldReturnForbiddenOnRemoveExpenseWhenExpenseAccountIdDoesNotEqualUserAccountId() throws Exception {
        mockMvc.perform(delete(getUrl(Expense.class, 1L))
                .with(csrf()))
                .andExpect(status().isForbidden());
    }
}