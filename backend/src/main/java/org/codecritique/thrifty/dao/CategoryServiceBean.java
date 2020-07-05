package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Expense;
import org.springframework.stereotype.Service;

import java.util.List;

import org.codecritique.thrifty.entity.Category;

/**
 * @author Eduard Dedu
 */

@Service
public class CategoryServiceBean extends BaseService implements CategoryService {

    @Override
    public void store(Category o) {
        super.persist(o);
    }

    @Override
    public Category get(long id) {
        return em.find(Category.class, id);
    }

    @Override
    public List<Category> getCategories() {
        return getCategoriesSortedByName();
    }

    private List<Category> getCategoriesSortedByName() {
        String sql = "Select r from Category r Order by r.name ";
        return em.createQuery(sql, Category.class).getResultList();
    }

    @Override
    public void update(Category category) {
        Category category1 = em.find(Category.class, category.getId());
        if (category1 != null) {
            em.getTransaction().begin();
            category1.setName(category.getName());
            category1.setDescription(category.getDescription());
            em.getTransaction().commit();
        }
    }

    @Override
    public void remove(long categoryId) {
        Category category = em.find(Category.class, categoryId);
        if (category != null) {
            em.getTransaction().begin();
            for (Expense expense : category.getExpenses()) {
                expense.setCategory(null);
            }
            em.remove(category);
            em.getTransaction().commit();
        }
    }
}
