package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.codecritique.thrifty.Generator.*;
import static org.junit.jupiter.api.Assertions.*;

class CategoryDaoTest extends BaseDaoTest {

    @Test
    void shouldStoreCategory() {
        Category category = categorySupplier.get();
        categoryDao.store(category);
        assertEquals(category, categoryDao.getCategory(category.getId()));
    }

    @Test
    void shouldGetCategoriesSortedByName() {

        int numEntities = 10;

        for (int i = 0; i < numEntities; i++)
            categoryDao.store(categorySupplier.get());

        Iterator<String> it = categoryDao.getCategories(accountId)
                .stream().map(Category::getName).iterator();

        while (it.hasNext()) {
            String name = it.next();
            if (it.hasNext()) {
                assertTrue(name.compareTo(it.next()) <= 0);
            }
        }
    }

    @Test
    void shouldUpdateCategory() {
        //setup
        Category category = categorySupplier.get();
        categoryDao.store(category);
        Expense expense = expenseSupplier.get();
        expense.setCategory(category);
        expenseDao.store(expense);

        //exercise
        category.setName(stringSupplier.get());
        category.setDescription(stringSupplier.get());
        categoryDao.updateCategory(category);

        //verify
        assertEquals(category, categoryDao.getCategory(category.getId()));
        assertEquals(category, expense.getCategory());
    }

    @Test
    void shouldRemoveCategory() {
        //setup
        Category category = categorySupplier.get();
        categoryDao.store(category);

        //exercise
        categoryDao.removeCategory(category.getId());

        //verify
        assertNull(categoryDao.getCategory(category.getId()));
    }

}