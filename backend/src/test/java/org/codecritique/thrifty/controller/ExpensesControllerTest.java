package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.TestUtil;
import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ExpensesControllerTest extends BaseControllerTest {

    @Test
    void testCreateExpense() throws Exception {
        createExpense();
    }

    @Test
    void testGetExpenses() throws Exception {
        createExpense();
        mockMvc.perform(get(Resource.EXPENSES.url))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testUpdateExpenseAmount() throws Exception {
        //setup
        Expense expense = createExpense();
        //exercise
        expense.setAmount(12.54);
        //verify
        assertEquals(expense, updateEntity(expense, Resource.EXPENSES));
    }

    @Test
    void testUpdateExpenseCategory() throws Exception {
        Expense expense = createExpense();
        Category category = createCategory();
        //exercise
        expense.setCategory(category);
        //verify
        assertEquals(expense, updateEntity(expense, Resource.EXPENSES));;
    }

    @Test
    void testUpdateExpenseCreatedOn() throws Exception {
        //setup
        Expense expense = createExpense();
        //exercise
        expense.setCreatedOn(TestUtil.dateSupplier.get());
        //verify
        assertEquals(expense, updateEntity(expense, Resource.EXPENSES));
    }

    @Test
    void testRemoveExpense() throws Exception {
        Expense expense = createExpense();

        //exercise
        deleteEntity(Resource.EXPENSES, expense.getId());
        //verify
        mockMvc.perform(get(Resource.EXPENSES.url + expense.getId()))
                .andExpect(status().isNotFound());
    }
}