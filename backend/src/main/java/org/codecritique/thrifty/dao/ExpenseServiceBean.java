package org.codecritique.thrifty.dao;


import org.codecritique.thrifty.entity.Expense;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExpenseServiceBean extends BaseEntityService implements ExpenseService {

    @Override
    public void addExpense(Expense o) {
        this.addEntity(o);
    }

    @Override
    public Expense getExpense(int id) {
        return em.find(Expense.class, id);
    }

    @Override
    public List<Expense> getExpensesSortedByName() {
        String s = "Select r from Expense r Order By r.createdOn";
        return em.createQuery(s, Expense.class).getResultList();
    }

    @Override
    public void removeExpense(int id) {
        this.removeEntity(Expense.class, id);
    }
}
