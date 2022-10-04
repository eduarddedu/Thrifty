package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.codecritique.thrifty.Suppliers.labels;
import static org.codecritique.thrifty.Suppliers.strings;
import static org.junit.jupiter.api.Assertions.*;

class LabelDaoTest extends BaseDaoTest {

    @Test
    void shouldCreateLabel() {
        Label label = createAndGetLabel();
        assertEquals(label, repository.findById(Label.class, label.getId()));
    }

    @Test
    void shouldGetLabelsSortedByName() {
        int numEntities = 10;

        for (int i = 0; i < numEntities; i++)
            repository.save(labels.get());

        Iterator<String> it = repository.findLabels(ACCOUNT_ID)
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
        Label label = createAndGetLabel();
        Expense expense = createAndGetExpense();
        expense.addLabel(label);

        //exercise
        label.setName(strings.get());
        repository.updateEntity(label);

        //verify
        assertEquals(label, repository.findById(Label.class, label.getId()));

        //verify that the view from expense side is consistent
        assertEquals(1, expense.getLabels().size());

        Set<Label> labelsHashSet = new HashSet<>(expense.getLabels());
        assertTrue(labelsHashSet.contains(label)); // passes

        // assertTrue(expense.getLabels().contains(label)); // fails!
        // ...even though expense.getLabels() does contain an equal object!
        // Expense.getLabels() returns org.hibernate.collection.internal.PersistentSet
        // Looks like PersistentSet implementation of contains() violates the contract of the Set interface

        assertEquals(1, label.getExpenses().size());
        assertTrue(label.getExpenses().contains(expense));
    }

    @Test
    void shouldRemoveLabel() {
        Label label = createAndGetLabel();
        assertNotNull(repository.findById(Label.class, label.getId()));
        repository.removeLabel(label.getId());
        assertNull(repository.findById(Label.class, label.getId()));
    }

    @Test
    void shouldRemoveLabelLinkedToExpense() {
        // setup
        Expense expense = createAndGetExpense();
        Label label = createAndGetLabel();
        expense.addLabel(label);
        repository.updateEntity(expense);

        // exercise
        repository.removeLabel(label.getId());

        // verify
        assertNull(repository.findById(Label.class, label.getId()));
        assertFalse(repository.findById(Expense.class, expense.getId()).getLabels().contains(label));
    }

}