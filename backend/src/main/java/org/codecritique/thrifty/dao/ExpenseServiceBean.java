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
        super.remove(Expense.class, id);
    }

    @Override
    public void update(Expense expense) {
        Expense expense1 = em.find(Expense.class, expense.getId());
        if (expense1 != null) {
            em.getTransaction().begin();
            expense1.setCreatedOn(expense.getCreatedOn());
            expense1.setDescription(expense.getDescription());
            expense1.setAmount(expense.getAmount());
            expense1.setCategory(expense.getCategory());
            expense1.setLabels(expense.getLabels());
            em.getTransaction().commit();
        }
    }

    private List<Expense> getExpensesSortedByDateDescending() {
        String sql = "SELECT r from Expense r ORDER BY r.createdOn DESC";
        return em.createQuery(sql, Expense.class).getResultList();
    }
}
