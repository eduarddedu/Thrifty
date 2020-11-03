package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;

import java.util.List;

/**
 * @author Eduard Dedu
 */


public interface CategoryService {

    void store(Category o);

    Category getCategory(long id);

    List<Category> getCategories();

    void removeCategory(long id);

    void updateCategory(Category o);
}
