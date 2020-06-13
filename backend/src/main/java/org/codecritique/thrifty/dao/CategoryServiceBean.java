package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryServiceBean extends AbstractServiceBean {

    public List<Category> getCategories() {
        return em.createQuery("Select r from Category r Order by r.name", Category.class)
                .getResultList();
    }

    public Category getCategoryById(int id) {
        return em.find(Category.class, id);
    }
}
