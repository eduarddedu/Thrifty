package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.codecritique.thrifty.TestUtils.expenseSupplier;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class ExpensesControllerTest extends BaseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final String resourcePath = "/rest-api/expenses";
    private final String locationHeaderPattern = "http://localhost" + resourcePath + "/(\\d+)";
    ;

    @Test
    void storeExpense() throws Exception {
        Expense expense = expenseSupplier.get();
        String json = mapper.writeValueAsString(expense);
        ResultActions actions = mockMvc.perform(post(resourcePath).contentType(MediaType.APPLICATION_JSON).content(json));
        actions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.matchesPattern(locationHeaderPattern)));

        String locationHeaderValue = actions.andReturn().getResponse().getHeader("Location");
        long expenseId = parseEntityIdFromLocationHeader(locationHeaderValue, locationHeaderPattern);
        mockMvc.perform(get(resourcePath + "/" + expenseId))
                .andDo(print())
                .andExpect(jsonPath("$.description").value(expense.getDescription()))
                .andExpect(jsonPath("$.amount").value(Double.toString(expense.getAmount())));
    }

    @Test
    void getExpenses() throws Exception {
        storeExpense();
        mockMvc.perform(get(resourcePath))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void updateExpense() throws Exception {
        String json = mapper.writeValueAsString(expenseSupplier.get());
        ResultActions actions = mockMvc.perform(post(resourcePath)
                .contentType(MediaType.APPLICATION_JSON).content(json));
        long expenseId = parseEntityIdFromLocationHeader(actions.andReturn().getResponse().getHeader("Location"),
                locationHeaderPattern);
        json = mockMvc.perform(get(resourcePath + "/" + expenseId))
                .andReturn().getResponse().getContentAsString();
        Expense expense = mapper.readValue(json, Expense.class);
        //exercise
        expense.setAmount(12.54);
        mockMvc.perform(put(resourcePath).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(expense)))
                .andExpect(status().isOk());
        //verify
        mockMvc.perform(get(resourcePath + "/" + expense.getId())).andExpect(jsonPath("$.amount").value("12.54"));
    }
}