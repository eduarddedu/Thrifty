package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Expense;

import java.util.List;

/**
 * @author Eduard Dedu
 */


public interface ExpenseService {

    void store(Expense o);

    Expense get(long id);

    List<Expense> getExpenses();

    void remove(long id);

    void update(Expense e);
}
