package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.exception.WebException;
import org.springframework.stereotype.Service;

import java.util.List;

import org.codecritique.thrifty.entity.Expense;

import javax.persistence.EntityManager;

/**
 * @author Eduard Dedu
 */

@Service
public class ExpenseServiceBean extends BaseService implements ExpenseService {

    @Override
    public void store(Expense expense) {
        try {
            super.persist(expense);
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            th.printStackTrace();
            throw ex;
        }
    }

    @Override
    public Expense get(long id) {
        try {
            return (Expense) super.find(Expense.class, id);
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            th.printStackTrace();
            throw ex;
        }
    }

    @Override
    public List<Expense> getExpenses() {
        return getExpensesSortedByDateDescending();
    }

    @Override
    public void remove(long id) {
        try {
            super.remove(Expense.class, id);
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            th.printStackTrace();
            throw ex;
        }
    }

    @Override
    public void update(Expense expense) {
        try {
            super.update(expense);
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            th.printStackTrace();
            throw ex;
        }
    }

    private List<Expense> getExpensesSortedByDateDescending() {
        try {
            String sql = "SELECT r from Expense r ORDER BY r.createdOn DESC";
            return em.createQuery(sql, Expense.class).getResultList();
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            th.printStackTrace();
            throw ex;
        }
    }
}
