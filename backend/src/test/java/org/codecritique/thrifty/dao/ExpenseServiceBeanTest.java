package org.codecritique.thrifty.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.codecritique.thrifty.TestUtils.*;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;


class ExpenseServiceBeanTest extends BaseServiceBeanTest {
    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private LabelService labelService;

    @Autowired
    private CategoryService categoryService;

    @Test
    void testStoreExpense() {
        expenseService.store(expenseSupplier.get());
    }

    @Test
    void testGetExpense() {
        Expense expense = expenseSupplier.get();
        expenseService.store(expense);
        assertEquals(expense, expenseService.get(expense.getId()));
    }

    @Test
    void testAddLabel() {
        //setup
        Label label = labelSupplier.get();
        Expense expense = expenseSupplier.get();
        labelService.store(label);
        expenseService.store(expense);

        //exercise
        expense.addLabel(label);

        //verify
        assertTrue(expense.getLabels().contains(label));
        assertTrue(label.getExpenses().contains(expense));
        assertEquals(expense, expenseService.get(expense.getId()));
    }

    @Test
    void testSetCategory() {
        Category category = categorySupplier.get();
        Expense expense = expenseSupplier.get();
        categoryService.store(category);
        expenseService.store(expense);

        //exercise
        expense.setCategory(category);

        //verify
        assertEquals(category, expense.getCategory());
        assertEquals(expense, expenseService.get(expense.getId()));
    }

    @Test
    void testRemoveLabel() {
        //setup
        Label label1 = labelSupplier.get();
        Label label2 = labelSupplier.get();
        labelService.store(label1);
        labelService.store(label2);

        Expense expense = expenseSupplier.get();
        expense.setLabels(new HashSet<>(Arrays.asList(label1, label2)));
        expenseService.store(expense);

        //exercise
        expense.removeLabel(label1);

        //verify
        assertEquals(expense, expenseService.get(expense.getId()));
        assertTrue(labelService.getLabels().contains(label1));
        assertFalse(expense.getLabels().contains(label1));
    }

    @Test
    void testGetExpensesSortedByDateDescending() {
        int numEntities = 10;

        for (int i = 0; i < numEntities; i++) {
            expenseService.store(expenseSupplier.get());
        }

        Iterator<LocalDate> it = expenseService.getExpenses().
                stream().map(Expense::getCreatedOn).iterator();

        while (it.hasNext()) {
            LocalDate date = it.next();
            if (it.hasNext()) {
                assertTrue(date.compareTo(it.next()) >= 0);
            }
        }
    }

    @Test
    void testUpdateExpense() {
        //setup
        Expense expense = expenseSupplier.get();
        Category category = categorySupplier.get();
        Label label = labelSupplier.get();
        expenseService.store(expense);
        categoryService.store(category);
        labelService.store(label);


        //exercise
        expense.setCreatedOn(randomDate.get());
        expense.setAmount((double) Integer.MAX_VALUE);
        expense.setDescription("Baz");
        expense.setCategory(category);
        expense.addLabel(label);
        expenseService.update(expense);

        //verify
        assertEquals(expense, expenseService.get(expense.getId()));
    }

    @Test
    void testRemoveExpense() {
        //setup
        //setup
        Expense expense = expenseSupplier.get();
        Category category = categorySupplier.get();
        Label label = labelSupplier.get();
        categoryService.store(category);
        labelService.store(label);
        expense.setCategory(category);
        expense.addLabel(label);
        expenseService.store(expense);

        //exercise
        expenseService.remove(expense.getId());

        //verify that:
        //- expense has been deleted
        assertNull(expenseService.get(expense.getId()));

        //- existing labels have not been deleted
        assertTrue(labelService.getLabels().contains(label));

        //- existing category has not been deleted
        assertTrue(categoryService.getCategories().contains(category));

    }

}