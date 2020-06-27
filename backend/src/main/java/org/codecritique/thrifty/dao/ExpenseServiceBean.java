package org.codecritique.thrifty.dao;


import org.codecritique.thrifty.entity.Expense;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExpenseServiceBean extends BaseEntityService implements ExpenseService {

    @Override
    public void addExpense(Expense o) {
        super.addEntity(o);
    }

    @Override
    public Expense getExpense(int id) {
        return em.find(Expense.class, id);
    }

    @Override
    public List<Expense> getExpenses() {
        String sql = "Select r from Expense r";
        return em.createQuery(sql, Expense.class).getResultList();
    }

    @Override
    public void removeExpense(int id) {
        super.removeEntity(Expense.class, id);
    }

    @Override
    public void updateExpense(Expense o) {
        Expense e = em.find(Expense.class, o.getId());
        if (e == null)
            return;
        em.getTransaction().begin();
        e.setCreatedOn(o.getCreatedOn());
        e.setDescription(o.getDescription());
        e.setCategory(o.getCategory());
        e.setLabels(o.getLabels());
        e.setAmount(o.getAmount());
        em.getTransaction().commit();
    }
}
