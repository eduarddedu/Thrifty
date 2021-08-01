package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional
@org.springframework.stereotype.Repository
public class Repository {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private ExpenseViewDao expenseViewDao;

    private void removeEntity(Class<? extends BaseEntity> klass, long id) {
        BaseEntity entity = em.find(klass, id);
        if (entity != null)
            em.remove(entity);
    }

    public void save(BaseEntity entity) {
        em.persist(entity);
    }

    public void updateEntity(BaseEntity entity) {
        BaseEntity oldEntity = em.find(entity.getClass(), entity.getId());
        if (oldEntity != null && !oldEntity.equals(entity))
            em.merge(entity);
    }

    public User findByUsernameIgnoreCase(String username) {
        String sql = "Select u from User u where u.username = :username";
        return em.createQuery(sql, User.class).setParameter("username", username).getSingleResult();
    }

    public <T extends BaseEntity> T findById(Class<T> klass, long id) {
        return em.find(klass, id);
    }

    public List<Expense> findExpenses(long accountId) {
        String sql = "Select e from Expense e where e.accountId = :accountId order by e.createdOn DESC";
        return em.createQuery(sql, Expense.class).setParameter("accountId", accountId).getResultList();
    }

    public List<ExpenseView> findExpenseViews(long accountId) {
        return expenseViewDao.findByAccountId(accountId);
    }

    public List<Category> findCategories(long accountId) {
        String sql = "Select c from Category c where c.accountId = :accountId order by c.name";
        return em.createQuery(sql, Category.class).setParameter("accountId", accountId).getResultList();
    }

    public List<Label> findLabels(long accountId) {
        String sql = "SELECT l from Label l where l.accountId = :accountId ORDER BY l.name";
        return em.createQuery(sql, Label.class).setParameter("accountId", accountId).getResultList();
    }

    public void removeExpense(long id) {
        removeEntity(Expense.class, id);
    }

    public void removeCategory(long id) {
        removeEntity(Category.class, id);
    }

    public void removeLabel(long id) {
        Label label = em.find(Label.class, id);
        if (label != null) {
            label.getExpenses().forEach(e -> e.removeLabel(label));
            em.remove(label);
        }
    }

    public void removeAccountData(long accountId) {
        Account account = em.find(Account.class, accountId);
        if (account == null)
            return;
        account.getLabels().forEach(label -> removeLabel(label.getId()));
        expenseViewDao.findByAccountId(accountId).forEach(exp -> removeExpense(exp.getId()));
        account.getCategories().forEach(c -> removeCategory(c.getId()));
    }
}
