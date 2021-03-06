package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.codecritique.thrifty.Generator.*;
import static org.junit.jupiter.api.Assertions.*;


class ExpenseDaoTest extends BaseDaoTest {

    @Test
    void shouldStoreExpense() {
        Category category = createAndStoreCategory();
        Expense expense = expenseSupplier.get();
        expense.setCategory(category);
        Label label = createAndStoreLabel();
        expense.addLabel(label);
        expenseDao.store(expense);

        assertTrue(category.getExpenses().contains(expense));
        assertTrue(label.getExpenses().contains(expense));
    }

    @Test
    void shouldGetExpense() {
        Expense expense = expenseSupplier.get();
        expense.setCategory(createAndStoreCategory());
        expenseDao.store(expense);
        assertEquals(expense, expenseDao.getExpense(expense.getId()));
    }

    @Test
    void shouldAddLabelToExpense() {
        //setup
        Expense expense = expenseSupplier.get();
        expense.setCategory(createAndStoreCategory());
        expenseDao.store(expense);
        Label label = createAndStoreLabel();

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
        Category category = createAndStoreCategory();
        Expense expense = expenseSupplier.get();
        expense.setCategory(category);
        expenseDao.store(expense);

        Category category2 = createAndStoreCategory();

        //exercise
        expense.setCategory(category2);
        expenseDao.updateExpense(expense);

        //verify
        assertEquals(expense.getCategory(), category2);
        assertEquals(expense, expenseDao.getExpense(expense.getId()));
    }

    @Test
    void shouldRemoveLabel() {
        //setup // create entities
        Label label = labelSupplier.get();
        Label label2 = labelSupplier.get();
        labelDao.store(label);
        labelDao.store(label2);

        Expense expense = expenseSupplier.get();
        expense.setCategory(createAndStoreCategory());
        expense.setLabels(Arrays.asList(label, label2));
        expenseDao.store(expense);

        //exercise
        expense.removeLabel(label2);
        expenseDao.updateExpense(expense);

        //verify
        assertFalse(expense.getLabels().contains(label2));
        assertEquals(expense, expenseDao.getExpense(expense.getId()));
        assertTrue(labelDao.getLabels().contains(label2));
    }

    @Test
    void shouldGetExpensesSortedByDateDescending() {
        int numEntities = 5;
        Category category = createAndStoreCategory();
        for (int i = 0; i < numEntities; i++) {
            Expense expense = expenseSupplier.get();
            expense.setCategory(category);
            expenseDao.store(expense);
        }
        Iterator<LocalDate> it = expenseDao.getExpenses().stream().map(Expense::getCreatedOn).iterator();
        while (it.hasNext()) {
            LocalDate date = it.next();
            if (it.hasNext()) {
                assertTrue(date.compareTo(it.next()) >= 0);
            }
        }
    }

    @Test
    void shouldGetExpensesForInterval() {
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 12, 31);
        Category category = categorySupplier.get();
        categoryDao.store(category);
        List<Expense> expensesBetweenDates = new ArrayList<>();
        int numEntities = 5;
        for (int i = 0; i < numEntities; i++) {
            Expense expense = expenseSupplier.get();
            expense.setCategory(category);
            expense.setCreatedOn(dateSupplier.get().withYear(2020));
            expensesBetweenDates.add(expense);
            expenseDao.store(expense);
        }
        List<Expense> responseExpenses = expenseDao.getExpensesForPeriod(startDate, endDate);
        assertTrue(responseExpenses.containsAll(expensesBetweenDates));
    }

    @Test
    void shouldUpdateExpense() {
        //setup
        Category c = createAndStoreCategory();
        Expense expense = expenseSupplier.get();
        expense.setCategory(c);
        expenseDao.store(expense);

        //exercise
        expense.setCreatedOn(dateSupplier.get());
        expense.setAmount(new BigDecimal("17.23"));
        expense.setDescription(stringSupplier.get());
        expense.setCategory(createAndStoreCategory());
        expense.addLabel(createAndStoreLabel());
        expenseDao.updateExpense(expense);

        //verify
        assertEquals(expense, expenseDao.getExpense(expense.getId()));
    }

    @Test
    void shouldRemoveExpense() {
        //setup
        Category category = createAndStoreCategory();
        Expense expense = expenseSupplier.get();
        expense.setCategory(category);

        Label label = createAndStoreLabel();
        expense.addLabel(label);
        expenseDao.store(expense);

        //exercise
        expenseDao.removeExpense(expense.getId());

        //verify that:
        //- expense has been deleted
        assertNull(expenseDao.getExpense(expense.getId()));

        //- existing labels have not been deleted
        assertTrue(labelDao.getLabels().contains(label));

        //- existing category has not been deleted
        assertTrue(categoryDao.getCategories().contains(category));

    }

    @Test
    void shouldGetTotalExpensesAmount() {
        List<Expense> allExpenses = expenseDao.getExpenses();
        BigDecimal expectedTotalAmount = allExpenses.stream().map(Expense::getAmount).reduce(new BigDecimal("0"), BigDecimal::add);
        BigDecimal actualTotalAmount = expenseDao.getTotalExpenseAmount();
        assertEquals(expectedTotalAmount, actualTotalAmount);
    }

    @Test
    void shouldAllowAmountWith_7_IntegerAnd_2_FractionalDigits() {
        BigDecimal amount = new BigDecimal("1234567.00");
        Expense expense = expenseSupplier.get();
        expense.setCategory(createAndStoreCategory());
        expense.setAmount(amount);
        expenseDao.store(expense);
    }

    @Test
    void shouldNotAllowAmountWith_8_IntegerDigits() {
        BigDecimal amount = new BigDecimal("12345678.00");
        Expense expense = expenseSupplier.get();
        expense.setCategory(createAndStoreCategory());
        expense.setAmount(amount);
        assertThrows(ConstraintViolationException.class, () -> expenseDao.store(expense));
    }

    @Test
    void shouldNotAllowAmountWith_3_FractionalDigits() {
        BigDecimal amount = new BigDecimal("1234567.001");
        Expense expense = expenseSupplier.get();
        expense.setCategory(createAndStoreCategory());
        expense.setAmount(amount);
        assertThrows(ConstraintViolationException.class, () -> expenseDao.store(expense));
    }

}