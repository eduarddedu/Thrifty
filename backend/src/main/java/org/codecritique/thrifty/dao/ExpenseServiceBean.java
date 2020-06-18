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
        return super.getEntities(Expense.class);
    }

    @Override
    public void removeExpense(int id) {
        super.removeEntity(Expense.class, id);
    }
}
