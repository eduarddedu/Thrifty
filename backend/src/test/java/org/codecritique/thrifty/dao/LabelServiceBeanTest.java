package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.codecritique.thrifty.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class LabelServiceBeanTest extends BaseServiceBeanTest {

    @Autowired
    private LabelServiceBean labelService;

    @Autowired
    private ExpenseServiceBean expenseService;

    @Autowired
    private CategoryServiceBean categoryService;

    @Test
    void testAddLabel() {
        Label label = labelSupplier.get();
        labelService.store(label);
        assertEquals(label, labelService.get(label.getId()));
    }

    @Test
    void testGetLabelsSortedByName() {
        int numEntities = 10;

        for (int i = 0; i < numEntities; i++)
            labelService.store(labelSupplier.get());

        Iterator<String> it = labelService.getLabels()
                .stream().map(Label::getName).iterator();

        while (it.hasNext()) {
            String name = it.next();
            if (it.hasNext()) {
                assertTrue(name.compareTo(it.next()) <= 0);
            }
        }
    }

    @Test
    void testUpdateLabel() {
        //setup

        Label label = labelSupplier.get();
        labelService.store(label);

        Category category = categorySupplier.get();
        categoryService.store(category);

        Expense expense = expenseSupplier.get();
        expense.setCategory(category);
        expense.addLabel(label);
        expenseService.store(expense);

        //exercise

        label.setName(nameSupplier.get());
        labelService.update(label);

        //verify

        assertEquals(label, labelService.get(label.getId()));

        //verify the view from expense side is consistent
        assertEquals(1, expense.getLabels().size());
        assertTrue(expense.getLabels().contains(label));
        assertEquals(1, label.getExpenses().size());
        assertTrue(label.getExpenses().contains(expense));
    }

    @Test
    void testRemoveLabel() {
        Label label = new Label(nameSupplier.get());
        labelService.store(label);
        assertNotNull(labelService.get(label.getId()));
        labelService.remove(label.getId());
        assertNull(labelService.get(label.getId()));
    }

}