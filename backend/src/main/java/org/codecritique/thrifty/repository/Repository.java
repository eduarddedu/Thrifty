package org.codecritique.thrifty.repository;

import org.codecritique.thrifty.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@org.springframework.stereotype.Repository
public class Repository {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private ExpenseViewRepository expenseViewRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private LabelRespository labelRespository;
    @Autowired
    private UserRepository userRepository;

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
        return userRepository.findByUsernameIgnoreCase(username);
    }

    public <T extends BaseEntity> T findById(Class<T> klass, long id) {
        return em.find(klass, id);
    }

    public List<Expense> findExpenses(long accountId) {
        List<Expense> result = expenseRepository.findByAccountId(accountId);
        result.sort(Comparator.comparing(Expense::getCreatedOn).reversed());
        return result;
    }

    public List<ExpenseView> findExpenseViews(long accountId) {
        return expenseViewRepository.findByAccountId(accountId);
    }

    public List<Category> findCategories(long accountId) {
        List<Category> result = categoryRepository.findByAccountId(accountId);
        result.sort(Comparator.comparing(Category::getName));
        return result;
    }

    public List<Label> findLabels(long accountId) {
        List<Label> result = labelRespository.findByAccountId(accountId);
        result.sort(Comparator.comparing(Label::getName));
        return result;
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
        expenseViewRepository.findByAccountId(accountId).forEach(exp -> removeExpense(exp.getId()));
        account.getCategories().forEach(c -> removeCategory(c.getId()));
    }
}
