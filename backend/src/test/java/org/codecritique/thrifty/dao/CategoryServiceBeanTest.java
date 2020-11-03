package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

import static org.codecritique.thrifty.Generator.*;
import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceBeanTest extends BaseServiceBeanTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ExpenseService expenseService;

    @Test
    void testStoreCategory() {
        Category category = categorySupplier.get();
        categoryService.store(category);
        assertEquals(category, categoryService.getCategory(category.getId()));
    }

    @Test
    void testGetCategoriesSortedByName() {

        int numEntities = 10;

        for (int i = 0; i < numEntities; i++)
            categoryService.store(categorySupplier.get());

        Iterator<String> it = categoryService.getCategories()
                .stream().map(Category::getName).iterator();

        while (it.hasNext()) {
            String name = it.next();
            if (it.hasNext()) {
                assertTrue(name.compareTo(it.next()) <= 0);
            }
        }
    }

    @Test
    void testUpdateCategory() {
        //setup
        Category category = categorySupplier.get();
        categoryService.store(category);
        Expense expense = expenseSupplier.get();
        expense.setCategory(category);
        expenseService.store(expense);

        //exercise
        category.setName(stringSupplier.get());
        category.setDescription(stringSupplier.get());
        categoryService.updateCategory(category);

        //verify
        assertEquals(category, categoryService.getCategory(category.getId()));
        assertEquals(category, expense.getCategory());
    }

    @Test
    void testRemoveCategory() {
        //setup
        Category category = categorySupplier.get();
        categoryService.store(category);

        //exercise
        categoryService.removeCategory(category.getId());

        //verify
        assertNull(categoryService.getCategory(category.getId()));
    }
}