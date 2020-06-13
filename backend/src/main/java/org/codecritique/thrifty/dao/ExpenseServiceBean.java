package org.codecritique.thrifty.dao;


import org.codecritique.thrifty.entity.Expense;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExpenseServiceBean extends AbstractServiceBean {

    public List<Expense> getExpenses() {
        String sql = "Select c from Expense c Order By c.createdOn";
        return em.createQuery(sql, Expense.class).getResultList();
    }

    public Expense getExpenseById(int id) {
        return em.find(Expense.class, id);
    }
}
