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


class ExpenseServiceBeanTest extends BaseServiceBeanTest {

    @Test
    void testStoreExpense() {
        Category category = createAndStoreCategory();
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
        expense.setCategory(createAndStoreCategory());
        expenseService.store(expense);
        assertEquals(expense, expenseService.getExpense(expense.getId()));
    }

    @Test
    void testAddLabelToExpense() {
        //setup

        Expense expense = expenseSupplier.get();
        expense.setCategory(createAndStoreCategory());
        expenseService.store(expense);

        Label label = labelSupplier.get();
        labelService.store(label);

        //exercise
        expense.addLabel(label);
        expenseService.updateExpense(expense);

        //verify
        assertTrue(expense.getLabels().contains(label));
        assertTrue(label.getExpenses().contains(expense));
        Expense clone = expenseService.getExpense(expense.getId());
        assertTrue(clone.getLabels().contains(label));
        assertEquals(expense, clone);
    }

    @Test
    void testSetCategory() {
        Category category = createAndStoreCategory();
        Expense expense = expenseSupplier.get();
        expense.setCategory(category);
        expenseService.store(expense);

        Category category2 = createAndStoreCategory();

        //exercise
        expense.setCategory(category2);
        expenseService.updateExpense(expense);

        //verify
        assertEquals(expense.getCategory(), category2);
        assertEquals(expense, expenseService.getExpense(expense.getId()));
    }

    @Test
    void testRemoveLabel() {
        //setup // create entities
        Label label = labelSupplier.get();
        Label label2 = labelSupplier.get();
        labelService.store(label);
        labelService.store(label2);

        Expense expense = expenseSupplier.get();
        expense.setCategory(createAndStoreCategory());
        expense.setLabels(Arrays.asList(label, label2));
        expenseService.store(expense);

        //exercise
        expense.removeLabel(label2);
        expenseService.updateExpense(expense);

        //verify
        assertFalse(expense.getLabels().contains(label2));
        assertEquals(expense, expenseService.getExpense(expense.getId()));
        assertTrue(labelService.getLabels().contains(label2));
    }

    @Test
    void testGetExpensesSortedByDateDescending() {
        int numEntities = 5;
        Category category = createAndStoreCategory();
        for (int i = 0; i < numEntities; i++) {
            Expense expense = expenseSupplier.get();
            expense.setCategory(category);
            expenseService.store(expense);
        }
        Iterator<LocalDate> it = expenseService.getExpenses().stream().map(Expense::getCreatedOn).iterator();
        while (it.hasNext()) {
            LocalDate date = it.next();
            if (it.hasNext()) {
                assertTrue(date.compareTo(it.next()) >= 0);
            }
        }
    }

    @Test
    void testGetExpensesForPeriod() {
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 12, 31);
        Category category = categorySupplier.get();
        categoryService.store(category);
        List<Expense> expensesBetweenDates = new ArrayList<>();
        int numEntities = 5;
        for (int i = 0; i < numEntities; i++) {
            Expense expense = expenseSupplier.get();
            expense.setCategory(category);
            expense.setCreatedOn(dateSupplier.get().withYear(2020));
            expensesBetweenDates.add(expense);
            expenseService.store(expense);
        }
        List<Expense> responseExpenses = expenseService.getExpensesForPeriod(startDate, endDate);
        assertTrue(responseExpenses.containsAll(expensesBetweenDates));
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
        expense.setAmount(new BigDecimal("17.23"));
        expense.setDescription(stringSupplier.get());
        expense.setCategory(category);
        expense.addLabel(label);
        expenseService.updateExpense(expense);

        //verify
        assertEquals(expense, expenseService.getExpense(expense.getId()));
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
        expenseService.removeExpense(expense.getId());

        //verify that:
        //- expense has been deleted
        assertNull(expenseService.getExpense(expense.getId()));

        //- existing labels have not been deleted
        assertTrue(labelService.getLabels().contains(label));

        //- existing category has not been deleted
        assertTrue(categoryService.getCategories().contains(category));

    }

    @Test
    void testGetExpensesTotalAmount() {
        List<Expense> allExpenses = expenseService.getExpenses();
        BigDecimal expectedTotalAmount = allExpenses.stream().map(Expense::getAmount).reduce(new BigDecimal("0"), BigDecimal::add);
        BigDecimal actualTotalAmount = expenseService.getExpensesTotalAmount();
        assertEquals(expectedTotalAmount, actualTotalAmount);
    }

    @Test
    void setAmountMaxNumberOfDigits() {
        BigDecimal amount = new BigDecimal("1234567.00");
        Expense expense = expenseSupplier.get();
        expense.setCategory(createAndStoreCategory());
        expense.setAmount(amount);
        expenseService.store(expense);
    }

    @Test
    void setAmountWithLargerThanMaxNumberOfIntegers() {
        BigDecimal amount = new BigDecimal("12345678.00");
        Expense expense = expenseSupplier.get();
        expense.setCategory(createAndStoreCategory());
        expense.setAmount(amount);
        assertThrows(ConstraintViolationException.class, () -> expenseService.store(expense));
    }

    @Test
    void setAmountWithLargerThanMaxNumberOfDecimals() {
        BigDecimal amount = new BigDecimal("1234567.001");
        Expense expense = expenseSupplier.get();
        expense.setCategory(createAndStoreCategory());
        expense.setAmount(amount);
        assertThrows(ConstraintViolationException.class, () -> expenseService.store(expense));
    }

}