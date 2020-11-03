package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Expense;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Eduard Dedu
 */


public interface ExpenseService {

    void store(Expense o);

    Expense getExpense(long id);

    List<Expense> getExpenses();

    List<Expense> getExpensesForPeriod(LocalDate startDate, LocalDate endDate);

    List<Expense> getExpensesForYear(int year);

    void removeExpense(long id);

    void updateExpense(Expense e);

    double getExpensesTotalAmount();
}
