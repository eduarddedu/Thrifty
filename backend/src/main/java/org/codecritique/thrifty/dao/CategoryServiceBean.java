package org.codecritique.thrifty.dao;

import org.springframework.stereotype.Service;

import java.util.List;

import org.codecritique.thrifty.entity.Category;

/**
 * @author Eduard Dedu
 */

@Service
public class CategoryServiceBean extends BaseEntityService implements CategoryService {

    @Override
    public void addCategory(Category o) {
        super.addEntity(o);
    }

    @Override
    public Category getCategory(int id) {
        return em.find(Category.class, id);
    }

    @Override
    public List<Category> getCategories() {
        String sql = "Select r from Category r Order by r.name ";
        return em.createQuery(sql, Category.class).getResultList();
    }

    @Override
    public void removeCategory(int id) {
        super.removeEntity(Category.class, id);
    }

    @Override
    public void updateCategory(Category o) {
        Category c = em.find(Category.class, o.getId());
        if (c == null)
            return;
        em.getTransaction().begin();
        c.setName(o.getName());
        c.setDescription(o.getDescription());
        em.getTransaction().commit();
    }
}
