package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Expense;

import java.util.List;

/**
 * @author Eduard Dedu
 */


public interface ExpenseService {

    void store(Expense o);

    Expense get(int id);

    List<Expense> getExpenses();

    List<Expense> getExpensesSortedByDateDescending();

    void remove(int id);

    void update(Expense e);
}
