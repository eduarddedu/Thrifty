package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;


class ExpenseServiceBeanTest extends BaseServiceBeanTest {

    @Test
    void testAddExpense() {
        expenseService.store(expenseSupplier.get());
    }

    @Test
    void testGetExpense() {
        Expense expense = expenseSupplier.get();
        expenseService.store(expense);
        assertEquals(expense, expenseService.get(expense.getId()));
    }

    @Test
    void testGetExpensesSortedByDateDescending() {
        int numEntities = 10;

        for (int i = 0; i < numEntities; i++) {
            expenseService.store(expenseSupplier.get());
        }

        Iterator<LocalDate> it = expenseService.getExpensesSortedByDateDescending().
                stream().map(Expense::getCreatedOn).iterator();

        while (it.hasNext()) {
            LocalDate date = it.next();
            if (it.hasNext()) {
                assertTrue(date.compareTo(it.next()) >= 0);
            }
        }
    }

    @Test
    void updateExpense() {
        //setup
        Expense expense = expenseSupplier.get();
        expenseService.store(expense);

        //exercise
        expense.setCreatedOn(LocalDate.MAX);
        expense.setAmount((double) Integer.MAX_VALUE);
        expense.setDescription(randomName.get());
        expense.setCategory(categorySupplier.get());
        expense.addExpenseLabel(labelSupplier.get());
        expenseService.update(expense);

        //verify
        assertEquals(expense, expenseService.get(expense.getId()));
    }

    @Test
    void testAddExpenseLabel() {
        //setup
        Label label = labelSupplier.get();
        Expense expense = expenseSupplier.get();
        expenseService.store(expense);

        //exercise
        expense.addExpenseLabel(label);

        //verify
        assertTrue(expenseService.get(expense.getId()).getLabels().contains(label));
        for (Label label1 : labelService.getLabelsSortedByName()) {
            if (label1.equals(label))
                assertTrue(label.getExpenses().contains(expense));
        }
    }

    @Test
    void testSetCategory() {
        //setup
        Category category = categorySupplier.get();
        Expense expense = expenseSupplier.get();
        expenseService.store(expense);

        //exercise
        expense.setCategory(category);

        //verify
        assertEquals(category, expenseService.get(expense.getId()).getCategory());
    }

    @Test
    void testRemoveLabel() {
        //setup
        Label label = labelSupplier.get();
        Expense expense = expenseSupplier.get();
        labelService.store(label);
        expenseService.store(expense);
        expense.addExpenseLabel(label);

        //exercise
        labelService.remove(label.getId());

        //verify
        assertNull(labelService.get(label.getId()));
        assertFalse(expense.getLabels().contains(label));
        assertFalse(expenseService.get(expense.getId()).getLabels().contains(label));
    }

}