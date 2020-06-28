package org.codecritique.thrifty.dao;

import org.springframework.stereotype.Service;

import java.util.List;

import org.codecritique.thrifty.entity.Expense;

/**
 * @author Eduard Dedu
 */

@Service
public class ExpenseServiceBean extends BaseService implements ExpenseService {

    @Override
    public void store(Expense o) {
        super.persist(o);
    }

    @Override
    public Expense get(int id) {
        return em.find(Expense.class, id);
    }

    @Override
    public List<Expense> getExpenses() {
        String sql = "SELECT r from Expense r";
        return em.createQuery(sql, Expense.class).getResultList();
    }

    @Override
    public List<Expense> getExpensesSortedByDateDescending() {
        String sql = "SELECT r from Expense r ORDER BY r.createdOn DESC";
        return em.createQuery(sql, Expense.class).getResultList();
    }

    @Override
    public void remove(int id) {
        super.remove(Expense.class, id);
    }

    @Override
    public void update(Expense expense) {
        if (em.find(Expense.class, expense.getId()) != null)
            super.persist(expense);
    }
}
