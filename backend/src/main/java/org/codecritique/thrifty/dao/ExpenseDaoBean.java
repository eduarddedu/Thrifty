package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Expense;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseDaoBean extends BaseDao<Expense> implements ExpenseDao {

    @Override
    public Expense getExpense(long id) {
        return findById(Expense.class, id);
    }

    @Override
    public List<Expense> getExpenses(long accountId) {
        String sql = "Select e from Expense e where e.accountId = :accountId order by e.createdOn DESC";
        return em.createQuery(sql, Expense.class).setParameter("accountId", accountId)
                .getResultList();
    }

    @Override
    public void removeExpense(long id) {
        super.remove(Expense.class, id);
    }

    @Override
    public void updateExpense(Expense expense) {
        super.update(expense);
    }

}
