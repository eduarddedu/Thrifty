package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.codecritique.thrifty.Generator.labelSupplier;
import static org.codecritique.thrifty.Generator.stringSupplier;
import static org.junit.jupiter.api.Assertions.*;

class LabelServiceBeanTest extends BaseServiceBeanTest {

    @Test
    void testAddLabel() {
        Label label = createAndStoreLabel();
        assertEquals(label, labelService.getLabel(label.getId()));
    }

    @Test
    void testGetLabelsSortedByName() {
        int numEntities = 10;

        for (int i = 0; i < numEntities; i++)
            labelService.store(labelSupplier.get());

        Iterator<String> it = labelService.getLabels()
                .stream().map(Label::getName).iterator();

        while (it.hasNext()) {
            String labelName = it.next();
            if (it.hasNext()) {
                assertTrue(labelName.compareTo(it.next()) <= 0);
            }
        }
    }

    @Test
    void testUpdateLabel() {
        //setup

        Label label = createAndStoreLabel();
        Expense expense = createAndStoreExpense();
        expense.addLabel(label);

        //exercise

        label.setName(stringSupplier.get());
        labelService.updateLabel(label);

        //verify

        assertEquals(label, labelService.getLabel(label.getId()));

        //verify the view from expense side is consistent
        assertEquals(1, expense.getLabels().size());
        assertTrue(expense.getLabels().contains(label));
        assertEquals(1, label.getExpenses().size());
        assertTrue(label.getExpenses().contains(expense));
    }

    @Test
    void testRemoveLabel() {
        Label label = createAndStoreLabel();
        assertNotNull(labelService.getLabel(label.getId()));
        labelService.removeLabel(label.getId());
        assertNull(labelService.getLabel(label.getId()));
    }

    @Test
    void testRemoveLabelLinkedToExpense() {
        // setup
        Expense expense = createAndStoreExpense();
        Label label = createAndStoreLabel();
        expense.addLabel(label);
        expenseService.updateExpense(expense);

        // exercise
        labelService.removeLabel(label.getId());

        // verify
        assertNull(labelService.getLabel(label.getId()));
        //assertFalse(expense.getLabels().contains(label)); // fails
        assertFalse(expenseService.getExpense(expense.getId()).getLabels().contains(label));
    }

}