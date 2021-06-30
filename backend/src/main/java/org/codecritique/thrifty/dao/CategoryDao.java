package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;

import java.util.List;

public interface CategoryDao {

    void store(Category o);

    Category getCategory(long id);

    List<Category> getCategories(long accountId);

    void removeCategory(long id);

    void updateCategory(Category o);
}
