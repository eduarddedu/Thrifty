package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Eduard Dedu
 */

@Service
public class CategoryServiceBean extends BaseService implements CategoryService {

    @Override
    @Transactional
    public void store(Category o) {
        super.persist(o);
    }

    @Override
    @Transactional
    public Category get(long id) {
        return (Category) super.find(Category.class, id);
    }

    @Override
    @Transactional
    public List<Category> getCategories() {
        return getCategoriesSortedByName();
    }

    @Transactional
    private List<Category> getCategoriesSortedByName() {
        String sql = "Select r from Category r Order by r.name ";
        return em.createQuery(sql, Category.class).getResultList();
    }

    @Override
    @Transactional
    public void update(Category category) {
        super.update(category);
    }

    @Override
    @Transactional
    public void remove(long id) {
        super.remove(Category.class, id);
    }
}
