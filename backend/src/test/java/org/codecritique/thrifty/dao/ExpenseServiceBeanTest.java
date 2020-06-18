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
import java.util.HashSet;

@SpringBootTest(classes = org.codecritique.thrifty.Application.class)
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExpenseServiceBeanTest {
    @Autowired
    ExpenseServiceBean service;
    @Autowired
    CategoryServiceBean categoryService;
    @Autowired
    LabelServiceBean labelService;


    @AfterAll
    void removeAll() {
        service.getExpenses().forEach(e -> service.removeExpense(e.getId()));
        categoryService.getCategories().forEach(category -> categoryService.removeCategory(category.getId()));
        labelService.getLabels().forEach(label -> labelService.removeLabel(label.getId()));
    }


    @Test
    void testAddExpense() {
        service.addExpense(getExpense());
    }

    private Expense getExpense() {
        Label label = new Label();
        label.setName("foo");
        Category category = new Category();
        category.setName("foo");
        category.setDescription("foo");
        Expense exp = new Expense();
        exp.setAmount(0d);
        exp.setCreatedOn(LocalDate.MIN);
        exp.setDescription("Buy oranges");
        exp.setLabels(new HashSet<>(labelService.getLabels()));
        exp.setCategories(new HashSet<>(categoryService.getCategories()));
        return exp;
    }

    @Test
    void testGetExpense() {
        Expense expense = getExpense();
        service.addExpense(expense);
        assertEquals(1, service.getExpenses().size());
        assertEquals(expense, service.getExpense(expense.getId()));
    }
}