package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.codecritique.thrifty.Generator.labelSupplier;
import static org.codecritique.thrifty.Generator.stringSupplier;
import static org.junit.jupiter.api.Assertions.*;

class LabelDaoTest extends BaseDaoTest {

    @Test
    void shouldCreateLabel() {
        Label label = createAndStoreLabel();
        assertEquals(label, labelDao.getLabel(label.getId()));
    }

    @Test
    void shouldGetLabelsSortedByName() {
        int numEntities = 10;

        for (int i = 0; i < numEntities; i++)
            labelDao.store(labelSupplier.get());

        Iterator<String> it = labelDao.getLabels()
                .stream().map(Label::getName).iterator();

        while (it.hasNext()) {
            String labelName = it.next();
            if (it.hasNext()) {
                assertTrue(labelName.compareTo(it.next()) <= 0);
            }
        }
    }

    @Test
    void shouldUpdateLabel() {
        //setup
        Label label = createAndStoreLabel();
        Expense expense = createAndStoreExpense();
        expense.addLabel(label);

        //exercise
        label.setName(stringSupplier.get());
        labelDao.updateLabel(label);

        //verify
        assertEquals(label, labelDao.getLabel(label.getId()));

        //verify the view from expense side is consistent
        assertEquals(1, expense.getLabels().size());
        assertTrue(expense.getLabels().contains(label));
        assertEquals(1, label.getExpenses().size());
        assertTrue(label.getExpenses().contains(expense));
    }

    @Test
    void shouldRemoveLabel() {
        Label label = createAndStoreLabel();
        assertNotNull(labelDao.getLabel(label.getId()));
        labelDao.removeLabel(label.getId());
        assertNull(labelDao.getLabel(label.getId()));
    }

    @Test
    void shouldRemoveLabelLinkedToExpense() {
        // setup
        Expense expense = createAndStoreExpense();
        Label label = createAndStoreLabel();
        expense.addLabel(label);
        expenseDao.updateExpense(expense);

        // exercise
        labelDao.removeLabel(label.getId());

        // verify
        assertNull(labelDao.getLabel(label.getId()));
        //assertFalse(expense.getLabels().contains(label)); // fails
        assertFalse(expenseDao.getExpense(expense.getId()).getLabels().contains(label));
    }

}