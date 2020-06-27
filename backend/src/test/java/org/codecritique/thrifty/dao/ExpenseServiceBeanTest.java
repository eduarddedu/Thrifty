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
import java.util.Collections;
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

}