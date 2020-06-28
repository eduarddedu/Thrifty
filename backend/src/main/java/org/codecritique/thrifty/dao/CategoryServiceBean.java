package org.codecritique.thrifty.dao;

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
    public Category get(int id) {
        return em.find(Category.class, id);
    }

    @Override
    public List<Category> getCategoriesSortedByName() {
        String sql = "Select r from Category r Order by r.name ";
        return em.createQuery(sql, Category.class).getResultList();
    }

    @Override
    public void remove(int id) {
        super.remove(Category.class, id);
    }

    @Override
    public void update(Category category) {
        if (em.find(Category.class, category.getId()) != null)
            super.persist(category);
    }
}
