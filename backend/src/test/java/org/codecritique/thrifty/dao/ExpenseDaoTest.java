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

import static org.codecritique.thrifty.Generator.*;
import static org.junit.jupiter.api.Assertions.*;


class ExpenseDaoTest extends BaseDaoTest {

    @Test
    void shouldStoreExpense() {
        Category category = createAndGetCategory();
        Expense expense = expenseSupplier.get();
        expense.setCategory(category);
        Label label = createAndGetLabel();
        expense.addLabel(label);
        expenseDao.save(expense);

        assertTrue(category.getExpenses().contains(expense));
        assertTrue(label.getExpenses().contains(expense));
    }

    @Test
    void shouldGetExpense() {
        Expense expense = expenseSupplier.get();
        expense.setCategory(createAndGetCategory());
        expenseDao.save(expense);
        assertEquals(expense, expenseDao.getExpense(expense.getId()));
    }

    @Test
    void shouldAddLabelToExpense() {
        //setup
        Expense expense = expenseSupplier.get();
        expense.setCategory(createAndGetCategory());
        expenseDao.save(expense);
        Label label = createAndGetLabel();

        //exercise
        expense.addLabel(label);
        expenseDao.updateExpense(expense);

        //verify
        assertTrue(expense.getLabels().contains(label));
        assertTrue(label.getExpenses().contains(expense));
        Expense sameExpense = expenseDao.getExpense(expense.getId());
        assertTrue(sameExpense.getLabels().contains(label));
        assertEquals(expense, sameExpense);
    }

    @Test
    void shouldSetCategory() {
        Category category = createAndGetCategory();
        Expense expense = expenseSupplier.get();
        expense.setCategory(category);
        expenseDao.save(expense);

        Category category2 = createAndGetCategory();

        //exercise
        expense.setCategory(category2);
        expenseDao.updateExpense(expense);

        //verify
        assertEquals(expense.getCategory(), category2);
        assertEquals(expense, expenseDao.getExpense(expense.getId()));
    }

    @Test
    void shouldRemoveLabel() {
        //setup: create entities
        Label label = labelSupplier.get();
        Label label2 = labelSupplier.get();
        labelDao.save(label);
        labelDao.save(label2);

        Expense expense = expenseSupplier.get();
        expense.setCategory(createAndGetCategory());
        expense.setLabels(Arrays.asList(label, label2));
        expenseDao.save(expense);

        //exercise
        expense.removeLabel(label2);
        expenseDao.updateExpense(expense);

        //verify
        assertFalse(expense.getLabels().contains(label2));
        assertEquals(expense, expenseDao.getExpense(expense.getId()));
        assertTrue(labelDao.getLabels(accountId).contains(label2));
    }

    @Test
    void shouldGetExpensesSortedByDateDescending() {
        int numEntities = 5;
        Category category = createAndGetCategory();
        for (int i = 0; i < numEntities; i++) {
            Expense expense = expenseSupplier.get();
            expense.setCategory(category);
            expenseDao.save(expense);
        }
        Iterator<LocalDate> it = expenseDao.getExpenses(accountId)
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
        Expense expense = expenseSupplier.get();
        expense.setCategory(c);
        expenseDao.save(expense);

        //exercise
        expense.setCreatedOn(dateSupplier.get());
        expense.setAmount(new BigDecimal("17.23"));
        expense.setDescription(stringSupplier.get());
        expense.setCategory(createAndGetCategory());
        expense.addLabel(createAndGetLabel());
        expenseDao.updateExpense(expense);

        //verify
        assertEquals(expense, expenseDao.getExpense(expense.getId()));
    }

    @Test
    void shouldRemoveExpense() {
        // setup
        Category category = createAndGetCategory();
        Expense expense = expenseSupplier.get();
        expense.setCategory(category);

        Label label = createAndGetLabel();
        expense.addLabel(label);
        expenseDao.save(expense);

        // exercise
        expenseDao.removeExpense(expense.getId());

        // verify that:
        //- expense has been deleted
        assertNull(expenseDao.getExpense(expense.getId()));

        //- its labels have not been deleted
        assertTrue(labelDao.getLabels(accountId).contains(label));

        //- its category has not been deleted
        assertTrue(categoryDao.getCategories(accountId).contains(category));

    }

    @Test
    void shouldAllowAmountWith_7_IntegerAnd_2_FractionalDigits() {
        BigDecimal amount = new BigDecimal("1234567.00");
        Expense expense = expenseSupplier.get();
        expense.setCategory(createAndGetCategory());
        expense.setAmount(amount);
        expenseDao.save(expense);
    }

    @Test
    void shouldNotAllowAmountWith_8_IntegerDigits() {
        BigDecimal amount = new BigDecimal("12345678.00");
        Expense expense = expenseSupplier.get();
        expense.setCategory(createAndGetCategory());
        expense.setAmount(amount);
        assertThrows(ConstraintViolationException.class, () -> expenseDao.save(expense));
    }

    @Test
    void shouldNotAllowAmountWith_3_FractionalDigits() {
        BigDecimal amount = new BigDecimal("1234567.001");
        Expense expense = expenseSupplier.get();
        expense.setCategory(createAndGetCategory());
        expense.setAmount(amount);
        assertThrows(ConstraintViolationException.class, () -> expenseDao.save(expense));
    }

}