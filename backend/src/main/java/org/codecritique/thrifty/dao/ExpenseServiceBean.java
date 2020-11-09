package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Expense;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Eduard Dedu
 */

@Service
public class ExpenseServiceBean extends BaseService implements ExpenseService {

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
    public List<Expense> getExpensesForPeriod(LocalDate startDate, LocalDate endDate) {
        String sql = "Select e from Expense e where e.createdOn >= :startDate and e.createdOn <= :endDate " +
                "order by e.createdOn DESC";
        return em.createQuery(sql, Expense.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    @Override
    public List<Expense> getExpensesForYear(int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        return getExpensesForPeriod(startDate, endDate);
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

    @Override
    public BigDecimal getExpensesTotalAmount() {
        return (BigDecimal) em.createNativeQuery("Select SUM(AMOUNT) AS Total from Expense").getSingleResult();
    }
}
