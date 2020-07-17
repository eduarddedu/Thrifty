package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Expense;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class ExpensesControllerTest extends BaseControllerTest {


    @Test
    void testCreateExpense() throws Exception {
        createExpense();
    }

    @Test
    void testGetExpenses() throws Exception {
        createExpense();
        mockMvc.perform(get(EXPENSE_RESOURCE_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testUpdateExpense() throws Exception {
        Expense expense = createExpense();

        //exercise
        expense.setAmount(12.54);
        mockMvc.perform(put(EXPENSE_RESOURCE_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(expense)))
                .andExpect(status().isOk());

        //verify
        mockMvc.perform(get(EXPENSE_RESOURCE_PATH + "/" + expense.getId())).andExpect(jsonPath("$.amount").value("12.54"));
    }

    @Test
    void testRemoveExpense() throws Exception {
        Expense expense = createExpense();

        //exercise
        mockMvc.perform(delete(EXPENSE_RESOURCE_PATH + "/" + expense.getId()))
                .andExpect(status().isOk());
        //verify
        mockMvc.perform(get(EXPENSE_RESOURCE_PATH + "/" + expense.getId()))
                .andExpect(status().isNotFound());
    }
}