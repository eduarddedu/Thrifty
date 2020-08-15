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
    public void store(Expense expense) {
        super.persist(expense);
    }

    @Override
    public Expense get(long id) {
        return em.find(Expense.class, id);
    }

    @Override
    public List<Expense> getExpenses() {
        return getExpensesSortedByDateDescending();
    }

    @Override
    public void remove(long id) {
        Expense expense = em.find(Expense.class, id);
        if (expense != null)
            super.remove(expense);
    }

    @Override
    public void update(Expense expense) {
        super.update(expense);
    }

    private List<Expense> getExpensesSortedByDateDescending() {
        String sql = "SELECT r from Expense r ORDER BY r.createdOn DESC";
        return em.createQuery(sql, Expense.class).getResultList();
    }
}
