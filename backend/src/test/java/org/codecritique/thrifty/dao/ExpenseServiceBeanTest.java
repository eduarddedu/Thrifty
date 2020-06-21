package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;

@SpringBootTest(classes = org.codecritique.thrifty.Application.class)
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExpenseServiceBeanTest {
    @Autowired
    ExpenseServiceBean service;

    @Test
    void testAddExpense() {
        service.addExpense(getInstance());
    }

    @Test
    void testGetExpense() {
        Expense expense = getInstance();
        service.addExpense(expense);
        assertEquals(1, service.getExpenses().size());
        assertEquals(expense, service.getExpense(expense.getId()));
    }

    private static Expense getInstance() {
        Label label = Label.getInstance("Foo");
        Category category = Category.getInstance("A", "Description");
        Expense exp = new Expense();
        exp.setLabels(new HashSet<>(Collections.singletonList(label)));
        exp.setCategories(new HashSet<>(Collections.singletonList(category)));
        exp.setAmount(0d);
        exp.setCreatedOn(LocalDate.MIN);
        exp.setDescription("Buy oranges");
        return exp;
    }
}