package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;

import java.util.List;

/**
 * @author Eduard Dedu
 */


public interface CategoryService {

    void store(Category o);

    Category get(int id);

    List<Category> getCategoriesSortedByName();

    void remove(int id);

    void update(Category o);
}
