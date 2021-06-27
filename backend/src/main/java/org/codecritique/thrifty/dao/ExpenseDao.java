package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Eduard Dedu
 */


public interface ExpenseDao {

    void store(Expense o);

    Expense getExpense(long id);

    List<Expense> getExpenses();

    void removeExpense(long id);

    void updateExpense(Expense e);

   /* BigDecimal getTotalExpenseAmount();

    List<Expense> getExpensesForPeriod(LocalDate startDate, LocalDate endDate);

    List<Expense> getExpensesForYear(int year);*/
}
