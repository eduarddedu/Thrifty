package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Expense;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Eduard Dedu
 */

@Service
public class ExpenseServiceBean extends BaseService implements ExpenseService {

    @Override
    @Transactional
    public void store(Expense expense) {
        super.persist(expense);
    }

    @Override
    @Transactional
    public Expense get(long id) {
        return (Expense) super.find(Expense.class, id);
    }

    @Override
    @Transactional
    public List<Expense> getExpenses() {
        return getExpensesSortedByDateDescending();
    }

    @Override
    @Transactional
    public void remove(long id) {
        super.remove(Expense.class, id);
    }

    @Override
    @Transactional
    public void update(Expense expense) {
        super.update(expense);
    }

    @Transactional
    private List<Expense> getExpensesSortedByDateDescending() {
        String sql = "SELECT r from Expense r ORDER BY r.createdOn DESC";
        return em.createQuery(sql, Expense.class).getResultList();
    }
}
