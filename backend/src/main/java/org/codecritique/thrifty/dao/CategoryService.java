package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;

import java.util.List;

/**
 * @author Eduard Dedu
 */


public interface CategoryService {

    void store(Category o);

    Category get(long id);

    List<Category> getCategories();

    void remove(long id);

    void update(Category o);
}
