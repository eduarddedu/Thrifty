package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;

import static org.codecritique.thrifty.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;


class ExpenseServiceBeanTest extends BaseServiceBeanTest {
    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private LabelService labelService;

    @Autowired
    private CategoryService categoryService;

    @Test
    void testStoreExpense() {

        Category category = categorySupplier.get();
        categoryService.store(category);
        Expense expense = expenseSupplier.get();
        expense.setCategory(category);
        Label label = labelSupplier.get();
        labelService.store(label);
        expense.addLabel(label);
        expenseService.store(expense);

        assertTrue(category.getExpenses().contains(expense));
        assertTrue(label.getExpenses().contains(expense));
    }

    @Test
    void testGetExpense() {
        Expense expense = expenseSupplier.get();
        Category category = categorySupplier.get();
        categoryService.store(category);
        expense.setCategory(category);
        expenseService.store(expense);
        assertEquals(expense, expenseService.get(expense.getId()));
    }

    @Test
    void testAddLabelToExpense() {
        //setup
        Category category = categorySupplier.get();
        categoryService.store(category);

        Expense expense = expenseSupplier.get();
        expense.setCategory(category);
        expenseService.store(expense);

        Label label = labelSupplier.get();
        labelService.store(label);

        //exercise
        expense.addLabel(label);
        expenseService.update(expense);

        //verify
        assertTrue(expense.getLabels().contains(label));
        assertTrue(label.getExpenses().contains(expense));
        Expense clone = expenseService.get(expense.getId());
        assertTrue(clone.getLabels().contains(label));
        assertEquals(expense, clone);
    }

    @Test
    void testSetCategory() {
        Category category = categorySupplier.get();
        categoryService.store(category);
        Expense expense = expenseSupplier.get();
        expense.setCategory(category);
        expenseService.store(expense);

        Category category2 = categorySupplier.get();
        categoryService.store(category2);

        //exercise
        expense.setCategory(category2);
        expenseService.update(expense);

        //verify
        assertEquals(expense.getCategory(), category2);
        assertEquals(expense, expenseService.get(expense.getId()));
    }

    @Test
    void testRemoveLabel() {
        //setup // create entities
        Label label = labelSupplier.get();
        Label label2 = labelSupplier.get();
        labelService.store(label);
        labelService.store(label2);

        Category category = categorySupplier.get();
        categoryService.store(category);

        Expense expense = expenseSupplier.get();
        expense.setCategory(category);
        expense.setLabels(Arrays.asList(label, label2));
        expenseService.store(expense);

        //exercise
        expense.removeLabel(label2);
        expenseService.update(expense);

        //verify
        assertFalse(expense.getLabels().contains(label2));
        assertEquals(expense, expenseService.get(expense.getId()));
        assertTrue(labelService.getLabels().contains(label2));
    }

    @Test
    void testGetExpensesSortedByDateDescending() {
        int numEntities = 10;

        for (int i = 0; i < numEntities; i++) {
            Category c = categorySupplier.get();
            categoryService.store(c);
            Expense expense = expenseSupplier.get();
            expense.setCategory(c);
            expenseService.store(expense);
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
        Category c = categorySupplier.get();
        categoryService.store(c);
        Expense expense = expenseSupplier.get();
        expense.setCategory(c);
        expenseService.store(expense);

        Category category = categorySupplier.get();
        Label label = labelSupplier.get();
        categoryService.store(category);
        labelService.store(label);


        //exercise
        expense.setCreatedOn(dateSupplier.get());
        expense.setAmount((double) Integer.MAX_VALUE);
        expense.setDescription(nameSupplier.get());
        expense.setCategory(category);
        expense.addLabel(label);
        expenseService.update(expense);

        //verify
        assertEquals(expense, expenseService.get(expense.getId()));
    }

    @Test
    void testRemoveExpense() {
        //setup
        Category category = categorySupplier.get();
        categoryService.store(category);
        Expense expense = expenseSupplier.get();
        expense.setCategory(category);

        Label label = labelSupplier.get();
        labelService.store(label);
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