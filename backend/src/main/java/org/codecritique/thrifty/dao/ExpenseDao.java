package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseDao {

    void store(Expense o);

    Expense getExpense(long id);

    List<Expense> getExpenses();

    void removeExpense(long id);

    void updateExpense(Expense e);

}
