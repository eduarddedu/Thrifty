package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;

import java.util.List;

public interface CategoryService {

    void addCategory(Category o);
    Category getCategory(int id);
    List<Category> getCategoriesSortedByName();
    void removeCategory(int id);
}
