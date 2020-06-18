package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryServiceBean extends BaseService implements CategoryService {

    @Override
    public void addCategory(Category o) {
        em.getTransaction().begin();
        em.persist(o);
        em.getTransaction().commit();
    }

    @Override
    public Category getCategory(int id) {
        return em.find(Category.class, id);
    }

    @Override
    public List<Category> getCategoriesSortedByName() {
        String s = "Select r from Category r Order by r.name";
        return em.createQuery(s, Category.class).getResultList();
    }

    @Override
    public void removeCategory(int id) {
        em.getTransaction().begin();
        em.remove(em.find(Category.class, id));
        em.getTransaction().commit();
    }
}
