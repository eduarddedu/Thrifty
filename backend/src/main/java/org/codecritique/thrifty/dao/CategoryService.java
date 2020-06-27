package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;

import java.util.List;

public interface CategoryService {

    void addCategory(Category c);
    Category getCategory(int id);
    List<Category> getCategories();
    void removeCategory(int id);
    void updateCategory(Category c);
}
