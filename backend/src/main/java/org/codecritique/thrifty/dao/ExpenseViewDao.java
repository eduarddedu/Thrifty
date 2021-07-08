package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.ExpenseView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional
public class ExpenseViewDao {
    @PersistenceContext
    private EntityManager em;

    public List<ExpenseView> findAll(long accountId) {
        String sql = "Select e from Expense e where e.accountId = :accountId order by e.createdOn DESC";
        return em.createQuery(sql, ExpenseView.class).setParameter("accountId", accountId)
                .getResultList();
    }

    public ExpenseView findById(Long id) {
        return em.find(ExpenseView.class, id);
    }
}
