package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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
        return super.getEntitiesSortedByName(Category.class);
    }

    @Override
    public void removeCategory(int id) {
        super.removeEntity(Category.class, id);
    }
}
