package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Expense;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return (Expense) super.find(Expense.class, id);
    }

    @Override
    public List<Expense> getExpenses() {
        return getExpensesSortedByDateDescending();
    }

    @Override
    public void remove(long id) {
        super.remove(Expense.class, id);
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
