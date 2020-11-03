package org.codecritique.thrifty.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.codecritique.thrifty.Generator.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ExpensesControllerTest extends BaseControllerTest {

    @Test
    void testCreateExpense() throws Exception {
        createExpense();
    }

    @Test
    void testCreateExpenseBadRequest() throws Exception {
        Expense expense = expenseSupplier.get();
        assertNull(expense.getCategory());
        String json = mapper.writeValueAsString(expense);
        mockMvc.perform(post(Resource.EXPENSES.url)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().is4xxClientError());
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
    void testGetExpensesBetweenDates() throws Exception {
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 12, 31);
        int numEntities = 5;
        Category category = createCategory();
        for (int i = 0; i < numEntities; i++) {
            Expense expense = expenseSupplier.get();
            expense.setCategory(category);
            expense.setCreatedOn(dateSupplier.get().withYear(2020));
            createEntity(expense);
        }
        String url = String.format(Resource.EXPENSES.url + "forPeriod?startDate=%s&endDate=%s", startDate, endDate);
        String json = mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<Expense> responseExpenses = mapper.readValue(json, new TypeReference<List<Expense>>() {
        });
        assertTrue(responseExpenses.size() >= numEntities);
        responseExpenses.forEach(expense -> {
            LocalDate createdOn = expense.getCreatedOn();
            assertTrue(createdOn.compareTo(startDate) >= 0);
            assertTrue(createdOn.compareTo(endDate) <= 0);
        });
    }

    @Test
    void testGetExpensesForYear() throws Exception {
        int year = 2020;
        int numEntities = 2;
        Category category = createCategory();
        for (int i = 0; i < numEntities; i++) {
            Expense expense = expenseSupplier.get();
            expense.setCategory(category);
            expense.setCreatedOn(dateSupplier.get().withYear(2020));
            createEntity(expense);
        }
        String url = Resource.EXPENSES.url + "forYear?year=" + year;
        String json = mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<Expense> responseExpenses = mapper.readValue(json, new TypeReference<List<Expense>>() {
        });
        assertTrue(responseExpenses.size() >= numEntities);
        responseExpenses.forEach(expense -> assertEquals(year, expense.getCreatedOn().getYear()));
    }

    @Test
    void testGetExpensesTotalAmount() throws Exception {
        String json = mockMvc.perform(get(Resource.EXPENSES.url))
                .andReturn().getResponse().getContentAsString();
        List<Expense> allExpenses = mapper.readValue(json, new TypeReference<List<Expense>>() {
        });
        String url = Resource.EXPENSES.url + "sum";
        String totalAmountStr = mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        BigDecimal expectedTotalAmount = allExpenses.stream().map(Expense::getAmount).reduce(new BigDecimal("0"), BigDecimal::add);
        BigDecimal actualTotalAmount = new BigDecimal(totalAmountStr);
        assertEquals(expectedTotalAmount, actualTotalAmount);
        assertEquals(expectedTotalAmount, actualTotalAmount);
        assertTrue(totalAmountStr.matches("\\d+(\\.\\d(\\d)?)?")); // a number with two decimal places
    }

    @Test
    void testUpdateExpenseAmount() throws Exception {
        //setup
        Expense expense = createExpense();
        //exercise
        expense.setAmount(expenseAmountSupplier.get());
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
        assertEquals(expense, updateEntity(expense, Resource.EXPENSES));
        ;
    }

    @Test
    void testUpdateExpenseCreatedOn() throws Exception {
        //setup
        Expense expense = createExpense();
        //exercise
        expense.setCreatedOn(dateSupplier.get());
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