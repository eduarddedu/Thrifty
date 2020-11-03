package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Eduard Dedu
 */

@Service
public class CategoryServiceBean extends BaseService implements CategoryService {

    @Override
    public void store(Category o) {
        em.persist(o);
    }

    @Override
    public Category getCategory(long id) {
        return em.find(Category.class, id);
    }

    @Override
    public List<Category> getCategories() {
        return getCategoriesSortedByName();
    }

    @Override
    public void updateCategory(Category category) {
        super.update(category);
    }

    @Override
    public void removeCategory(long id) {
        super.remove(Category.class, id);
    }

    private List<Category> getCategoriesSortedByName() {
        String sql = "Select r from Category r Order by r.name ";
        return em.createQuery(sql, Category.class).getResultList();
    }
}
