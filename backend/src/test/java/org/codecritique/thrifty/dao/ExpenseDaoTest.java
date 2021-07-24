package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;

import static org.codecritique.thrifty.Suppliers.*;
import static org.junit.jupiter.api.Assertions.*;


class ExpenseDaoTest extends BaseDaoTest {

    @Test
    void shouldStoreExpense() {
        Category category = createAndGetCategory();
        Expense expense = expenses.get();
        expense.setCategory(category);
        Label label = createAndGetLabel();
        expense.addLabel(label);
        repository.save(expense);

        assertTrue(category.getExpenses().contains(expense));
        assertTrue(label.getExpenses().contains(expense));
    }

    @Test
    void shouldGetExpense() {
        Expense expense = expenses.get();
        expense.setCategory(createAndGetCategory());
        repository.save(expense);
        assertEquals(expense, repository.findById(Expense.class, expense.getId()));
    }

    @Test
    void shouldAddLabelToExpense() {
        //setup
        Expense expense = expenses.get();
        expense.setCategory(createAndGetCategory());
        repository.save(expense);
        Label label = createAndGetLabel();

        //exercise
        expense.addLabel(label);
        repository.updateEntity(expense);

        //verify
        assertTrue(expense.getLabels().contains(label));
        assertTrue(label.getExpenses().contains(expense));
        Expense sameExpense = repository.findById(Expense.class, expense.getId());
        assertTrue(sameExpense.getLabels().contains(label));
        assertEquals(expense, sameExpense);
    }

    @Test
    void shouldSetCategory() {
        Category category = createAndGetCategory();
        Expense expense = expenses.get();
        expense.setCategory(category);
        repository.save(expense);

        Category category2 = createAndGetCategory();

        //exercise
        expense.setCategory(category2);
        repository.updateEntity(expense);

        //verify
        assertEquals(expense.getCategory(), category2);
        assertEquals(expense, repository.findById(Expense.class, expense.getId()));
    }

    @Test
    void shouldRemoveLabel() {
        //setup: create entities
        Label label = labels.get();
        Label label2 = labels.get();
        repository.save(label);
        repository.save(label2);

        Expense expense = expenses.get();
        expense.setCategory(createAndGetCategory());
        expense.setLabels(Arrays.asList(label, label2));
        repository.save(expense);

        //exercise
        expense.removeLabel(label2);
        repository.updateEntity(expense);

        //verify
        assertFalse(expense.getLabels().contains(label2));
        assertEquals(expense, repository.findById(Expense.class, expense.getId()));
        assertTrue(repository.findLabels(ACCOUNT_ID).contains(label2));
    }

    @Test
    void shouldGetExpensesSortedByDateDescending() {
        int numEntities = 5;
        Category category = createAndGetCategory();
        for (int i = 0; i < numEntities; i++) {
            Expense expense = expenses.get();
            expense.setCategory(category);
            repository.save(expense);
        }
        Iterator<LocalDate> it = repository.findExpenses(ACCOUNT_ID)
                .stream().map(Expense::getCreatedOn).iterator();
        while (it.hasNext()) {
            LocalDate date = it.next();
            if (it.hasNext()) {
                assertTrue(date.compareTo(it.next()) >= 0);
            }
        }
    }

    @Test
    void shouldUpdateExpense() {
        //setup
        Category c = createAndGetCategory();
        Expense expense = expenses.get();
        expense.setCategory(c);
        repository.save(expense);

        //exercise
        expense.setCreatedOn(dates.get());
        expense.setAmount(new BigDecimal("17.23"));
        expense.setDescription(strings.get());
        expense.setCategory(createAndGetCategory());
        expense.addLabel(createAndGetLabel());
        repository.updateEntity(expense);

        //verify
        assertEquals(expense, repository.findById(Expense.class, expense.getId()));
    }

    @Test
    void shouldRemoveExpense() {
        // setup
        Category category = createAndGetCategory();
        Expense expense = expenses.get();
        expense.setCategory(category);

        Label label = createAndGetLabel();
        expense.addLabel(label);
        repository.save(expense);

        // exercise
        repository.removeExpense(expense.getId());

        // verify that:
        //- expense has been deleted
        assertNull(repository.findById(Expense.class, expense.getId()));

        //- its labels have not been deleted
        assertTrue(repository.findLabels(ACCOUNT_ID).contains(label));

        //- its category has not been deleted
        assertTrue(repository.findCategories(ACCOUNT_ID).contains(category));

    }

    @Test
    void shouldAllowAmountWith_7_IntegerAnd_2_FractionalDigits() {
        BigDecimal amount = new BigDecimal("1234567.00");
        Expense expense = expenses.get();
        expense.setCategory(createAndGetCategory());
        expense.setAmount(amount);
        repository.save(expense);
    }

    @Test
    void shouldNotAllowAmountWith_8_IntegerDigits() {
        BigDecimal amount = new BigDecimal("12345678.00");
        Expense expense = expenses.get();
        expense.setCategory(createAndGetCategory());
        expense.setAmount(amount);
        assertThrows(ConstraintViolationException.class, () -> repository.save(expense));
    }

    @Test
    void shouldNotAllowAmountWith_3_FractionalDigits() {
        BigDecimal amount = new BigDecimal("1234567.001");
        Expense expense = expenses.get();
        expense.setCategory(createAndGetCategory());
        expense.setAmount(amount);
        assertThrows(ConstraintViolationException.class, () -> repository.save(expense));
    }

}