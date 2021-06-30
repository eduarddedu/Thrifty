package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Eduard Dedu
 */

@Service
public class CategoryDaoBean extends BaseDao implements CategoryDao {

    @Override
    public void store(Category o) {
        em.persist(o);
    }

    @Override
    public Category getCategory(long id) {
        return em.find(Category.class, id);
    }

    @Override
    public List<Category> getCategories(long accountId) {
        return getCategoriesSortedByName(accountId);
    }

    @Override
    public void updateCategory(Category category) {
        super.update(category);
    }

    @Override
    public void removeCategory(long id) {
        Category category = em.find(Category.class, id);
        if (category != null)
            em.remove(category);
    }

    private List<Category> getCategoriesSortedByName(long accountId) {
        String sql = "Select c from Category c where c.accountId = :accountId order by c.name";
        return em.createQuery(sql, Category.class).setParameter("accountId", accountId).getResultList();
    }
}
