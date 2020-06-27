package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

@SpringBootTest(classes = org.codecritique.thrifty.Application.class)
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExpenseServiceBeanTest extends BaseServiceBeanTest  {
    @Autowired
    ExpenseServiceBean service;

    @Test
    void testAddExpense() {
        service.addExpense(expenseSupplier.get());
    }

    @Test
    void testGetExpense() {
        Expense expense = expenseSupplier.get();
        service.addExpense(expense);
        assertTrue(service.getExpenses().size() >= 1);
        assertEquals(expense, service.getExpense(expense.getId()));
    }

    @Test
    void updateExpense() {
        Expense o = expenseSupplier.get();
        service.addExpense(o);
        o.setCreatedOn(LocalDate.MAX);
        o.setAmount((double) Integer.MAX_VALUE);
        o.setDescription(rNameGen.get());
        o.setLabels(new HashSet<>(Arrays.asList(labelSupplier.get(), labelSupplier.get())));
        o.setCategory(categorySupplier.get());
        service.updateExpense(o);
        assertEquals(o, service.getExpense(o.getId()));
    }

}