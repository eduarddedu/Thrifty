package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


class ExpenseServiceBeanTest extends BaseServiceBeanTest {
    @Autowired
    ExpenseService service;

    @Autowired
    LabelService labelService;

    @Autowired
    CategoryService categoryService;

    @Test
    void testAddExpense() {
        service.store(expenseSupplier.get());
    }

    @Test
    void testGetExpense() {
        Expense expense = expenseSupplier.get();
        service.store(expense);
        assertEquals(expense, service.get(expense.getId()));
    }

    @Test
    void testGetExpensesSortedByDateDescending() {
        int numEntities = 10;

        for (int i = 0; i < numEntities; i++) {
            service.store(expenseSupplier.get());
        }

        Iterator<LocalDate> it = service.getExpenses().
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
        service.store(expense);

        //exercise
        expense.setCreatedOn(LocalDate.MAX);
        expense.setAmount((double) Integer.MAX_VALUE);
        expense.setDescription(randomName.get());
        expense.setCategory(categorySupplier.get());
        expense.addLabel(labelSupplier.get());
        service.update(expense);

        //verify
        assertEquals(expense, service.get(expense.getId()));
    }


    @Test
    void testSetCategory() {
        //setup
        Category category = categorySupplier.get();
        Expense expense = expenseSupplier.get();
        service.store(expense);

        //exercise
        expense.setCategory(category);

        //verify
        assertEquals(category, service.get(expense.getId()).getCategory());
    }

    @Test
    void testAddLabel() {
        //setup
        Label label = labelSupplier.get();
        Expense expense = expenseSupplier.get();
        service.store(expense);

        //exercise
        expense.addLabel(label);

        //verify
        assertTrue(service.get(expense.getId()).getLabels().contains(label));
        for (Label label1 : labelService.getLabels()) {
            if (label1.equals(label))
                assertTrue(label.getExpenses().contains(expense));
        }
    }

    @Test
    void testRemoveLabelFromExpense() {
        //setup
        Label label = labelSupplier.get();
        Expense expense = expenseSupplier.get();
        labelService.store(label);
        service.store(expense);
        expense.addLabel(label);

        //exercise
        labelService.remove(label.getId());

        //verify
        assertNull(labelService.get(label.getId()));
        assertFalse(expense.getLabels().contains(label));
        assertFalse(service.get(expense.getId()).getLabels().contains(label));
    }

    @Test
    void testSetLabels() {
        Expense expense = expenseSupplier.get();
        service.store(expense);

        //exercise
        Set<Label> labels = new HashSet<>(Arrays.asList(labelSupplier.get(), labelSupplier.get()));
        expense.setLabels(labels);

        //verify
        assertEquals(labels, service.get(expense.getId()).getLabels());
    }

    @Test
    void testRemoveExpense() {
        //setup
        Expense expense = expenseSupplier.get();
        Set<Label> labels = expense.getLabels();
        Category category = expense.getCategory();
        service.store(expense);

        //exercise
        service.remove(expense.getId());

        //verify that:
        //- expense has been deleted
        assertNull(service.get(expense.getId()));

        //- existing labels have not been deleted
        for (Label label : labels)
            assertTrue(labelService.getLabels().contains(label));

        //- existing category has not been deleted
        assertTrue(categoryService.getCategories().contains(category));

    }

}