package org.codecritique.thrifty.dao;


import org.codecritique.thrifty.entity.Expense;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExpenseServiceBean extends BaseService {

    public List<Expense> getExpenses() {
        String s = "Select r from Expense r Order By r.createdOn";
        return em.createQuery(s, Expense.class).getResultList();
    }

    public Expense getExpenseById(int id) {
        return em.find(Expense.class, id);
    }
}
