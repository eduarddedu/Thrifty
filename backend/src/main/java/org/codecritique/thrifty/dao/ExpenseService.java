package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Expense;

import java.util.List;

public interface ExpenseService {
    void addExpense(Expense o);
    Expense getExpense(int id);
    List<Expense> getExpensesSortedByName();
    void removeExpense(int id);
}
