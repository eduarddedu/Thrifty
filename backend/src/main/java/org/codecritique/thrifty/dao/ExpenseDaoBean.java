package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Expense;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseDaoBean extends BaseDao implements ExpenseDao {

    @Override
    public void store(Expense expense) {
        em.persist(expense);
    }

    @Override
    public Expense getExpense(long id) {
        return em.find(Expense.class, id);
    }

    @Override
    public List<Expense> getExpenses() {
        return em.createQuery("Select e from Expense e order by e.createdOn DESC", Expense.class)
                .getResultList();
    }

    @Override
    public void removeExpense(long id) {
        Expense expense = em.find(Expense.class, id);
        if (expense != null)
            em.remove(expense);
    }

    @Override
    public void updateExpense(Expense expense) {
        super.update(expense);
    }

}
