package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryServiceBean extends BaseServiceBean {

    public List<Category> getCategories() {
        String sql = "Select r from Category r Order by r.name";
        return em.createQuery(sql, Category.class)
                .getResultList();
    }

    public Category getCategoryById(int id) {
        return em.find(Category.class, id);
    }
}
