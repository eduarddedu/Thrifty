package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryDaoBean extends BaseDao<Category> implements CategoryDao {

    @Override
    public Category getCategory(long id) {
        return findById(Category.class, id);
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
        super.remove(Category.class, id);
    }

    private List<Category> getCategoriesSortedByName(long accountId) {
        String sql = "Select c from Category c where c.accountId = :accountId order by c.name";
        return em.createQuery(sql, Category.class).setParameter("accountId", accountId).getResultList();
    }
}
