package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Expense;

import java.util.List;

/**
 * @author Eduard Dedu
 */


public interface ExpenseService {

    void addExpense(Expense o);

    Expense getExpense(int id);

    List<Expense> getExpenses();

    void removeExpense(int id);

    void updateExpense(Expense e);
}
